package br.ufmg.lcc.arangitester.el;

import javax.el.ArrayELResolver;
import javax.el.CompositeELResolver;
import javax.el.ListELResolver;
import javax.el.MapELResolver;
import javax.el.ResourceBundleELResolver;

/**
 * Needed to add the LccResolver.
 * @author Lucas Gonçalves
 *
 */
public class CompositeResolver extends CompositeELResolver{
	public CompositeResolver() {
		add(new ArrayELResolver(false));
		add(new ListELResolver(false));
		add(new ListElResolver());
		add(new MapELResolver(false));
		add(new ResourceBundleELResolver());
		add(new BeanResolver());
		add(new AnnotationResolver());
	}
}
