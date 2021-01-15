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

package org.apache.cayenne.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.cayenne.access.event.SnapshotEvent;
import org.apache.cayenne.test.parallel.ParallelTestContainer;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;

/**
 */
public class EventBridgeTest {

	public EventBridge mockEventBridge1(EventSubject localSubject, String externalSubject) {
		CayenneEvent[] mockFieldVariableLastLocalEvent = new CayenneEvent[1];
		EventBridge mockInstance = Mockito.mock(EventBridge.class, Mockito.withSettings()
				.useConstructor(localSubject, externalSubject).defaultAnswer(Mockito.CALLS_REAL_METHODS));
		try {
			Mockito.doAnswer((stubInvo) -> {
				CayenneEvent event = stubInvo.getArgument(0);
				mockFieldVariableLastLocalEvent[0] = event;
				return null;
			}).when(mockInstance).sendExternalEvent(Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				return null;
			}).when(mockInstance).shutdownExternal();
			Mockito.doAnswer((stubInvo) -> {
				return null;
			}).when(mockInstance).startupExternal();
		} catch (Exception exception) {
		}
		return mockInstance;
	}

	private List<DefaultEventManager> managersToClean = new ArrayList<>();

	@After
	public void cleanEventManagers() {
		for (DefaultEventManager manager : managersToClean) {
			manager.shutdown();
		}
		managersToClean.clear();
	}

	@Test
	public void testConstructor() throws Exception {
		EventSubject local = EventSubject.getSubject(EventBridgeTest.class, "testInstall");
		String external = "externalSubject";
		EventBridge bridge = mockEventBridge1(local, external);

		Collection subjects = bridge.getLocalSubjects();
		assertEquals(1, subjects.size());
		assertTrue(subjects.contains(local));
		assertEquals(external, bridge.getExternalSubject());
	}

	@Test
	public void testStartup() throws Exception {
		EventSubject local = EventSubject.getSubject(EventBridgeTest.class, "testInstall");
		String external = "externalSubject";
		EventBridge bridge = Mockito.mock(EventBridge.class,
				Mockito.withSettings().useConstructor(local, external).defaultAnswer(Mockito.CALLS_REAL_METHODS));
		CayenneEvent[] bridgeLastLocalEvent = new CayenneEvent[1];
		try {
			Mockito.doAnswer((stubInvo) -> {
				CayenneEvent event = stubInvo.getArgument(0);
				bridgeLastLocalEvent[0] = event;
				return null;
			}).when(bridge).sendExternalEvent(Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				return null;
			}).when(bridge).shutdownExternal();
			Mockito.doAnswer((stubInvo) -> {
				return null;
			}).when(bridge).startupExternal();
		} catch (Exception exception) {
		}

		DefaultEventManager manager = new DefaultEventManager();
		managersToClean.add(manager);
		bridge.startup(manager, EventBridge.RECEIVE_LOCAL_EXTERNAL);

		assertSame(manager, bridge.eventManager);
		Mockito.verify(bridge, Mockito.times(1)).startupExternal();
		Mockito.verify(bridge, Mockito.times(0)).shutdownExternal();

		// try startup again
		DefaultEventManager newManager = new DefaultEventManager();
		managersToClean.add(newManager);
		bridge.startup(newManager, EventBridge.RECEIVE_LOCAL_EXTERNAL);

		assertSame(newManager, bridge.eventManager);
		Mockito.verify(bridge, Mockito.times(2)).startupExternal();
		Mockito.verify(bridge, Mockito.times(1)).shutdownExternal();
	}

	@Test
	public void testShutdown() throws Exception {
		EventSubject local = EventSubject.getSubject(EventBridgeTest.class, "testInstall");
		String external = "externalSubject";
		EventBridge bridge = Mockito.mock(EventBridge.class,
				Mockito.withSettings().useConstructor(local, external).defaultAnswer(Mockito.CALLS_REAL_METHODS));
		CayenneEvent[] bridgeLastLocalEvent = new CayenneEvent[1];
		try {
			Mockito.doAnswer((stubInvo) -> {
				CayenneEvent event = stubInvo.getArgument(0);
				bridgeLastLocalEvent[0] = event;
				return null;
			}).when(bridge).sendExternalEvent(Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				return null;
			}).when(bridge).shutdownExternal();
			Mockito.doAnswer((stubInvo) -> {
				return null;
			}).when(bridge).startupExternal();
		} catch (Exception exception) {
		}

		DefaultEventManager manager = new DefaultEventManager();
		managersToClean.add(manager);
		bridge.startup(manager, EventBridge.RECEIVE_LOCAL_EXTERNAL);
		bridge.shutdown();

		assertNull(bridge.eventManager);
		Mockito.verify(bridge, Mockito.times(1)).startupExternal();
		Mockito.verify(bridge, Mockito.times(1)).shutdownExternal();
	}

	@Test
	public void testSendExternalEvent() throws Exception {

		final EventSubject local = EventSubject.getSubject(EventBridgeTest.class, "testInstall");
		String external = "externalSubject";
		final EventBridge bridge = Mockito.mock(EventBridge.class,
				Mockito.withSettings().useConstructor(local, external).defaultAnswer(Mockito.CALLS_REAL_METHODS));
		CayenneEvent[] bridgeLastLocalEvent = new CayenneEvent[1];
		try {
			Mockito.doAnswer((stubInvo) -> {
				CayenneEvent event = stubInvo.getArgument(0);
				bridgeLastLocalEvent[0] = event;
				return null;
			}).when(bridge).sendExternalEvent(Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				return null;
			}).when(bridge).shutdownExternal();
			Mockito.doAnswer((stubInvo) -> {
				return null;
			}).when(bridge).startupExternal();
		} catch (Exception exception) {
		}

		DefaultEventManager manager = new DefaultEventManager(2);
		managersToClean.add(manager);
		bridge.startup(manager, EventBridge.RECEIVE_LOCAL_EXTERNAL);

		final SnapshotEvent eventWithNoSubject = new SnapshotEvent(this, this, null, null, null, null);

		manager.postEvent(eventWithNoSubject, local);

		// check that event was received and that subject was injected...

		// since bridge is notified asynchronously by default,
		// we must wait till notification is received
		ParallelTestContainer helper = new ParallelTestContainer() {

			@Override
			protected void assertResult() throws Exception {
				assertTrue(bridgeLastLocalEvent[0] instanceof SnapshotEvent);
				assertEquals(local, bridgeLastLocalEvent[0].getSubject());
			}
		};

		helper.runTest(5000);

		final SnapshotEvent eventWithSubject = new SnapshotEvent(this, this, null, null, null, null);
		eventWithSubject.setSubject(local);
		manager.postEvent(eventWithNoSubject, local);

		// check that event was received and that subject was injected...

		// since bridge is notified asynchronously by default,
		// we must wait till notification is received
		ParallelTestContainer helper1 = new ParallelTestContainer() {

			@Override
			protected void assertResult() throws Exception {
				assertTrue(bridgeLastLocalEvent[0] instanceof SnapshotEvent);
				assertEquals(local, bridgeLastLocalEvent[0].getSubject());
			}
		};

		helper1.runTest(5000);
	}
}
