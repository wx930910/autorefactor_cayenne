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

package org.apache.cayenne.tx;

import static org.junit.Assert.assertEquals;

import javax.sql.DataSource;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.di.Inject;
import org.apache.cayenne.log.JdbcEventLogger;
import org.apache.cayenne.testdo.testmap.Artist;
import org.apache.cayenne.unit.di.server.CayenneProjects;
import org.apache.cayenne.unit.di.server.ServerCase;
import org.apache.cayenne.unit.di.server.UseServerRuntime;
import org.junit.Test;
import org.mockito.Mockito;

@UseServerRuntime(CayenneProjects.TESTMAP_PROJECT)
public class UserTransactionIT extends ServerCase {

	@Inject
	private ObjectContext context;

	@Inject
	private JdbcEventLogger logger;

	@Test
	public void testCommit() throws Exception {

		Artist a = context.newObject(Artist.class);
		a.setArtistName("AAA");

		Transaction t = Mockito.mock(Transaction.class);
		Transaction tDelegate;
		tDelegate = new CayenneTransaction(logger);
		try {
			Mockito.doAnswer((stubInvo) -> {
				tDelegate.rollback();
				return null;
			}).when(t).rollback();
			Mockito.doAnswer((stubInvo) -> {
				TransactionListener listener = stubInvo.getArgument(0);
				tDelegate.addListener(listener);
				return null;
			}).when(t).addListener(Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				tDelegate.begin();
				return null;
			}).when(t).begin();
			Mockito.doAnswer((stubInvo) -> {
				tDelegate.setRollbackOnly();
				return null;
			}).when(t).setRollbackOnly();
			Mockito.when(t.getOrCreateConnection(Mockito.any(), Mockito.any())).thenAnswer((stubInvo) -> {
				String connectionName = stubInvo.getArgument(0);
				DataSource dataSource = stubInvo.getArgument(1);
				return tDelegate.getOrCreateConnection(connectionName, dataSource);
			});
			Mockito.doAnswer((stubInvo) -> {
				tDelegate.commit();
				return null;
			}).when(t).commit();
			Mockito.when(t.isRollbackOnly()).thenAnswer((stubInvo) -> {
				return tDelegate.isRollbackOnly();
			});
			Mockito.when(t.getConnections()).thenAnswer((stubInvo) -> {
				return tDelegate.getConnections();
			});
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		BaseTransaction.bindThreadTransaction(t);

		try {
			context.commitChanges();
		} finally {
			t.rollback();
			BaseTransaction.bindThreadTransaction(null);
		}

		Mockito.verify(t, Mockito.times(0)).commit();
		assertEquals(1, t.getConnections().size());
	}

}
