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
package br.ufmg.lcc.arangitester.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;

/**
 * 
 * @author Lucas Gonçalves
 *
 */
public class TesterConfigReader {

	
	public static void main(String[] args) throws FileNotFoundException{
		//LccTesterConfigReader reader = new LccTesterConfigReader();
//		reader.write();
		//reader.read(new File("teste.xml"));
		
	}
	
	public void write() throws FileNotFoundException{
		Config config = new Config();
		ArrayList<ConfigEnv> envs = new ArrayList<ConfigEnv>();
		ConfigEnv evn = new ConfigEnv();
		evn.setName("lucas");
		envs.add(evn);
		config.setEnvironments(envs);
		XStream xtream = getXstream();
		xtream.toXML(config, new FileOutputStream("teste.xml"));
	}
	
	private static XStream getXstream(){
		XStream xtream = new XStream();
		xtream.processAnnotations(new Class[]{Config.class, ConfigDatabase.class, ConfigDumpFile.class, ConfigEnv.class});
		return xtream;
	}
	
	public static Config read(File file) throws FileNotFoundException{
		return (Config)getXstream().fromXML(new FileInputStream(file));
	}
}
