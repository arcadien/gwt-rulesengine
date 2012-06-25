package net.bobosse.gwt.rulesengine.client;

import java.util.List;

/**
 * A Rule process a fact (Object), and according to filters, may fire some
 * actions.<br />
 * 
 * @author sesa202001
 * 
 */
public interface Rule extends HasPreceeding<Rule>, HasFollowing<Rule>
{

	/**
	 * Add an {@link RuledCommand} to fire when rule matches
	 * 
	 * @param command
	 * @return
	 */
	public boolean addCommand(RuledCommand command);

	/**
	 * Remove an {@link RuledCommand} to fire when rule matches
	 * 
	 * @param command
	 * @return
	 */
	public boolean removeCommand(RuledCommand command);

	/**
	 * 
	 * @return list of {@link RuledCommand} to fire when rule matches
	 */
	public List<RuledCommand> getCommands();

	/**
	 * Rule salience is an int between -100 an 100. Higher is this value, higher
	 * is the priority of the rule. High priority rules will be executed first.
	 * 
	 * @return rule's salience
	 */
	public int getSalience();

	/**
	 * Execute business logic, using specific {@link Rule} implementation. This
	 * is the good place to call <code>executeCommands()</code>.
	 */
	void execute(Object fact, Report report);

	/**
	 * 
	 * 
	 * @return true if rule is active, false otherwise
	 */
	public boolean isActive();

	/**
	 * Set rule state to active
	 * 
	 * @return previous active status
	 */
	public boolean activate();

	/**
	 * Set rule state to passive (will not be processed)
	 * 
	 * @return previous active status
	 */
	public boolean passivate();

	/**
	 * Remove all actions triggered by this {@link Rule} when it matches.
	 * 
	 */
	public void clearCommands();

	/**
	 * 
	 * @return rule's name
	 */
	public String getName();

	/**
	 * 
	 * @return what is tested by this rule during current session
	 */
	public Object getFact();

	/**
	 * 
	 * @return
	 */
	public Report getReport();

	/**
	 * call each {@link RuledCommand}'s <code>execute()</code> method
	 */
	void executeCommands();

}
