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
package net.bobosse.gwt.rulesengine.client.impl.rules;

import java.util.ArrayList;
import java.util.List;

import net.bobosse.gwt.rulesengine.client.Report;
import net.bobosse.gwt.rulesengine.client.Rule;

import org.junit.Assert;
import org.junit.Test;

public class AbstractRuleTest {

	private AbstractRule rule = createRule("default");

	private static AbstractRule createRule(final String name) {
		return new AbstractRule(name, 10) {

			@Override
			public boolean execute(Object fact, Report report) {
				return true;
			}
		};
	}

	@Test
	public void testSalience() {
		Assert.assertEquals(10, rule.getSalience());
	}

	@Test(expected = IllegalStateException.class)
	public void testGetNullFact() {
		rule.getFact();
	}

	@Test(expected = IllegalStateException.class)
	public void testGetNullReport() {
		rule.getReport();
	}

	@Test
	public void testActivationStatus() {

		// rules are created active
		junit.framework.Assert.assertEquals(true, rule.isActive());

		// passivate rule
		rule.passivate();
		junit.framework.Assert.assertEquals(false, rule.isActive());

		// activate rule
		rule.activate();
		junit.framework.Assert.assertEquals(true, rule.isActive());
	}

	@Test
	public void testLinkedRules() {

		List<Rule> rules = new ArrayList<Rule>();

		rules.add(createRule("one"));
		rules.add(createRule("two"));

		rule.setFollowingRules(rules);
		rule.setPreceedingRules(rules);

		Assert.assertEquals(rules, rule.getFollowing());
		Assert.assertEquals(rules, rule.getPreceding());

	}
}
