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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.cayenne.di.Inject;
import org.apache.cayenne.query.MockQuery;
import org.apache.cayenne.query.Query;
import org.apache.cayenne.query.SelectQuery;
import org.apache.cayenne.testdo.testmap.Artist;
import org.apache.cayenne.testdo.testmap.Gallery;
import org.apache.cayenne.unit.di.server.CayenneProjects;
import org.apache.cayenne.unit.di.server.ServerCase;
import org.apache.cayenne.unit.di.server.UseServerRuntime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests various DataContextDelegate methods invocation and consequences on
 * DataContext behavior.
 */
@UseServerRuntime(CayenneProjects.TESTMAP_PROJECT)
public class DataContextDelegateIT extends ServerCase {

	public DataContextDelegate mockDataContextDelegate4(List<Query> queriesPerformed) {
		DataContextDelegate mockInstance = Mockito.spy(DataContextDelegate.class);
		try {
			Mockito.doAnswer((stubInvo) -> {
				Query query = stubInvo.getArgument(1);
				queriesPerformed.add(query);
				return query;
			}).when(mockInstance).willPerformGenericQuery(Mockito.any(), Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				Query query = stubInvo.getArgument(1);
				return query;
			}).when(mockInstance).willPerformQuery(Mockito.any(), Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				return true;
			}).when(mockInstance).shouldProcessDelete(Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				return true;
			}).when(mockInstance).shouldMergeChanges(Mockito.any(), Mockito.any());
		} catch (Exception exception) {
		}
		return mockInstance;
	}

	public DataContextDelegate mockDataContextDelegate3(List<Query> queriesPerformed) {
		DataContextDelegate mockInstance = Mockito.spy(DataContextDelegate.class);
		try {
			Mockito.doAnswer((stubInvo) -> {
				Query query = stubInvo.getArgument(1);
				return query;
			}).when(mockInstance).willPerformQuery(Mockito.any(), Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				return true;
			}).when(mockInstance).shouldProcessDelete(Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				Query query = stubInvo.getArgument(1);
				queriesPerformed.add(query);
				return null;
			}).when(mockInstance).willPerformGenericQuery(Mockito.any(), Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				return true;
			}).when(mockInstance).shouldMergeChanges(Mockito.any(), Mockito.any());
		} catch (Exception exception) {
		}
		return mockInstance;
	}

	public DataContextDelegate mockDataContextDelegate2(List<Query> queriesPerformed) {
		DataContextDelegate mockInstance = Mockito.spy(DataContextDelegate.class);
		try {
			Mockito.doAnswer((stubInvo) -> {
				Query query = stubInvo.getArgument(1);
				queriesPerformed.add(query);
				return query;
			}).when(mockInstance).willPerformQuery(Mockito.any(), Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				Query query = stubInvo.getArgument(1);
				return query;
			}).when(mockInstance).willPerformGenericQuery(Mockito.any(), Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				return true;
			}).when(mockInstance).shouldProcessDelete(Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				return true;
			}).when(mockInstance).shouldMergeChanges(Mockito.any(), Mockito.any());
		} catch (Exception exception) {
		}
		return mockInstance;
	}

	public DataContextDelegate mockDataContextDelegate1(List<Query> queriesPerformed) {
		DataContextDelegate mockInstance = Mockito.spy(DataContextDelegate.class);
		try {
			Mockito.doAnswer((stubInvo) -> {
				Query query = stubInvo.getArgument(1);
				return query;
			}).when(mockInstance).willPerformGenericQuery(Mockito.any(), Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				return true;
			}).when(mockInstance).shouldProcessDelete(Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				Query query = stubInvo.getArgument(1);
				queriesPerformed.add(query);
				return null;
			}).when(mockInstance).willPerformQuery(Mockito.any(), Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				return true;
			}).when(mockInstance).shouldMergeChanges(Mockito.any(), Mockito.any());
		} catch (Exception exception) {
		}
		return mockInstance;
	}

	@Inject
	private DataContext context;

	@Before
	public void setUp() throws Exception {
		// prepare a single gallery record
		Gallery gallery = (Gallery) context.newObject("Gallery");
		gallery.setGalleryName("version1");

		// prepare a single artist record
		Artist artist = (Artist) context.newObject("Artist");
		artist.setArtistName("version1");

		context.commitChanges();
	}

	@Test
	public void testWillPerformGenericQuery() throws Exception {

		final List<Query> queriesPerformed = new ArrayList<Query>(1);
		DataContextDelegate delegate = mockDataContextDelegate4(queriesPerformed);
		context.setDelegate(delegate);

		// test that delegate is consulted before select
		MockQuery query = new MockQuery();
		context.performGenericQuery(query);

		assertTrue("Delegate is not notified of a query being run.", queriesPerformed.contains(query));
		assertEquals(1, queriesPerformed.size());
		assertTrue("Delegate unexpectedly blocked the query.", query.isRouteCalled());
	}

	@Test
	public void testWillPerformGenericQueryBlocked() throws Exception {

		final List<Query> queriesPerformed = new ArrayList<Query>(1);
		DataContextDelegate delegate = mockDataContextDelegate3(queriesPerformed);

		context.setDelegate(delegate);
		MockQuery query = new MockQuery();
		context.performGenericQuery(query);

		assertTrue("Delegate is not notified of a query being run.", queriesPerformed.contains(query));
		assertEquals(1, queriesPerformed.size());
		assertFalse("Delegate couldn't block the query.", query.isRouteCalled());
	}

	@Test
	public void testWillPerformQuery() throws Exception {

		final List<Query> queriesPerformed = new ArrayList<Query>(1);
		DataContextDelegate delegate = mockDataContextDelegate2(queriesPerformed);
		context.setDelegate(delegate);

		// test that delegate is consulted before select
		SelectQuery query = new SelectQuery(Gallery.class);
		List<?> results = context.performQuery(query);

		assertTrue("Delegate is not notified of a query being run.", queriesPerformed.contains(query));
		assertEquals(1, queriesPerformed.size());
		assertNotNull(results);
	}

	@Test
	public void testWillPerformQueryBlocked() throws Exception {

		final List<Query> queriesPerformed = new ArrayList<Query>(1);
		DataContextDelegate delegate = mockDataContextDelegate1(queriesPerformed);

		context.setDelegate(delegate);
		SelectQuery query = new SelectQuery(Gallery.class);
		List<?> results = context.performQuery(query);

		assertTrue("Delegate is not notified of a query being run.", queriesPerformed.contains(query));
		assertEquals(1, queriesPerformed.size());

		assertNotNull(results);

		// blocked
		assertEquals("Delegate couldn't block the query.", 0, results.size());
	}
}
