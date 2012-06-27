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
package net.bobosse.gwt.rulesengine.client.impl.rules;

import java.util.ArrayList;

import net.bobosse.gwt.rulesengine.client.Rule;
import net.bobosse.gwt.rulesengine.client.RuleHandler;
import net.bobosse.gwt.rulesengine.client.RulesEngine.OrderMode;
import net.bobosse.gwt.rulesengine.client.impl.commands.AbstractRuledCommand;
import net.bobosse.gwt.rulesengine.client.impl.engines.SingleFactRulesEngine;
import net.bobosse.gwt.rulesengine.client.impl.rules.RegexRule;

import org.junit.Assert;
import org.junit.Test;

public class RegexRuleTest
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

		RuleHandler handler = engine.addRule(new RegexRule("lower case only",
				"[a-z]", 100));
		handler.getRule().addCommand(new LogRuleAction(matchedRules));

		RuleHandler handler2 = engine.addRule(new RegexRule("upper case only",
				"[A-Z]", 100));
		handler2.getRule().addCommand(new LogRuleAction(matchedRules));

		engine.processFact("i must match");
		Assert.assertEquals("'lower case only' must match", true,
				matchedRules.contains(handler.getRule()));

		engine.processFact("AAAAA");
		Assert.assertEquals("'upper case only' must match", true,
				matchedRules.contains(handler2.getRule()));

	}

}
