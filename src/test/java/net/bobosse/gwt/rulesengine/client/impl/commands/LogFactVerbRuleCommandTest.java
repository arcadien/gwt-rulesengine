/**
 * Copyright  2012 Aurélien Labrosse <aurelien.labrosse@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.bobosse.gwt.rulesengine.client.impl.commands;

import static org.junit.Assert.*;

import net.bobosse.gwt.rulesengine.client.Report;
import net.bobosse.gwt.rulesengine.client.impl.commands.LogFactVerbRuleCommand;
import net.bobosse.gwt.rulesengine.client.impl.rules.RegexRule;

import org.junit.Test;

public class LogFactVerbRuleCommandTest
{

	@Test
	public void testLogFormat()
	{

		Report report = new Report();
		RegexRule rule = new RegexRule("rule", "[a-z]");
		
		System.out.println(rule.toString());
		
		LogFactVerbRuleCommand cmd = new LogFactVerbRuleCommand("verb");
		rule.addCommand(cmd);
		rule.execute("fact", report);

		assertEquals("'fact' verb 'rule'", report.get(0));
	}

}
