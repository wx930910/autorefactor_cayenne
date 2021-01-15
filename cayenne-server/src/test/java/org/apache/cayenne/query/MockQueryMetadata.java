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

package org.apache.cayenne.query;

import org.mockito.Mockito;

public class MockQueryMetadata {

	static public QueryMetadata mockQueryMetadata1() {
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
			}).when(mockInstance).getCacheKey();
			Mockito.doAnswer((stubInvo) -> {
				return null;
			}).when(mockInstance).getDataMap();
		} catch (Exception exception) {
		}
		return mockInstance;
	}
}
