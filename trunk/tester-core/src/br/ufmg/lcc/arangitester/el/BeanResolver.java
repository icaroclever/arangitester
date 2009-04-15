package br.ufmg.lcc.arangitester.el;

import javax.el.BeanELResolver;
import javax.el.ELContext;

import br.ufmg.lcc.arangitester.util.Refletions;

public class BeanResolver extends BeanELResolver{
	public Object getValue(ELContext context, Object base, Object property) {

		if (context == null) {
			throw new NullPointerException();
		}

		if (base == null || property == null) {
			return null;
		}

		Object value = null;
		try{
			value = Refletions.getFieldValue((String)property, base);
		}catch(Throwable e){
			return null;
		}
		
		context.setPropertyResolved(true);			
		
		return value;
	}
}