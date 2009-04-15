package br.ufmg.lcc.arangitester.el;

import javax.el.ELContext;
import javax.el.ListELResolver;

import br.ufmg.lcc.arangitester.ui.GenericLine;
import br.ufmg.lcc.arangitester.ui.UiTable;

/**
 * 
 * @author Lucas Gonçalves
 *
 */
public class ListElResolver extends ListELResolver {

	@SuppressWarnings("unchecked")
	public Object getValue(ELContext context, Object base, Object property) {

		if (context == null) {
			throw new NullPointerException();
		}

		if (base != null && base instanceof UiTable) {
			context.setPropertyResolved(true);
			UiTable<? extends GenericLine> list = (UiTable<? extends GenericLine>) base;
			int index = toInteger(property);
			return list.getLine(index);
		}
		return null;
	}
	
	 private int toInteger(Object p) {
	        if (p instanceof Integer) {
	            return ((Integer) p).intValue();
	        }
	        if (p instanceof Character) {
	            return ((Character) p).charValue();
	        }
	        if (p instanceof Boolean) {
	            return ((Boolean) p).booleanValue()? 1: 0;
	        }
	        if (p instanceof Number) {
	            return ((Number) p).intValue();
	        }
	        if (p instanceof String) {
	            return Integer.parseInt((String) p);
	        }
	        throw new IllegalArgumentException();
	    }
}
