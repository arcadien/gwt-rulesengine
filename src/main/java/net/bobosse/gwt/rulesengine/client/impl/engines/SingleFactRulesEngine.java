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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.bobosse.gwt.rulesengine.client.Report;
import net.bobosse.gwt.rulesengine.client.Rule;
import net.bobosse.gwt.rulesengine.client.RuleHandler;
import net.bobosse.gwt.rulesengine.client.RulesEngine;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;

/**
 * This engine aims single fact processing. It could be used to trigger various
 * actions when an user types into a {@link TextBox}, using a {@link SuggestBox}
 * that uses more that one {@link SuggestOracle}, for example. This simple rule
 * engine could help to delegate to the right {@link SuggestOracle}.
 * 
 * @author sesa202001
 * 
 */
public class SingleFactRulesEngine implements RulesEngine {

	/**
	 * How the result of <code>getRules(OrderMode mode)</code> will be sorted.<br />
	 * <ul>
	 * <li>OrderMode.SALIENCE : sorted by salience value. remember default is
	 * -1. If two rules has same salience value, the older one (early added)
	 * will appear first.</li>
	 * <li>OrderMode.INSERT : sorted in same order they were added</li>
	 * </ul>
	 * 
	 * @author Aurélien Labrosse <aurelien.labrosse@gmail.com>
	 * 
	 */
	public enum OrderMode {
		SALIENCE, INSERT
	}

	private final Map<Integer, RuleHandler> rulesMap = new HashMap<Integer, RuleHandler>();

	private final Report report;

	/**
	 * How the result of <code>getRules(OrderMode mode)</code> will be sorted.<br />
	 * <ul>
	 * <li>OrderMode.SALIENCE : sorted by salience value. remember default is -1
	 * </li>
	 * <li>OrderMode.INSERT : sorted in same order they were added</li>
	 * </ul>
	 * 
	 * @author Aurélien Labrosse <aurelien.labrosse@gmail.com>
	 * 
	 */
	private OrderMode mode;

	/**
	 * {@link RuleHandler} implementation for {@link SingleFactRulesEngine}
	 * 
	 * @author sesa202001
	 * 
	 */
	public class RuleHandlerImpl implements RuleHandler {

		private Rule rule;
		private RulesEngine engine;

		protected RuleHandlerImpl(Rule rule, RulesEngine engine) {
			this.rule = rule;
			this.engine = engine;
		}

		@Override
		public void dispose() {
			rule.passivate();
			rule.clearCommands();
			((SingleFactRulesEngine) engine).removeRule(rule);

		}

		@Override
		public Rule getRule() {
			return rule;
		}
	}

	public SingleFactRulesEngine(OrderMode mode) {
		this.report = new Report();
		this.mode = mode;
	}
	
	
	public SingleFactRulesEngine(OrderMode mode, Report report) {
		this.report = report;
		this.mode = mode;
	}
	
	@Override
	public RuleHandler addRule(Rule rule) {
		RuleHandlerImpl handler = new RuleHandlerImpl(rule, this);
		getRulesMap().put(getRulesMap().size(), handler);
		return handler;
	}

	/**
	 * removeRule may only be used trough {@link RuleHandler}
	 * 
	 * @param rule
	 *            to remove
	 */
	private synchronized void removeRule(Rule rule) {
		synchronized (rulesMap) {
			boolean found = false;
			int i = -1;
			for (i = 0; i <= rulesMap.size() - 1; i++) {
				if (rulesMap.get(i).getRule().equals(rule)) {
					found = true;
					break;
				}
			}
			if (found) {
				getRulesMap().remove(i);
			}
		}
	}

	@Override
	public void processFact(Object fact) {
		processFact(fact, getReport());
	}

	@Override
	public void processFact(Object fact, Report report) {
		for (Rule rule : getRules()) {
			Log.debug("#processFact() executes rule " + rule);
			rule.execute(fact, report);
		}
	}

	/**
	 * package protected
	 * 
	 * @return
	 */
	private Map<Integer, RuleHandler> getRulesMap() {
		return rulesMap;
	}

	/**
	 * 
	 * @return rules list in the order they where inserted
	 */
	public List<Rule> getRules() {
		ArrayList<Rule> rules = new ArrayList<Rule>(rulesMap.keySet().size());
		for (RuleHandler rh : rulesMap.values()) {
			rules.add(rh.getRule());
		}

		if (mode == OrderMode.SALIENCE) {
			Collections.sort(rules, new Comparator<Rule>() {

				@Override
				public int compare(Rule o1, Rule o2) {
					if (o1.getSalience() == o2.getSalience()) {
						return 0;
					} else {
						return (o1.getSalience() < o2.getSalience()) ? -1 : 1;
					}
				}
			});
		}
		return rules;
	}

	@Override
	public Report getReport() {
		return report;
	}

	@Override
	public void clearReport() {
		report.clear();

	}

}
