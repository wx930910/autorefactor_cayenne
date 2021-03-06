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
package org.apache.cayenne.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.apache.cayenne.query.QueryMetadata;
import org.apache.cayenne.util.Util;
import org.junit.Test;
import org.mockito.Mockito;

public class MapQueryCacheTest {

	public QueryMetadata mockQueryMetadata1() {
		QueryMetadata mockInstance = Mockito.spy(QueryMetadata.class);
		try {
			Mockito.doAnswer((stubInvo) -> {
				return null;
			}).when(mockInstance).getResultSetMapping();
			Mockito.doAnswer((stubInvo) -> {
				return null;
			}).when(mockInstance).getPathSplitAliases();
			Mockito.doAnswer((stubInvo) -> {
				return null;
			}).when(mockInstance).getCacheGroup();
			Mockito.doAnswer((stubInvo) -> {
				return "key";
			}).when(mockInstance).getCacheKey();
			Mockito.doAnswer((stubInvo) -> {
				return null;
			}).when(mockInstance).getClassDescriptor();
			Mockito.doAnswer((stubInvo) -> {
				return null;
			}).when(mockInstance).getDbEntity();
			Mockito.doAnswer((stubInvo) -> {
				return null;
			}).when(mockInstance).getObjEntity();
			Mockito.doAnswer((stubInvo) -> {
				return null;
			}).when(mockInstance).getProcedure();
			Mockito.doAnswer((stubInvo) -> {
				return null;
			}).when(mockInstance).getPrefetchTree();
			Mockito.doAnswer((stubInvo) -> {
				return -1;
			}).when(mockInstance).getFetchOffset();
			Mockito.doAnswer((stubInvo) -> {
				return null;
			}).when(mockInstance).getOriginatingQuery();
			Mockito.doAnswer((stubInvo) -> {
				return null;
			}).when(mockInstance).getCacheStrategy();
			Mockito.doAnswer((stubInvo) -> {
				return null;
			}).when(mockInstance).getDataMap();
		} catch (Exception exception) {
		}
		return mockInstance;
	}

	@Test
	public void testSerializability() throws Exception {

		MapQueryCache cache = new MapQueryCache(5);
		cache.put(mockQueryMetadata1(), new ArrayList<Object>());

		assertEquals(1, cache.size());

		MapQueryCache deserialized = (MapQueryCache) Util.cloneViaSerialization(cache);
		assertNotNull(deserialized);
		assertEquals(1, deserialized.size());
	}
}
