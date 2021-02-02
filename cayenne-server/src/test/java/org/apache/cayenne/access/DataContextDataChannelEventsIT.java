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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.cayenne.DataChannelListener;
import org.apache.cayenne.di.Inject;
import org.apache.cayenne.graph.GraphEvent;
import org.apache.cayenne.test.parallel.ParallelTestContainer;
import org.apache.cayenne.testdo.testmap.Artist;
import org.apache.cayenne.unit.di.server.CayenneProjects;
import org.apache.cayenne.unit.di.server.ServerCaseContextsSync;
import org.apache.cayenne.unit.di.server.UseServerRuntime;
import org.apache.cayenne.util.EventUtil;
import org.junit.Test;

/**
 * Tests that DataContext sends DataChannel events.
 */
@UseServerRuntime(CayenneProjects.TESTMAP_PROJECT)
public class DataContextDataChannelEventsIT extends ServerCaseContextsSync {

	@Inject
	private DataContext context;

	@Test
	public void testRollbackEvent() throws Exception {
		Artist a = context.newObject(Artist.class);
		a.setArtistName("X");
		context.commitChanges();

		final MockChannelListener listener = new MockChannelListener();
		EventUtil.listenForChannelEvents(context, listener);

		a.setArtistName("Y");
		context.rollbackChanges();

		new ParallelTestContainer() {

			@Override
			protected void assertResult() throws Exception {
				assertFalse(listener.graphCommitted);
				assertFalse(listener.graphChanged);
				assertTrue(listener.graphRolledBack);
			}
		}.runTest(10000);
	}

	class MockChannelListener implements DataChannelListener {

		boolean graphChanged;
		boolean graphCommitted;
		boolean graphRolledBack;

		public void graphChanged(GraphEvent event) {
			graphChanged = true;
		}

		public void graphFlushed(GraphEvent event) {
			graphCommitted = true;
		}

		public void graphRolledback(GraphEvent event) {
			graphRolledBack = true;
		}
	}
}
