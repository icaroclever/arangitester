package br.ufmg.lcc.arangitester.util;

import javax.el.ExpressionFactory;
import javax.el.PropertyNotFoundException;
import javax.el.ValueExpression;

import br.ufmg.lcc.arangitester.el.CompositeResolver;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

/**
 * Helper to Resolve El Expression.
 * After create a instance of LccElHelper, put some variable on scope of El with method
 * addVariable. After that will be possible resolve expressions.
 * 
 * @author Lucas Gonçalves
 *
 */
public class ElHelper {
	private ExpressionFactory factory = new ExpressionFactoryImpl();
	private SimpleContext context = new SimpleContext(new CompositeResolver());

	public void addVariable(String name, Object value){
		context.setVariable(name, factory.createValueExpression(value, Object.class));
	}
	
	public String resolveElExpression(String exprStr){
		ValueExpression expr =	factory.createValueExpression(context, exprStr, Object.class);
		String value = null;
		try{
			value = (String)expr.getValue(context);
		}catch (PropertyNotFoundException e){
			return null;
		}
		return value;
	}

}
