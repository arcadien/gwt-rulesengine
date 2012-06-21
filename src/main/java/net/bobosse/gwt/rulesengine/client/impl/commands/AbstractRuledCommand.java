package net.bobosse.gwt.rulesengine.client.impl.commands;

import net.bobosse.gwt.rulesengine.client.Rule;
import net.bobosse.gwt.rulesengine.client.RuledCommand;

import com.allen_sauer.gwt.log.client.Log;

/**
 * Base {@link RuledCommand} implementation that handles <code>this.rule</code>
 * management only.
 * 
 * @author sesa202001
 * 
 */
public abstract class AbstractRuledCommand implements RuledCommand
{
	private Rule rule;

	@Override
	public void setRule(Rule rule)
	{		
		Log.debug(this + " linked to rule '" + rule + "'");
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
