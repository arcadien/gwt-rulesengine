package net.bobosse.gwt.rulesengine.client;

/**
 * 
 * Keep a reference on {@link Rule} to let it be
 * removed from {@link RulesEngine}.
 * 
 * @author sesa202001
 *
 */
public interface RuleHandler
{
	/**
	 * free rule and all its associations
	 */
	public void dispose();

	/**
	 * 
	 * @return backed {@link Rule}
	 */
	public Rule getRule();
	
}
