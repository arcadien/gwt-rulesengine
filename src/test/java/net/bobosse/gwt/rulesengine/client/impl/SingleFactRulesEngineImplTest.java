package net.bobosse.gwt.rulesengine.client.impl;

import net.bobosse.gwt.rulesengine.client.RuleHandler;

import org.junit.Assert;
import org.junit.Test;

public class SingleFactRulesEngineImplTest
{

	@Test
	public void testAddRule()
	{
		SingleFactRulesEngineImpl engine = new SingleFactRulesEngineImpl();

		engine.addRule(new RegexRuleImpl("test pattern", "[a-z]", null));

		Assert.assertEquals(1, engine.getOrderedRules().size());
	}

	@Test
	public void testDispose()
	{

		SingleFactRulesEngineImpl engine = new SingleFactRulesEngineImpl();

		RuleHandler handler = engine.addRule(new RegexRuleImpl(
				"test pattern", "[a-z]", null, 100));

		Assert.assertEquals(1, engine.getOrderedRules().size());

		handler.dispose();

		Assert.assertEquals(0, engine.getOrderedRules().size());

	}

}
