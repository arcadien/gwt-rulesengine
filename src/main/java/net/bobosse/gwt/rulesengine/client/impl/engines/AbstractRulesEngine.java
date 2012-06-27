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

import java.io.Serializable;
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
import net.bobosse.gwt.rulesengine.client.Session;

import com.allen_sauer.gwt.log.client.Log;

/**
 * Base {@link RulesEngine} implementation that contains all plumber code as of
 * rules holding and sorting, a {@link RuleHandler} implementation.
 * 
 * @author Aurélien Labrosse <aurelien.labrosse@gmail.com>
 * 
 */
public class AbstractRulesEngine implements RulesEngine {

	private final Map<Integer, RuleHandler> rulesMap = new HashMap<Integer, RuleHandler>();

	/**
	 * Comparator that uses salience as sort key
	 * 
	 * @author Aurélien Labrosse <aurelien.labrosse@gmail.com>
	 * 
	 */
	private static class RuleSalienceComparator implements Comparator<Rule>,
			Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6749347239721865601L;

		@Override
		public int compare(Rule o1, Rule o2) {
			if (o1.getSalience() == o2.getSalience()) {
				return 0;
			} else {
				return (o1.getSalience() < o2.getSalience()) ? -1 : 1;
			}
		}
	};

	/**
	 * {@link RuleHandler} implementation for {@link AbstractRulesEngine}
	 * 
	 * @author Aurélien Labrosse <aurelien.labrosse@gmail.com>
	 */
	public static class RuleHandlerImpl implements RuleHandler {

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
			((AbstractRulesEngine) engine).removeRule(rule);

		}

		@Override
		public Rule getRule() {
			return rule;
		}
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

	protected void processFact(Object fact, Report report, OrderMode mode) {
		Log.debug("#processFact() " + mode.name() + " sort mode activated");
		for (Rule rule : getRules(mode)) {
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
	public List<Rule> getRules(OrderMode mode) {
		ArrayList<Rule> rules = new ArrayList<Rule>(rulesMap.keySet().size());
		for (RuleHandler rh : rulesMap.values()) {
			rules.add(rh.getRule());
		}

		if (mode == OrderMode.SALIENCE) {
			Collections.sort(rules, new RuleSalienceComparator());
		}
		return rules;
	}

	@Override
	public Session createStatelessSession(final OrderMode mode,
			final Report report) {
		return new Session() {

			@Override
			public void processFact(Object fact, Report dedicatedReport) {
				AbstractRulesEngine.this.processFact(fact, dedicatedReport,
						mode);
			}

			@Override
			public void processFact(Object fact) {
				AbstractRulesEngine.this.processFact(fact, report, mode);
			}

			@Override
			public boolean addFact(Object fact) {
				// TODO Auto-generated method stub
				throw new IllegalStateException("Not implemented yet");
			}

			@Override
			public boolean retractFact(Object fact) {
				// TODO Auto-generated method stub
				throw new IllegalStateException("Not implemented yet");
			}

			@Override
			public void fireAllRules() {
				// TODO Auto-generated method stub
				throw new IllegalStateException("Not implemented yet");
			}

			@Override
			public void dispose() {
				// nothing to do for stateless
			}
		};
	}
}
