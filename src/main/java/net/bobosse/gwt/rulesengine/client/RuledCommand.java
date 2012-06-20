package net.bobosse.gwt.rulesengine.client;

import com.google.gwt.user.client.Command;

public interface RuledCommand extends Command {
	/**
	 * Set rule that may trigger this action
	 * 
	 * @param rule
	 *            that may trigger this action (back reference. @fixme needed?)
	 */
	void setRule(Rule rule);

	/**
	 * @return {@link Rule} that may trigger this action
	 * 
	 */
	Rule getRule();
}
