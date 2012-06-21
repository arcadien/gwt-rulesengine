package net.bobosse.gwt.rulesengine.client.impl;

import java.util.List;

import net.bobosse.gwt.rulesengine.client.Rule;
import net.bobosse.gwt.rulesengine.client.RuleHandler;
import net.bobosse.gwt.rulesengine.client.impl.SingleFactRulesEngineImpl.OrderMode;

import org.junit.Assert;
import org.junit.Test;

public class SingleFactRulesEngineImplTest {

	@Test
	public void testAddRule() {
		SingleFactRulesEngineImpl engine = new SingleFactRulesEngineImpl(OrderMode.INSERT);

		engine.addRule(new RegexRule("test pattern", "[a-z]"));

		Assert.assertEquals(1, engine.getRules().size());
	}

	@Test
	public void testDispose() {

		SingleFactRulesEngineImpl engine = new SingleFactRulesEngineImpl(OrderMode.INSERT);

		RuleHandler handler = engine.addRule(new RegexRule("test pattern",
				"[a-z]", 100));

		Assert.assertEquals(1, engine.getRules().size());

		handler.dispose();

		Assert.assertEquals(0, engine.getRules().size());

	}

	@Test
	public void testSalienceEvaluation() {
		SingleFactRulesEngineImpl engine = new SingleFactRulesEngineImpl(OrderMode.INSERT);

		List<RegexRule> rules = (java.util.Arrays.asList(new RegexRule[] {
				new RegexRule("third", "[a-z]", 10),
				new RegexRule("second", "[a-z]", -1),
				new RegexRule("first", "[a-z]", -100),
				new RegexRule("fourth", "[a-z]", 100) }));
		
		engine.addRule(rules.get(0));
		engine.addRule(rules.get(1));
		engine.addRule(rules.get(2));
		engine.addRule(rules.get(3));

		List<Rule> sortedRules = engine.getRules();
		
	//	Assert.assertEquals(1, engine.getRules().size());
	}

}
