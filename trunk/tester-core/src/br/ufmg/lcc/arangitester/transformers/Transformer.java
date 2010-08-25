package br.ufmg.lcc.arangitester.transformers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class Transformer {

	public static void tranform(File fileIn, File fileOut, InputStream xslIs) throws Exception {
		FileOutputStream out = new FileOutputStream(fileOut);
		Result bufferResultado = new StreamResult(out);

		Source xsltSource = new StreamSource(xslIs);
		Source xmlSource = new StreamSource(new FileInputStream(fileIn));
		TransformerFactory fabricaTrans = TransformerFactory.newInstance();
		try {
			javax.xml.transform.Transformer transformador = fabricaTrans.newTransformer(xsltSource);
			try {
				transformador.transform(xmlSource, bufferResultado);
			} catch (TransformerException ex) {
				ex.printStackTrace();
			}

		} catch (TransformerConfigurationException ex) {
			ex.printStackTrace();
		}
		out.close();
	}

}
