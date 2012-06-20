package net.bobosse.gwt.rulesengine.client.impl;

import net.bobosse.gwt.rulesengine.client.RuledCommand;
import net.bobosse.gwt.rulesengine.client.Rule;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Base {@link RuledCommand} implementation that handles <code>this.rule</code>
 * management only.
 * 
 * @author sesa202001
 * 
 */
public abstract class AbstractRuledCommandImpl implements RuledCommand
{
	private static final Logger logger = Logger.getLogger(AbstractRuleImpl.class);
	private Rule rule;

	@Override
	public void setRule(Rule rule)
	{		
		logger.log(Level.DEBUG, this + " linked to rule '" + rule + "'");
		this.rule = rule;
	}

	@Override
	public Rule getRule()
	{
		return rule;
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
