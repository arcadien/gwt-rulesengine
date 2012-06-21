package net.bobosse.gwt.rulesengine.client.impl.rules;

import net.bobosse.gwt.rulesengine.client.Report;

/**
 * simple rule that matches if a fact or its <code>toString()</code> output is
 * null or length == 0.
 * 
 * @author sesa202001
 * 
 */
public class NullOrEmptyRule extends AbstractRule
{

	public NullOrEmptyRule (String name, int salience)
	{
		super(name, salience);
	}

	@Override
	public void execute(Object fact, Report report)
	{
		setFact(fact);
		setReport(report);
		if(null == fact || fact.toString().length() == 0)
		{
			executeCommands();
		}
	}
}