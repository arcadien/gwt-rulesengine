package net.bobosse.gwt.rulesengine.client.impl;

import java.util.ArrayList;
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
public class SingleFactRulesEngineImpl implements RulesEngine
{

	private final Map<Integer, RuleHandler> rulesMap = new HashMap<Integer, RuleHandler>();

	private Report report;

	/**
	 * {@link RuleHandler} implementation for {@link SingleFactRulesEngineImpl}
	 * 
	 * @author sesa202001
	 * 
	 */
	public class RuleHandlerImpl implements RuleHandler
	{

		private Rule rule;
		private RulesEngine engine;

		protected RuleHandlerImpl (Rule rule, RulesEngine engine)
		{
			this.rule = rule;
			this.engine = engine;
		}

		@Override
		public void dispose()
		{
			rule.passivate();
			rule.clearCommands();
			((SingleFactRulesEngineImpl) engine).removeRule(rule);
		}

		@Override
		public Rule getRule()
		{
			return rule;
		}
	}

	public SingleFactRulesEngineImpl ()
	{
		this.report = new Report();
	}

	@Override
	public RuleHandler addRule(Rule rule)
	{
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
	private synchronized void removeRule(Rule rule)
	{
		synchronized (rulesMap)
		{
			boolean found = false;
			int i = -1;
			for(i = rulesMap.size() - 1; i >= 0; i--)
			{
				if(rulesMap.get(i).getRule().equals(rule))
				{
					found = true;
					break;
				}
			}
			if(found)
			{
				getRulesMap().remove(i);
			}
		}
	}

	@Override
	public void processFact(Object fact)
	{
		processFact(fact, getReport());
	}

	@Override
	public void processFact(Object fact, Report report)
	{
		for(Rule rule: getOrderedRules())
		{
			Log.debug("#processFact() executes rule " + rule);
			rule.execute(fact, report);
		}
	}

	/**
	 * package protected
	 * 
	 * @return
	 */
	private Map<Integer, RuleHandler> getRulesMap()
	{
		return rulesMap;
	}

	/**
	 * 
	 * @return rules list in the order they where inserted
	 */
	public List<Rule> getOrderedRules()
	{
		ArrayList<Rule> rules = new ArrayList<Rule>();
		rulesMap.values();
		for(int i = rulesMap.size() - 1; i >= 0; i--)
		{
			rules.add(rulesMap.get(i).getRule());
		}
		return rules;
	}

	@Override
	public Report getReport()
	{
		return report;
	}

	@Override
	public void clearReport()
	{
		report = new Report();

	}

}
