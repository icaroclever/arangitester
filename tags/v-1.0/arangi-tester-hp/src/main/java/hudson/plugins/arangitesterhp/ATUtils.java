package hudson.plugins.arangitesterhp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class ATUtils {

	public static String getContents(File aFile,String screenshots) {
	    //...checks on aFile are elided
	    StringBuilder contents = new StringBuilder();
	    
	    try {
	      //use buffering, reading one line at a time
	      //FileReader always assumes default encoding is OK!
	      BufferedReader input =  new BufferedReader(new FileReader(aFile));
	      try {
	        String line = null; //not declared within while loop
	        /*
	        * readLine is a bit quirky :
	        * it returns the content of a line MINUS the newline.
	        * it returns null only for the END of the stream.
	        * it returns an empty String if two newlines appear in a row.
	        */
	        while (( line = input.readLine()) != null){
	          contents.append(line);
	          contents.append(System.getProperty("line.separator"));
	        }
	      }
	      finally {
	        input.close();
	      }
	    }
	    catch (IOException ex){
	      ex.printStackTrace();
	    }
	    
	    return contents.toString().replaceAll("%%SCREENSHOT%%",screenshots);
	  }
	
	/**
	 * Transform a xml file in a html file using the style xsl
	 * @param xml
	 * @param xsl
	 * @param html
	 * @throws IOException
	 */
	public static void xmlToHtml(File xml,File xsl, File html) throws IOException
	{
		// Pre-conditions
		if(xml == null) throw new RuntimeException("");
		if(xsl == null) throw new RuntimeException("");
		if(html == null) throw new RuntimeException("");
		
		// Transformation
		try{
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer(new StreamSource(xsl));
			transformer.transform( new StreamSource(xml),new StreamResult(new FileOutputStream(html)));
		}
		catch(TransformerException te){
			te.printStackTrace();
		}
	}
}
