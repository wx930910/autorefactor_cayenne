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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.cayenne.access.MockQueryEngine;
import org.apache.cayenne.di.Inject;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.map.EntityResolver;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.testdo.inheritance_people.Department;
import org.apache.cayenne.testdo.inheritance_people.Employee;
import org.apache.cayenne.testdo.inheritance_people.Manager;
import org.apache.cayenne.unit.di.server.PeopleProjectCase;
import org.junit.Test;
import org.mockito.Mockito;

public class SelectQueryPrefetchRouterActionQualifiedEntityIT extends PeopleProjectCase {

	@Inject
	private EntityResolver resolver;

	@Test
	public void testPrefetchEmployee() throws Exception {
		ObjEntity departmentEntity = resolver.getObjEntity(Department.class);
		SelectQuery q = new SelectQuery(Employee.class, ExpressionFactory.matchExp("name", "abc"));

		q.addPrefetch(Employee.TO_DEPARTMENT.disjoint());

		SelectQueryPrefetchRouterAction action = new SelectQueryPrefetchRouterAction();

		QueryRouter router = Mockito.spy(QueryRouter.class);
		List[] routerQueries = new List[] { new ArrayList() };
		try {
			Mockito.doAnswer((stubInvo) -> {
				return new MockQueryEngine();
			}).when(router).engineForDataMap(Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				return new MockQueryEngine();
			}).when(router).engineForName(Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				Query query = stubInvo.getArgument(1);
				routerQueries[0].add(query);
				return null;
			}).when(router).route(Mockito.any(), Mockito.any(), Mockito.any());
		} catch (Exception exception) {
		}
		action.route(q, router, resolver);
		assertEquals(1, routerQueries[0].size());

		PrefetchSelectQuery prefetch = (PrefetchSelectQuery) Collections.unmodifiableList(routerQueries[0]).get(0);

		assertSame(departmentEntity, prefetch.getRoot());
		assertEquals(ExpressionFactory.exp("db:employees.NAME = 'abc' and (db:employees.PERSON_TYPE = 'EE' "
				+ "or db:employees.PERSON_TYPE = 'EM')"), prefetch.getQualifier());
	}

	@Test
	public void testPrefetchManager() throws Exception {
		ObjEntity departmentEntity = resolver.getObjEntity(Department.class);
		SelectQuery q = new SelectQuery(Manager.class, ExpressionFactory.matchExp("name", "abc"));

		q.addPrefetch(Employee.TO_DEPARTMENT.disjoint());

		SelectQueryPrefetchRouterAction action = new SelectQueryPrefetchRouterAction();

		QueryRouter router = Mockito.spy(QueryRouter.class);
		List[] routerQueries = new List[] { new ArrayList() };
		try {
			Mockito.doAnswer((stubInvo) -> {
				return new MockQueryEngine();
			}).when(router).engineForDataMap(Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				return new MockQueryEngine();
			}).when(router).engineForName(Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				Query query = stubInvo.getArgument(1);
				routerQueries[0].add(query);
				return null;
			}).when(router).route(Mockito.any(), Mockito.any(), Mockito.any());
		} catch (Exception exception) {
		}
		action.route(q, router, resolver);
		assertEquals(1, routerQueries[0].size());

		PrefetchSelectQuery prefetch = (PrefetchSelectQuery) Collections.unmodifiableList(routerQueries[0]).get(0);
		assertSame(departmentEntity, prefetch.getRoot());
		assertEquals(ExpressionFactory.exp("db:employees.NAME = 'abc' and db:employees.PERSON_TYPE = 'EM'"),
				prefetch.getQualifier());
	}
}
