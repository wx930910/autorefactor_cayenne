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

package org.apache.cayenne.access.types;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import org.mockito.Mockito;

public class MockExtendedType {

	static public ExtendedType<Object> mockExtendedType2(Class<?> objectClass) {
		Class<?>[] mockFieldVariableObjectClass = new Class[1];
		ExtendedType<Object> mockInstance = Mockito.spy(ExtendedType.class);
		mockFieldVariableObjectClass[0] = objectClass;
		try {
			Mockito.doAnswer((stubInvo) -> {
				return mockFieldVariableObjectClass[0].newInstance();
			}).when(mockInstance).materializeObject(Mockito.any(CallableStatement.class), Mockito.anyInt(),
					Mockito.anyInt());
			Mockito.doAnswer((stubInvo) -> {
				return mockFieldVariableObjectClass[0].newInstance();
			}).when(mockInstance).materializeObject(Mockito.any(ResultSet.class), Mockito.anyInt(), Mockito.anyInt());
			Mockito.doAnswer((stubInvo) -> {
				return mockFieldVariableObjectClass[0].getName();
			}).when(mockInstance).getClassName();
			Mockito.doAnswer((stubInvo) -> {
				Object value = stubInvo.getArgument(0);
				if (value == null) {
					return "NULL";
				}
				return value.toString();
			}).when(mockInstance).toString(Mockito.any());
		} catch (Exception exception) {
		}
		return mockInstance;
	}

	static public ExtendedType<Object> mockExtendedType1() {
		Class<?>[] mockFieldVariableObjectClass = new Class[1];
		ExtendedType<Object> mockInstance = Mockito.spy(ExtendedType.class);
		mockFieldVariableObjectClass[0] = Object.class;
		try {
			Mockito.doAnswer((stubInvo) -> {
				return mockFieldVariableObjectClass[0].newInstance();
			}).when(mockInstance).materializeObject(Mockito.any(CallableStatement.class), Mockito.anyInt(),
					Mockito.anyInt());
			Mockito.doAnswer((stubInvo) -> {
				return mockFieldVariableObjectClass[0].newInstance();
			}).when(mockInstance).materializeObject(Mockito.any(ResultSet.class), Mockito.anyInt(), Mockito.anyInt());
			Mockito.doAnswer((stubInvo) -> {
				return mockFieldVariableObjectClass[0].getName();
			}).when(mockInstance).getClassName();
			Mockito.doAnswer((stubInvo) -> {
				Object value = stubInvo.getArgument(0);
				if (value == null) {
					return "NULL";
				}
				return value.toString();
			}).when(mockInstance).toString(Mockito.any());
		} catch (Exception exception) {
		}
		return mockInstance;
	}
}
