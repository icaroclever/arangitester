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
package org.arangitester.boot;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.lang.StringUtils;

/**
 * Find class finish with TestCase or FunctionalTest
 * 
 * @author Lucas Gonçalves
 * 
 */
public class Scanner {
	private List<Class<?>>	tests	= new ArrayList<Class<?>>();

	private void handle(Set<String> paths) {
		for (String urlPath : paths) {
			try {
				File file = new File(urlPath);
				if (file.isDirectory()) {
					handleDirectory(file, null);
				} else {
					handleArchiveByFile(file);
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	public void scan() throws IOException {
		Set<String> paths = new HashSet<String>();
		String resourceName = "lcc.properties";
		Enumeration<URL> urlEnum = getClass().getClassLoader().getResources(resourceName);
		while (urlEnum.hasMoreElements()) {
			String urlPath = urlEnum.nextElement().getFile();
			urlPath = URLDecoder.decode(urlPath, "UTF-8");
			if (urlPath.startsWith("file:")) {
				urlPath = urlPath.substring(5);
			}
			if (urlPath.indexOf('!') > 0) {
				urlPath = urlPath.substring(0, urlPath.indexOf('!'));
			} else {
				File dirOrArchive = new File(urlPath);
				if (resourceName != null && resourceName.lastIndexOf('/') > 0) {
					// for META-INF/components.xml
					dirOrArchive = dirOrArchive.getParentFile();
				}
				urlPath = dirOrArchive.getParent();
			}
			paths.add(urlPath);
		}
		handle(paths);
	}

	private void handleArchiveByFile(File file) throws IOException {
		ZipFile zip = new ZipFile(file);
		Enumeration<? extends ZipEntry> entries = zip.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			String name = entry.getName();
			handleItem(name);
		}
	}
	
	private void handleDirectory(File file, String path) {
		for (File child : file.listFiles()) {
			String newPath = path == null ? child.getName() : path + '/' + child.getName();
			if (child.isDirectory()) {
				handleDirectory(child, newPath);
			} else {
				handleItem(newPath);
			}
		}
	}

	/** Recover the paths of class items which their names end with "FunctionalTest", "TestCase" or "TestSuite".
	 *  @param name		absolute item path
	 */
	public void handleItem(String name) {

		if (name.endsWith("FunctionalTest.class") || name.endsWith("TestCase.class") || name.endsWith("TestSuite.class")) {
			try {
				name = StringUtils.substringBeforeLast(StringUtils.replace(name, "/", "."), ".");
				tests.add(getClass().getClassLoader().loadClass(name));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public List<Class<?>> getTests() {
		return tests;
	}

	public void setTests(List<Class<?>> tests) {
		this.tests = tests;
	}
}
