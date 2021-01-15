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
package org.apache.cayenne.testdo.extended_type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.cayenne.access.types.ExtendedType;
import org.mockito.Mockito;

public class StringET1ExtendedType {

	static public ExtendedType<StringET1> mockExtendedType1() {
		ExtendedType<StringET1> mockInstance = Mockito.spy(ExtendedType.class);
		try {
			Mockito.doAnswer((stubInvo) -> {
				PreparedStatement statement = stubInvo.getArgument(0);
				StringET1 value = stubInvo.getArgument(1);
				int pos = stubInvo.getArgument(2);
				int type = stubInvo.getArgument(3);
				if (value != null) {
					statement.setString(pos, value.getString());
				} else {
					statement.setNull(pos, type);
				}
				return null;
			}).when(mockInstance).setJdbcObject(Mockito.any(), Mockito.any(), Mockito.anyInt(), Mockito.anyInt(),
					Mockito.anyInt());
			Mockito.doAnswer((stubInvo) -> {
				return StringET1.class.getName();
			}).when(mockInstance).getClassName();
			Mockito.doAnswer((stubInvo) -> {
				ResultSet rs = stubInvo.getArgument(0);
				int index = stubInvo.getArgument(1);
				String string = rs.getString(index);
				return string != null ? new StringET1(string) : null;
			}).when(mockInstance).materializeObject(Mockito.any(ResultSet.class), Mockito.anyInt(), Mockito.anyInt());
			Mockito.doAnswer((stubInvo) -> {
				StringET1 value = stubInvo.getArgument(0);
				if (value == null) {
					return "NULL";
				}
				return value.toString();
			}).when(mockInstance).toString(Mockito.any());
			Mockito.doAnswer((stubInvo) -> {
				CallableStatement rs = stubInvo.getArgument(0);
				int index = stubInvo.getArgument(1);
				String string = rs.getString(index);
				return string != null ? new StringET1(string) : null;
			}).when(mockInstance).materializeObject(Mockito.any(CallableStatement.class), Mockito.anyInt(),
					Mockito.anyInt());
		} catch (Exception exception) {
		}
		return mockInstance;
	}
}
