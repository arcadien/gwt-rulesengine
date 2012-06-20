package net.bobosse.gwt.rulesengine.client.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.bobosse.gwt.rulesengine.client.Report;
import net.bobosse.gwt.rulesengine.client.Rule;
import net.bobosse.gwt.rulesengine.client.RuleHandler;
import net.bobosse.gwt.rulesengine.client.RulesEngine;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

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
public class SingleFactRulesEngineImpl implements RulesEngine {

	private static final Logger logger = Logger
			.getLogger(SingleFactRulesEngineImpl.class);

	private final Map<Rule, RuleHandler> rulesMap = new HashMap<Rule, RuleHandler>();

	private Report report;

	/**
	 * {@link RuleHandler} implementation for {@link SingleFactRulesEngineImpl}
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
			((SingleFactRulesEngineImpl) engine).removeRule(rule);
		}

		@Override
		public Rule getRule() {
			return rule;
		}
	}

	public SingleFactRulesEngineImpl() {
		this.report = new Report();
	}
	
	@Override
	public RuleHandler addRule(Rule rule) {
		RuleHandlerImpl handler = new RuleHandlerImpl(rule, this);
		getRulesMap().put(rule, handler);
		return handler;
	}

	public void removeRule(Rule rule) {
		getRulesMap().remove(rule);

	}

	@Override
	public void processFact(Object fact) {
		processFact(fact, getReport());
	}

	@Override
	public void processFact(Object fact, Report report) {
		List<Rule> allRulesList = new ArrayList<Rule>(rulesMap.keySet());
		for (Rule rule : allRulesList) {
			logger.log(Level.DEBUG, "#processFact() executes rule " + rule);
			rule.execute(fact, report);
		}
	}

	/**
	 * package protected
	 * 
	 * @return
	 */
	private Map<Rule, RuleHandler> getRulesMap() {
		return rulesMap;
	}

	public List<Rule> getRules() {
		return new ArrayList<Rule>(rulesMap.keySet());
	}

	@Override
	public Report getReport() {
		return report;
	}
	
	@Override
	public void clearReport() {
		report = new Report();
		
	}

}
