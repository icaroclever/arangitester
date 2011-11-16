/*
 * Copyright 2000 Universidade Federal de Minas Gerais.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arangitester.config;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ConfigEnvConverter implements Converter {

	public void marshal(Object arg0, HierarchicalStreamWriter arg1, MarshallingContext arg2) {
		System.out.println("entrou");
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		ConfigEnv env = new ConfigEnv();
		reader.moveDown();
		env.setName(reader.getAttribute("name"));
		return env;
	}

	public boolean canConvert(@SuppressWarnings("rawtypes") Class clazz) {
		return clazz.equals(ConfigEnv.class);
	}

}
