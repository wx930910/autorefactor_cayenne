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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.apache.cayenne.PersistenceState;
import org.apache.cayenne.di.Inject;
import org.apache.cayenne.query.Query;
import org.apache.cayenne.test.parallel.ParallelTestContainer;
import org.apache.cayenne.testdo.testmap.Artist;
import org.apache.cayenne.unit.di.server.CayenneProjects;
import org.apache.cayenne.unit.di.server.ServerCaseContextsSync;
import org.apache.cayenne.unit.di.server.UseServerRuntime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

@UseServerRuntime(CayenneProjects.TESTMAP_PROJECT)
public class DataContextDelegateSharedCacheIT extends ServerCaseContextsSync {

	public DataContextDelegate mockDataContextDelegate4(boolean[] methodInvoked) {
		DataContextDelegate mockInstance = Mockito.spy(DataContextDelegate.class);
		try {
			Mockito.doAnswer((stubInvo) -> {
				Query query = stubInvo.getArgument(1);
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
				methodInvoked[0] = true;
				return true;
			}).when(mockInstance).shouldMergeChanges(Mockito.any(), Mockito.any());
		} catch (Exception exception) {
		}
		return mockInstance;
	}

	public DataContextDelegate mockDataContextDelegate3() {
		DataContextDelegate mockInstance = Mockito.spy(DataContextDelegate.class);
		try {
			Mockito.doAnswer((stubInvo) -> {
				Query query = stubInvo.getArgument(1);
				return query;
			}).when(mockInstance).willPerformGenericQuery(Mockito.any(), Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				Query query = stubInvo.getArgument(1);
				return query;
			}).when(mockInstance).willPerformQuery(Mockito.any(), Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				return true;
			}).when(mockInstance).shouldProcessDelete(Mockito.any());
		} catch (Exception exception) {
		}
		return mockInstance;
	}

	public DataContextDelegate mockDataContextDelegate2(boolean[] methodInvoked) {
		DataContextDelegate mockInstance = Mockito.spy(DataContextDelegate.class);
		try {
			Mockito.doAnswer((stubInvo) -> {
				Query query = stubInvo.getArgument(1);
				return query;
			}).when(mockInstance).willPerformGenericQuery(Mockito.any(), Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				Query query = stubInvo.getArgument(1);
				return query;
			}).when(mockInstance).willPerformQuery(Mockito.any(), Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				methodInvoked[0] = true;
				return true;
			}).when(mockInstance).shouldProcessDelete(Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				return true;
			}).when(mockInstance).shouldMergeChanges(Mockito.any(), Mockito.any());
		} catch (Exception exception) {
		}
		return mockInstance;
	}

	public DataContextDelegate mockDataContextDelegate1(boolean[] methodInvoked) {
		DataContextDelegate mockInstance = Mockito.spy(DataContextDelegate.class);
		try {
			Mockito.doAnswer((stubInvo) -> {
				methodInvoked[0] = true;
				return false;
			}).when(mockInstance).shouldProcessDelete(Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				Query query = stubInvo.getArgument(1);
				return query;
			}).when(mockInstance).willPerformGenericQuery(Mockito.any(), Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				Query query = stubInvo.getArgument(1);
				return query;
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

	@Inject
	private DataContext context1;

	private Artist artist;

	@Before
	public void setUp() throws Exception {

		// prepare a single artist record
		artist = (Artist) context.newObject("Artist");
		artist.setArtistName("version1");
		artist.setDateOfBirth(new Date());
		context.commitChanges();
	}

	/**
	 * Test case to prove that delegate method is invoked on external change of
	 * object in the store.
	 */
	@Test
	public void testShouldMergeChanges() throws Exception {

		final boolean[] methodInvoked = new boolean[1];
		DataContextDelegate delegate = mockDataContextDelegate4(methodInvoked);

		// make sure we have a fully resolved copy of an artist object
		// in the second context
		Artist altArtist = context1.localObject(artist);
		assertNotNull(altArtist);
		assertNotSame(altArtist, artist);
		assertEquals(artist.getArtistName(), altArtist.getArtistName());
		assertEquals(PersistenceState.COMMITTED, altArtist.getPersistenceState());

		context1.setDelegate(delegate);

		// Update and save artist in peer context
		artist.setArtistName("version2");
		context.commitChanges();

		// assert that delegate was consulted when an object store
		// was refreshed
		ParallelTestContainer helper = new ParallelTestContainer() {

			@Override
			protected void assertResult() throws Exception {
				assertTrue("Delegate was not consulted", methodInvoked[0]);
			}
		};
		helper.runTest(3000);
	}

	/**
	 * Test case to prove that delegate method can block changes made by
	 * ObjectStore.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBlockedShouldMergeChanges() throws Exception {
		String oldName = artist.getArtistName();

		DataContextDelegate delegate = mockDataContextDelegate3();
		context1.setDelegate(delegate);

		// make sure we have a fully resolved copy of an artist object
		// in the second context
		Artist altArtist = context1.localObject(artist);
		assertNotNull(altArtist);
		assertFalse(altArtist == artist);
		assertEquals(oldName, altArtist.getArtistName());
		assertEquals(PersistenceState.COMMITTED, altArtist.getPersistenceState());

		// Update and save artist in peer context
		artist.setArtistName("version2");
		context.commitChanges();

		// assert that delegate was able to block the merge
		assertEquals(oldName, altArtist.getArtistName());
	}

	/**
	 * Test case to prove that delegate method is invoked on external change of
	 * object in the store.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testShouldProcessDeleteOnExternalChange() throws Exception {

		final boolean[] methodInvoked = new boolean[1];
		DataContextDelegate delegate = mockDataContextDelegate2(methodInvoked);
		context1.setDelegate(delegate);

		// make sure we have a fully resolved copy of an artist object
		// in the second context
		Artist altArtist = context1.localObject(artist);
		assertNotNull(altArtist);
		assertFalse(altArtist == artist);
		assertEquals(artist.getArtistName(), altArtist.getArtistName());
		assertEquals(PersistenceState.COMMITTED, altArtist.getPersistenceState());

		// Update and save artist in peer context
		context.deleteObjects(artist);
		context.commitChanges();

		// assert that delegate was consulted when an object store
		// was refreshed
		ParallelTestContainer helper = new ParallelTestContainer() {

			@Override
			protected void assertResult() throws Exception {
				assertTrue("Delegate was not consulted", methodInvoked[0]);
			}
		};
		helper.runTest(3000);
	}

	/**
	 * Test case to prove that delegate method is invoked on external change of
	 * object in the store, and is able to block further object processing.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBlockShouldProcessDeleteOnExternalChange() throws Exception {

		final boolean[] methodInvoked = new boolean[1];
		DataContextDelegate delegate = mockDataContextDelegate1(methodInvoked);
		context1.setDelegate(delegate);

		// make sure we have a fully resolved copy of an artist object
		// in the second context
		Artist altArtist = context1.localObject(artist);
		assertNotNull(altArtist);
		assertFalse(altArtist == artist);
		assertEquals(artist.getArtistName(), altArtist.getArtistName());
		assertEquals(PersistenceState.COMMITTED, altArtist.getPersistenceState());

		// Update and save artist in peer context
		context.deleteObjects(artist);
		context.commitChanges();

		// assert that delegate was consulted when an object store
		// was refreshed, and actually blocked object expulsion
		ParallelTestContainer helper = new ParallelTestContainer() {

			@Override
			protected void assertResult() throws Exception {
				assertTrue("Delegate was not consulted", methodInvoked[0]);
			}
		};
		helper.runTest(3000);
		assertEquals(PersistenceState.COMMITTED, altArtist.getPersistenceState());
		assertNotNull(altArtist.getObjectContext());
	}
}
