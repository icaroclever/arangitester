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
package org.arangitester.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.arangitester.exceptions.ArangiTesterException;


public class ArangiTesterStringUtils {

	public static String interpolate(String text, Object bean) {

		if (text == null)
			throw new ArangiTesterException("Cannot interpolate because text has been null.");

		if (bean == null)
			throw new ArangiTesterException("Cannot interpolate because bean has been null.");

		Pattern compile = Pattern.compile("#\\{([\\w]*)\\}");
		Matcher m = compile.matcher(text);
		while (m.find()) {
			String group = m.group();
			try {
				String propertyName = group.substring(2, group.length() - 1);
				String propertyValue = BeanUtils.getProperty(bean, propertyName);
				text = StringUtils.replace(text, group, propertyValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return text;
	}

	public static boolean containsWithoutSpaces(final String text1, final String text2) {
		String text1Ww = StringUtils.deleteWhitespace(text1);
		String text2Ww = StringUtils.deleteWhitespace(text2);
		return (text1Ww.contains(text2Ww));
	}

	public static boolean equalsWithoutSpaces(final String text1, final String text2) {
		String text1Ww = StringUtils.deleteWhitespace(text1);
		String text2Ww = StringUtils.deleteWhitespace(text2);
		// System.out.println("Text1: "+text1Ww+" Text2:"+text2Ww);
		// System.out.println("res: "+ text1Ww.equals(text2Ww));
		return (text1Ww.equals(text2Ww));
	}

	public static String fill(char character, int frequency) throws ArangiTesterException {
		if ((frequency < 0) || frequency > Integer.MAX_VALUE) {
			throw new ArangiTesterException("The parameter 'frequency' must be positive and less than Integer.MAX_VALUE");
		}
		String newString = "";
		for (int i = 0; i < frequency; i++) {
			newString += character;
		}
		return newString;
	}
}
