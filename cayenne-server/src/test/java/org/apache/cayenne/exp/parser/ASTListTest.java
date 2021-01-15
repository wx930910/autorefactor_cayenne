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
package org.apache.cayenne.exp.parser;

import org.apache.cayenne.ObjectId;
import org.apache.cayenne.Persistent;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ASTListTest {

    @Test
	public void testConstructorWithCollection() {
		ObjectId objectId = new ObjectId("Artist", "ARTIST_ID", 1);
		Persistent artist = mock(Persistent.class);
		when(artist.getObjectId()).thenReturn(objectId);

		ASTList exp = new ASTList(Collections.singletonList(artist));
		assertNotNull(exp.getOperand(0));

		List<Persistent> collection = new ArrayList<>();
		collection.add(artist);
		exp = new ASTList(collection);
		assertNotNull(exp.getOperand(0));
	}

	@Test
	public void testEquals() throws Exception {
		ObjectId objectId = new ObjectId("Artist", "ARTIST_ID", 1);
		Persistent artist = mock(Persistent.class);
		when(artist.getObjectId()).thenReturn(objectId);

		ASTList exp = new ASTList(Collections.singletonList(artist));

		List<Persistent> collection = new ArrayList<>();
		collection.add(artist);
		ASTList exp2 = new ASTList(collection);
		ASTList exp3 = new ASTList(Collections.emptyList());

		assertEquals(exp, exp2);
		assertNotEquals(exp, exp3);
	}

	@Test
	public void testHashCode() throws Exception {
		ObjectId objectId = new ObjectId("Artist", "ARTIST_ID", 1);
		Persistent artist = mock(Persistent.class);
		when(artist.getObjectId()).thenReturn(objectId);

		ASTList exp = new ASTList(Collections.singletonList(artist));
		List<Persistent> collection = new ArrayList<>();
		collection.add(artist);
		ASTList exp2 = new ASTList(collection);
		ASTList exp3 = new ASTList(Collections.emptyList());

		assertEquals(exp.hashCode(), exp2.hashCode());
		assertNotEquals(exp.hashCode(), exp3.hashCode());
	}
    
}
