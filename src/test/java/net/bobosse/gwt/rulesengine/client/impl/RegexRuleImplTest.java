package net.bobosse.gwt.rulesengine.client.impl;

import java.util.ArrayList;

import net.bobosse.gwt.rulesengine.client.Rule;
import net.bobosse.gwt.rulesengine.client.RuleHandler;

import org.junit.Assert;
import org.junit.Test;

public class RegexRuleImplTest
{

	private class LogRuleAction extends AbstractRuledCommandImpl{

		private ArrayList<Rule> matched;
		
		public LogRuleAction (ArrayList<Rule> matched)
		{
			this.matched = matched;
		}

		@Override
		public void execute() {
			matched.add(getRule());
			
		}
	}
	
	
	@Test
	public void testMatch()
	{

		SingleFactRulesEngineImpl engine = new SingleFactRulesEngineImpl();

		final ArrayList<Rule> matchedRules = new ArrayList<Rule>();

		RuleHandler handler = engine.addRule(new RegexRuleImpl(
				"lower case only", "[a-z]", null, 100));
		handler.getRule().addCommand(new LogRuleAction(matchedRules));
		
		RuleHandler handler2 = engine.addRule(new RegexRuleImpl(
				"upper case only", "[A-Z]", null, 100));
		handler2.getRule().addCommand(new LogRuleAction(matchedRules));

		
		
		engine.processFact("i must match");
		Assert.assertEquals("'lower case only' must match", true, matchedRules.contains(handler.getRule()));
		
		engine.processFact("AAAAA");
		Assert.assertEquals("'upper case only' must match", true, matchedRules.contains(handler2.getRule()));

	}

}
