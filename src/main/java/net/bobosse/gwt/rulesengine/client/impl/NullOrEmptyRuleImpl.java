package net.bobosse.gwt.rulesengine.client.impl;

import java.util.List;

import net.bobosse.gwt.rulesengine.client.Report;
import net.bobosse.gwt.rulesengine.client.RuledCommand;

/**
 * simple rule that matches if a fact or its <code>toString()</code> output is
 * null or length == 0.
 * 
 * @author sesa202001
 * 
 */
public class NullOrEmptyRuleImpl extends AbstractRuleImpl {

	public NullOrEmptyRuleImpl(String name, List<RuledCommand> actions, int salience) {
		super(name, actions, salience);
	}

	@Override
	public void execute(Object fact, Report report) {
		setFact(fact);
		setReport(report);
		if (null == fact || fact.toString().length() == 0) {
			executeCommands();
		}
	}
}
