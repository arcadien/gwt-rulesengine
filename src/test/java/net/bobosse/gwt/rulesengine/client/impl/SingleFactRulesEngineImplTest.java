/**
 *    Copyright  2012 Aurélien Labrosse <aurelien.labrosse@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package net.bobosse.gwt.rulesengine.client.impl;

import java.util.List;

import net.bobosse.gwt.rulesengine.client.Rule;
import net.bobosse.gwt.rulesengine.client.RuleHandler;
import net.bobosse.gwt.rulesengine.client.impl.engines.SingleFactRulesEngine;
import net.bobosse.gwt.rulesengine.client.impl.engines.SingleFactRulesEngine.OrderMode;
import net.bobosse.gwt.rulesengine.client.impl.rules.RegexRule;

import org.junit.Assert;
import org.junit.Test;

public class SingleFactRulesEngineImplTest {

	@Test
	public void testAddRule() {
		SingleFactRulesEngine engine = new SingleFactRulesEngine(
				OrderMode.INSERT);

		engine.addRule(new RegexRule("test pattern", "[a-z]"));

		Assert.assertEquals(1, engine.getRules().size());
	}

	@Test
	public void testDispose() {

		SingleFactRulesEngine engine = new SingleFactRulesEngine(
				OrderMode.INSERT);

		RuleHandler handler = engine.addRule(new RegexRule("test pattern",
				"[a-z]", 100));

		Assert.assertEquals(1, engine.getRules().size());

		handler.dispose();

		Assert.assertEquals(0, engine.getRules().size());

	}

	@Test
	public void testRulesSortingModeInsert()
	{
		SingleFactRulesEngine engine = new SingleFactRulesEngine(
				OrderMode.INSERT);

		RegexRule first = new RegexRule("first", "[a-z]", -100);
		RegexRule second = new RegexRule("second", "[a-z]", -1);
		RegexRule third = new RegexRule("third", "[a-z]", 10);
		RegexRule fourth=new RegexRule("fourth", "[a-z]", 100);
		RegexRule fifth=new RegexRule("fifth", "[a-z]", 10);
		List<RegexRule> mixed = (java.util.Arrays.asList(new RegexRule[] {
				second,
				fourth,
				first,
				third,
				fifth}));

		engine.addRule(mixed.get(0));
		engine.addRule(mixed.get(1));
		engine.addRule(mixed.get(2));
		engine.addRule(mixed.get(3));
		engine.addRule(mixed.get(4));
		
		List<Rule> sortedRules = engine.getRules();

		Assert.assertEquals(mixed, sortedRules);
	}
	
	@Test
	public void testRulesSortingModeSalience()
	{
		SingleFactRulesEngine engine = new SingleFactRulesEngine(
				OrderMode.SALIENCE);

		RegexRule first = new RegexRule("first", "[a-z]", -100);
		RegexRule second = new RegexRule("second", "[a-z]", -1);
		RegexRule third = new RegexRule("third", "[a-z]", 10);
		RegexRule fourth=new RegexRule("fourth", "[a-z]", 10);
		RegexRule fifth=new RegexRule("fifth", "[a-z]", 100);
		
		engine.addRule(second);
		engine.addRule(third);
		engine.addRule(fourth);
		engine.addRule(first);
		engine.addRule(fifth);
		
		List<RegexRule> sorted = (java.util.Arrays.asList(new RegexRule[] {
				first,
				second,
				third,
				fourth,
				fifth}));
		
		List<Rule> sortedRules = engine.getRules();

		Assert.assertEquals(sorted, sortedRules);
	}
	
	
	@Test
	public void removeRule()
	{
		SingleFactRulesEngine engine = new SingleFactRulesEngine(
				OrderMode.SALIENCE);

		RegexRule first = new RegexRule("first", "[a-z]", -100);
		RegexRule second = new RegexRule("second", "[a-z]", -1);
		RegexRule third = new RegexRule("third", "[a-z]", 10);
		RegexRule fourth=new RegexRule("fourth", "[a-z]", 10);
		RegexRule fifth=new RegexRule("fifth", "[a-z]", 100);
		
		engine.addRule(second);
		RuleHandler handler = engine.addRule(third);
		engine.addRule(fourth);
		engine.addRule(first);
		engine.addRule(fifth);
		
		handler.dispose();
		
		List<RegexRule> expect = (java.util.Arrays.asList(new RegexRule[] {
				first,
				second,
				fourth,
				fifth}));
		
		List<Rule> sortedRules = engine.getRules();

		Assert.assertEquals(expect, sortedRules);
	}
	
	
	
	
}
