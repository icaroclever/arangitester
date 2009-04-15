package br.ufmg.lcc.arangitester.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import br.ufmg.lcc.arangitester.exceptions.LccException;

public class LccStringUtils {

	public static String interpolate(String text, Object bean){
		Pattern compile = Pattern.compile("#\\{([\\w]*)\\}");
		Matcher m= compile.matcher(text);
		while (m.find ()){
			String group = m.group();
			try {
				String propertyName = group.substring(2, group.length() -1);
				String propertyValue = BeanUtils.getProperty(bean, propertyName);
				text = StringUtils.replace(text, group, propertyValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
		return text;
	}
	
	public static boolean containsWithoutSpaces(final String text1, final String text2){
		String text1Ww = StringUtils.deleteWhitespace(text1);
		String text2Ww = StringUtils.deleteWhitespace(text2);
		return(text1Ww.contains(text2Ww));
	}
	
	public static boolean equalsWithoutSpaces(final String text1, final String text2){
		String text1Ww = StringUtils.deleteWhitespace(text1);
		String text2Ww = StringUtils.deleteWhitespace(text2);
		//System.out.println("Text1: "+text1Ww+" Text2:"+text2Ww);
		//System.out.println("res: "+ text1Ww.equals(text2Ww));
		return(text1Ww.equals(text2Ww));
	}
	
	public static String fill( char character, int frequency ) throws LccException {
		if( (frequency < 0) || frequency > Integer.MAX_VALUE ) {
			throw new LccException("The parameter 'frequency' must be positive and less than Integer.MAX_VALUE");
		}
		String newString = "";
		for( int i = 0; i < frequency; i++ ) {
			newString+=character;
		}
		return newString;
	}
}
