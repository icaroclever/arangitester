package br.ufmg.lcc.arangitester.config;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ConfigEnvConverter implements Converter {

	@Override
	public void marshal(Object arg0, HierarchicalStreamWriter arg1,	MarshallingContext arg2) {
		System.out.println("entrou");
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		ConfigEnv env = new ConfigEnv();
		reader.moveDown();
		env.setName(reader.getAttribute("name"));
		return env;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(ConfigEnv.class);
	}

}
