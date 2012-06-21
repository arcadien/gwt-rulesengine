package net.bobosse.gwt.rulesengine.client;

/**
 * A simple rules engine that backs a {@link Rule} structure and try to match
 * them against a <code>fact</code>. If a {@link Rule} matches, then all its
 * {@link RuledCommand} are fired. Else, nothing is done and the next
 * {@link Rule} is processed until no rule remains.<br />
 * 
 * @author sesa202001
 * 
 */
public interface RulesEngine
{

	RuleHandler addRule(Rule rule);

	/**
	 * process a fact trough rules
	 * 
	 * @param fact
	 */
	void processFact(Object fact);

	/**
	 * process a fact trough rules and specify a specific {@link Report}
	 * 
	 * @param fact
	 * @param report
	 */
	void processFact(Object fact, Report report);

	Report getReport();

	void clearReport();

}
