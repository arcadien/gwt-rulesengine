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
package net.bobosse.gwt.rulesengine.client.impl.engines;

import net.bobosse.gwt.rulesengine.client.Report;
import net.bobosse.gwt.rulesengine.client.Session;
import net.bobosse.gwt.rulesengine.client.RulesEngine.OrderMode;
import net.bobosse.gwt.rulesengine.client.impl.commands.LogFactVerbRuleCommand;
import net.bobosse.gwt.rulesengine.client.impl.engines.SingleFactRulesEngine;
import net.bobosse.gwt.rulesengine.client.impl.rules.RegexRule;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ScenarioTest
{

	@BeforeClass
	public static void initLog()
	{

		// Log.addLogger(new ConsoleLogger());
		// Log.setCurrentLogLevel(Log.LOG_LEVEL_DEBUG);
	}

	/**
	 * 
	 * Here is the scenario : - fact is the last word of a string - some regex
	 * based rules with logged actions log each matches as :
	 * <code>'[fact]' triggers [rule]</code>
	 * 
	 * - If action occurs, log success
	 */
	@Test
	public void testScenario()
	{

		// "a" rule propose targets
		// trigger lowercase rule
		String token1 = "a";

		// trigger lowercase rule
		String token2 = "tar";

		// trigger lowercase rule, domain rule
		String token3 = "target lo";

		// trigger lowercase rule, domain rule, instance rule
		String token4 = "target loc";

		// trigger lowercase rule, domain rule, instance rule, with rule
		String token5 = "target location";

		RegexRule lcRule = new RegexRule("lowercase", "[a-z]");
		lcRule.addCommand(new LogFactVerbRuleCommand("triggers"));

		RegexRule domainRule = new RegexRule("domain", "target");
		domainRule.addCommand(new LogFactVerbRuleCommand("triggers"));

		RegexRule instanceRule = new RegexRule("instance", "loc");
		instanceRule.addCommand(new LogFactVerbRuleCommand("triggers"));

		RegexRule withRule = new RegexRule("with", "location");
		withRule.addCommand(new LogFactVerbRuleCommand("triggers"));

		SingleFactRulesEngine engine = new SingleFactRulesEngine();
		engine.addRule(lcRule);
		engine.addRule(domainRule);
		engine.addRule(instanceRule);
		engine.addRule(withRule);

		Report report = new Report();
		
		Session session = engine.createStatelessSession(OrderMode.INSERT, report);
		
		System.out.println("---------- <token1> -----------");
		session.processFact(token1);
		System.out.println("---------- <token2> -----------");
		session.processFact(token2);
		System.out.println("---------- <token3> -----------");
		session.processFact(token3);
		System.out.println("---------- <token4> -----------");
		session.processFact(token4);
		System.out.println("---------- <token5> -----------");
		session.processFact(token5);

		
		// token1
		// trigger lowercase rule
		Assert.assertTrue("#1 'a' must trigger rule : lowercase",
				report.contains("'a' triggers 'lowercase'"));

		// token 2
		// trigger lowercase rule
		Assert.assertTrue("#2 'tar' must trigger rule : lowercase",
				report.contains("'tar' triggers 'lowercase'"));

		// token 3
		// trigger lowercase rule, domain rule
		Assert.assertTrue("#3 '" + token3 + "' must trigger rule : lowercase",
				report.contains("'" + token3 + "' triggers 'lowercase'"));
		Assert.assertTrue("#3 '" + token3 + "' must trigger rule : domain",
				report.contains("'" + token3 + "' triggers 'domain'"));

		// token 4
		// trigger lowercase , domain , instance
		Assert.assertTrue("#4 '" + token4 + "' must trigger rule : lowercase",
				report.contains("'" + token4 + "' triggers 'lowercase'"));

		Assert.assertTrue("#4 '" + token4 + "' must trigger rule :domain",
				report.contains("'" + token4 + "' triggers 'domain'"));

		Assert.assertTrue("#4 '" + token4 + "' must trigger rule : instance",
				report.contains("'" + token4 + "' triggers 'instance'"));

		// token 5
		// trigger lowercase , domain , instance , with
		Assert.assertTrue("#5 '" + token5 + "' must trigger rule : lowercase",
				report.contains("'" + token5 + "' triggers 'lowercase'"));

		Assert.assertTrue("#5 '" + token5 + "' must trigger rule : domain",
				report.contains("'" + token5 + "' triggers 'domain'"));

		Assert.assertTrue("#5 '" + token5 + "' must trigger rule : instance",
				report.contains("'" + token5 + "' triggers 'instance'"));

		Assert.assertTrue("#5 '" + token5 + "' must trigger rule : with",
				report.contains("'" + token5 + "' triggers 'with'"));
	}
}
