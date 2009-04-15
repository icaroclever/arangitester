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
