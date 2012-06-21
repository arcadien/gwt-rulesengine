package net.bobosse.gwt.rulesengine.client.impl;

import static org.junit.Assert.*;

import net.bobosse.gwt.rulesengine.client.Report;

import org.junit.Test;

public class LogFactVerbRuleCommandTest {

	@Test
	public void testLogFormat() {
		 
		Report report = new Report();
		RegexRule rule = new RegexRule("rule", "[a-z]");
		LogFactVerbRuleCommand cmd = new LogFactVerbRuleCommand("verb");
		rule.addCommand(cmd);
		rule.execute("fact", report);
		
		assertEquals("'fact' verb 'rule'", report.get(0));
	}

}
