package net.bobosse.gwt.rulesengine.client.impl;

import java.util.ArrayList;

import net.bobosse.gwt.rulesengine.client.Rule;
import net.bobosse.gwt.rulesengine.client.RuleHandler;
import net.bobosse.gwt.rulesengine.client.impl.commands.AbstractRuledCommand;
import net.bobosse.gwt.rulesengine.client.impl.engines.SingleFactRulesEngine;
import net.bobosse.gwt.rulesengine.client.impl.engines.SingleFactRulesEngine.OrderMode;
import net.bobosse.gwt.rulesengine.client.impl.rules.NullOrEmptyRule;

import org.junit.Assert;
import org.junit.Test;

public class NullOrEmptyRuleImplTest
{

	private class LogRuleAction extends AbstractRuledCommand
	{

		private ArrayList<Rule> matched;

		public LogRuleAction (ArrayList<Rule> matched)
		{
			this.matched = matched;
		}

		@Override
		public void execute()
		{
			matched.add(getRule());
		}
	}

	@Test
	public void testMatch()
	{

		SingleFactRulesEngine engine = new SingleFactRulesEngine(
				OrderMode.INSERT);

		final ArrayList<Rule> matchedRules = new ArrayList<Rule>();

		RuleHandler handler = engine.addRule(new NullOrEmptyRule(
				"empty parameter", 100));

		handler.getRule().addCommand(new LogRuleAction(matchedRules));

		engine.processFact(" ".trim());
		Assert.assertEquals("Empty string matches too!", true,
				matchedRules.contains(handler.getRule()));

	}

}
