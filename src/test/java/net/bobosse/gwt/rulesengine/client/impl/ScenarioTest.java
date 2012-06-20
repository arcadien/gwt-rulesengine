package net.bobosse.gwt.rulesengine.client.impl;

import net.bobosse.gwt.rulesengine.client.Report;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ScenarioTest {

	
	@BeforeClass
	public static void initLog(){

//		  Log.addLogger(new ConsoleLogger());
//		  Log.setCurrentLogLevel(Log.LOG_LEVEL_DEBUG);
	} 
	
	/**
	 * 
	 * @return a new action instance, that trace in a string list the sentence
	 *         we expect.
	 */
	private class LogCommand extends AbstractRuledCommandImpl {

		@Override
		public void execute() {
			getRule().getReport().add("'" + getRule().getFact() + "'" + " triggers "
					+ getRule().getName());

		}
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
	public void testScenario() {

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

		RegexRuleImpl lcRule = new RegexRuleImpl("lowercase", "[a-z]");
		lcRule.addCommand(new LogCommand());

		RegexRuleImpl domainRule = new RegexRuleImpl("domain", "target");
		domainRule.addCommand(new LogCommand());

		RegexRuleImpl instanceRule = new RegexRuleImpl("instance", "loc");
		instanceRule.addCommand(new LogCommand());

		RegexRuleImpl withRule = new RegexRuleImpl("with", "location");
		withRule.addCommand(new LogCommand());

		SingleFactRulesEngineImpl engine = new SingleFactRulesEngineImpl();
		engine.addRule(lcRule);
		engine.addRule(domainRule);
		engine.addRule(instanceRule);
		engine.addRule(withRule);

		System.out.println("---------- <token1> -----------");
		engine.processFact(token1);
		System.out.println("---------- <token2> -----------");
		engine.processFact(token2);
		System.out.println("---------- <token3> -----------");
		engine.processFact(token3);
		System.out.println("---------- <token4> -----------");
		engine.processFact(token4);
		System.out.println("---------- <token5> -----------");
		engine.processFact(token5);

		Report ctx = engine.getReport();
		engine.clearReport();
		
		// token1
		// trigger lowercase rule
		Assert.assertTrue("#1 'a' must trigger rule : lowercase",
				ctx.contains("'a' triggers lowercase"));

		// token 2
		// trigger lowercase rule
		Assert.assertTrue("#2 'tar' must trigger rule : lowercase",
				ctx.contains("'tar' triggers lowercase"));

		// token 3
		// trigger lowercase rule, domain rule
		Assert.assertTrue("#3 '" + token3 + "' must trigger rule : lowercase",
				ctx.contains("'" + token3 + "' triggers lowercase"));
		Assert.assertTrue("#3 '" + token3 + "' must trigger rule : domain",
				ctx.contains("'" + token3 + "' triggers domain"));

		// token 4
		// trigger lowercase , domain , instance
		Assert.assertTrue("#4 '" + token4 + "' must trigger rule : lowercase",
				ctx.contains("'" + token4 + "' triggers lowercase"));

		Assert.assertTrue("#4 '" + token4 + "' must trigger rule :domain",
				ctx.contains("'" + token4 + "' triggers domain"));

		Assert.assertTrue("#4 '" + token4 + "' must trigger rule : instance",
				ctx.contains("'" + token4 + "' triggers instance"));

		// token 5
		// trigger lowercase , domain , instance , with
		Assert.assertTrue("#5 '" + token5 + "' must trigger rule : lowercase",
				ctx.contains("'" + token5 + "' triggers lowercase"));

		Assert.assertTrue("#5 '" + token5 + "' must trigger rule : domain",
				ctx.contains("'" + token5 + "' triggers domain"));

		Assert.assertTrue("#5 '" + token5 + "' must trigger rule : instance",
				ctx.contains("'" + token5 + "' triggers instance"));

		Assert.assertTrue("#5 '" + token5 + "' must trigger rule : with",
				ctx.contains("'" + token5 + "' triggers with"));
	}

}
