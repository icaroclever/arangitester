package br.ufmg.lcc.arangitester.ioc;

/**
 * Class that implement this interface will be invoked create() method after it
 * creation. All field handles already be executed!
 * 
 * @author Lucas Gonçalves
 * 
 */
public interface ICreate {
	public void create();
}
