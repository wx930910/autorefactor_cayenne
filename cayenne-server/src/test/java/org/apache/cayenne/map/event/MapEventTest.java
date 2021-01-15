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

package org.apache.cayenne.map.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mockito;

/**
 */
public class MapEventTest {

	public MapEvent mockMapEvent1(Object source, String newName) {
		String[] mockFieldVariableNewName = new String[1];
		MapEvent mockInstance = Mockito.mock(MapEvent.class,
				Mockito.withSettings().useConstructor(source).defaultAnswer(Mockito.CALLS_REAL_METHODS));
		mockFieldVariableNewName[0] = newName;
		try {
			Mockito.doAnswer((stubInvo) -> {
				return mockFieldVariableNewName[0];
			}).when(mockInstance).getNewName();
		} catch (Exception exception) {
		}
		return mockInstance;
	}

	public MapEvent mockMapEvent2(Object source, String newName, String oldName) {
		String[] mockFieldVariableNewName = new String[1];
		MapEvent mockInstance = Mockito.mock(MapEvent.class,
				Mockito.withSettings().useConstructor(source, oldName).defaultAnswer(Mockito.CALLS_REAL_METHODS));
		mockFieldVariableNewName[0] = newName;
		try {
			Mockito.doAnswer((stubInvo) -> {
				return mockFieldVariableNewName[0];
			}).when(mockInstance).getNewName();
		} catch (Exception exception) {
		}
		return mockInstance;
	}

	@Test
	public void testNoNameChange() throws Exception {
		MapEvent event = mockMapEvent1(new Object(), "someName");
		assertEquals("someName", event.getNewName());
		assertFalse(event.isNameChange());
	}

	@Test
	public void testNameChange() throws Exception {
		MapEvent event = mockMapEvent2(new Object(), "someName", "someOldName");
		assertEquals("someName", event.getNewName());
		assertTrue(event.isNameChange());
	}

	@Test
	public void testOldName() throws Exception {
		MapEvent event = mockMapEvent1(new Object(), "someName");
		assertNull(event.getOldName());

		event.setOldName("oldName");
		assertEquals("oldName", event.getOldName());
	}
}
