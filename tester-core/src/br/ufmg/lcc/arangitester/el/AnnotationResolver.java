package br.ufmg.lcc.arangitester.el;

import java.lang.annotation.Annotation;

import javax.el.BeanELResolver;
import javax.el.ELContext;

/**
 * Resolve methods for annotations.
 * @author Lucas Gonçalves
 *
 */
public class AnnotationResolver extends BeanELResolver {
	public Object getValue(ELContext context, Object base, Object property) {
		if (context == null) {
			throw new NullPointerException();
		}

		if (base == null || property == null || !(base instanceof Annotation)) {
			return null;
		}

		Object value = null;
		try {
			value = base.getClass().getMethod((String) property).invoke(base);
		} catch (Throwable e) {
			return null;
		}

		context.setPropertyResolved(true);

		return value;
	}

}
