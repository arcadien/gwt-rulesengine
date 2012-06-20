package net.bobosse.gwt.rulesengine.client.impl;

import java.util.ArrayList;

import net.bobosse.gwt.rulesengine.client.Rule;
import net.bobosse.gwt.rulesengine.client.RuleHandler;

import org.junit.Assert;
import org.junit.Test;

public class NullOrEmptyRuleImplTest {

	private class LogRuleAction extends AbstractRuledCommandImpl {

		private ArrayList<Rule> matched;

		public LogRuleAction(ArrayList<Rule> matched) {
			this.matched = matched;
		}

		@Override
		public void execute() {
			matched.add(getRule());
		}
	}

	@Test
	public void testMatch() {

		SingleFactRulesEngineImpl engine = new SingleFactRulesEngineImpl();

		final ArrayList<Rule> matchedRules = new ArrayList<Rule>();

		RuleHandler handler = engine.addRule(new NullOrEmptyRuleImpl(
				"empty parameter", null, 100));

		handler.getRule().addCommand(new LogRuleAction(matchedRules));

		engine.processFact(" ".trim());
		Assert.assertEquals("Empty string matches too!", true,
				matchedRules.contains(handler.getRule()));

	}

}
