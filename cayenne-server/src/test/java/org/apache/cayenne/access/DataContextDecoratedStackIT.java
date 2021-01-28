/*****************************************************************
 *   Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 ****************************************************************/
package org.apache.cayenne.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.Map;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.DataChannel;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.dba.frontbase.FrontBaseAdapter;
import org.apache.cayenne.dba.openbase.OpenBaseAdapter;
import org.apache.cayenne.di.Inject;
import org.apache.cayenne.graph.GraphDiff;
import org.apache.cayenne.query.Query;
import org.apache.cayenne.query.SQLTemplate;
import org.apache.cayenne.testdo.testmap.Artist;
import org.apache.cayenne.unit.di.server.CayenneProjects;
import org.apache.cayenne.unit.di.server.ServerCase;
import org.apache.cayenne.unit.di.server.UseServerRuntime;
import org.junit.Test;
import org.mockito.Mockito;

@UseServerRuntime(CayenneProjects.TESTMAP_PROJECT)
public class DataContextDecoratedStackIT extends ServerCase {

	@Inject
	private ServerRuntime runtime;

	@Test
	public void testCommitDecorated() {
		DataDomain dd = runtime.getDataDomain();
		DataChannel decorator = Mockito.mock(DataChannel.class);
		DataChannel[] decoratorChannel = new DataChannel[1];
		decoratorChannel[0] = dd;
		try {
			Mockito.when(decorator.onQuery(Mockito.any(), Mockito.any())).thenAnswer((stubInvo) -> {
				ObjectContext originatingContext = stubInvo.getArgument(0);
				Query queryMockVariable = stubInvo.getArgument(1);
				return decoratorChannel[0].onQuery(originatingContext, queryMockVariable);
			});
			Mockito.when(decorator.getEntityResolver()).thenAnswer((stubInvo) -> {
				return decoratorChannel[0].getEntityResolver();
			});
			Mockito.when(decorator.getEventManager()).thenAnswer((stubInvo) -> {
				return decoratorChannel[0].getEventManager();
			});
			Mockito.when(decorator.onSync(Mockito.any(), Mockito.any(), Mockito.anyInt())).thenAnswer((stubInvo) -> {
				ObjectContext originatingContext = stubInvo.getArgument(0);
				GraphDiff changes = stubInvo.getArgument(1);
				int syncType = stubInvo.getArgument(2);
				return decoratorChannel[0].onSync(originatingContext, changes, syncType);
			});
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		DataContext context = (DataContext) runtime.newContext(decorator);

		Artist a = context.newObject(Artist.class);
		a.setArtistName("XXX");
		context.commitChanges();

		SQLTemplate query = new SQLTemplate(Artist.class, "select #result('count(1)' 'int' 'x') from ARTIST");
		query.setFetchingDataRows(true);
		query.setTemplate(FrontBaseAdapter.class.getName(), "select #result('COUNT(ARTIST_ID)' 'int' 'x') from ARTIST");
		query.setTemplate(OpenBaseAdapter.class.getName(), "select #result('COUNT(ARTIST_ID)' 'int' 'x') from ARTIST");
		Map<?, ?> count = (Map<?, ?>) Cayenne.objectForQuery(context, query);
		assertNotNull(count);
		assertEquals(new Integer(1), count.get("x"));
	}

	@Test
	public void testGetParentDataDomain() {
		DataDomain dd = runtime.getDataDomain();
		DataChannel decorator = Mockito.mock(DataChannel.class);
		DataChannel[] decoratorChannel = new DataChannel[1];
		decoratorChannel[0] = dd;
		try {
			Mockito.when(decorator.onQuery(Mockito.any(), Mockito.any())).thenAnswer((stubInvo) -> {
				ObjectContext originatingContext = stubInvo.getArgument(0);
				Query query = stubInvo.getArgument(1);
				return decoratorChannel[0].onQuery(originatingContext, query);
			});
			Mockito.when(decorator.getEntityResolver()).thenAnswer((stubInvo) -> {
				return decoratorChannel[0].getEntityResolver();
			});
			Mockito.when(decorator.getEventManager()).thenAnswer((stubInvo) -> {
				return decoratorChannel[0].getEventManager();
			});
			Mockito.when(decorator.onSync(Mockito.any(), Mockito.any(), Mockito.anyInt())).thenAnswer((stubInvo) -> {
				ObjectContext originatingContext = stubInvo.getArgument(0);
				GraphDiff changes = stubInvo.getArgument(1);
				int syncType = stubInvo.getArgument(2);
				return decoratorChannel[0].onSync(originatingContext, changes, syncType);
			});
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		DataContext context = (DataContext) runtime.newContext(decorator);

		assertSame(dd, context.getParentDataDomain());
	}

}
