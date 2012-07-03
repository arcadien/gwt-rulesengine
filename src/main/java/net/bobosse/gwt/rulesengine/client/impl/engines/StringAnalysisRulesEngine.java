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
import net.bobosse.gwt.rulesengine.client.Rule;
import net.bobosse.gwt.rulesengine.client.RuleHandler;
import net.bobosse.gwt.rulesengine.client.RulesEngine;
import net.bobosse.gwt.rulesengine.client.impl.commands.LogFactVerbRuleCommand;
import net.bobosse.gwt.rulesengine.client.impl.rules.RegexRule;

import com.google.gwt.regexp.shared.MatchResult;

/**
 * This basic {@link RulesEngine} implementation only need one {@link Rule} to
 * be added. For now only {@link RegexRule} as rules and String as fact are
 * accepted.<br />
 * A string is analyzed trough the rule added to the engine, and then trough its
 * following rules. If no following rule match, then the first word is stripped
 * and the process restart from current rule following rules.
 * 
 * 
 * @author Aurélien Labrosse <aurelien.labrosse@gmail.com>
 * 
 */
public class StringAnalysisRulesEngine extends AbstractRulesEngine {

	private class RemoveWordRule extends RegexRule {
		public RemoveWordRule() {
			super("Default - remove first word", "^\\w*\\s*");
			addCommand(new LogFactVerbRuleCommand("removed by"));
		}
	}

	private RegexRule removeWordRule = new RemoveWordRule();

	/**
	 * default constructor
	 */
	public StringAnalysisRulesEngine() {
	}
	
	/**
	 * Constuctor that allow to set the {@link Rule} that will be used to match
	 * something to strip from the analysed string when no rules can obtain a
	 * match. By default, {@link RemoveWordRule} removes <code>^\\w*\\s*</code>.
	 * 
	 * @param removeTokenRule
	 */
	public StringAnalysisRulesEngine(RegexRule removeTokenRule) {
		removeWordRule = removeTokenRule;
	}

	@Override
	public RuleHandler addRule(Rule rule) {
		if (!(rule instanceof RegexRule)) {
			throw new IllegalStateException(
					"StringAnalysisRulesEngine only accepts RegexRule");
		}
		return super.addRule(rule);
	}

	@Override
	public void processFact(Object fact, Report report, OrderMode mode) {

		if (!(fact instanceof String)) {
			throw new IllegalStateException(
					"StringAnalysisRulesEngine only accepts String facts");
		}

		Rule firstRule = getRules(mode).get(0);
		if (null != firstRule) {
			processRule((String) fact, report, firstRule);
		}
	}

	private boolean processRule(String fact, Report report, Rule rule) {

		System.out.print("processing rule " + rule.getName() + " against '"
				+ fact + "' ..");

		if (/*fact.length() > 0 && */ rule.execute(fact, report)) {

			System.out.println("match");

			// something was identified, we remove it from analyzed string
			for (MatchResult mr : ((RegexRule) rule).getMatches()) {
				fact = fact.substring(mr.getGroup(0).length());
			}

			// process following rules until string totally consumed
			while (fact.length() > 0) {

				boolean matchedSmtg = false;
				for (Rule fRule : rule.getFollowing()) {
					if (processRule(fact, report, fRule)) {
						matchedSmtg = true;
						break;
					}
				}
				if (!matchedSmtg) {
					// nothing matched, try to remove one word,
					// and loop
					removeWordRule.execute(fact, report);
					for (MatchResult mr : removeWordRule.getMatches()) {
						fact = fact.substring(mr.getGroup(0).length());
					}
				} else {
					// rule has matched, return to preceding rule
					break;
				}
			}
			return true;
		} else {
			System.out.println("NO match");
			return false;
		}
	}
}
