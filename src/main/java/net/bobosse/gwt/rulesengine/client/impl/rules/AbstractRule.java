package net.bobosse.gwt.rulesengine.client.impl.rules;

import java.util.ArrayList;
import java.util.List;

import net.bobosse.gwt.rulesengine.client.Report;
import net.bobosse.gwt.rulesengine.client.Rule;
import net.bobosse.gwt.rulesengine.client.RuledCommand;

import com.google.gwt.user.client.Command;

/**
 * {@link AbstractRule} implements action, active status, name and salience
 * manipulation. The rest is for subclasses, ie <code>execute()</code> interface
 * method.
 * 
 * @author sesa202001
 * 
 */
public abstract class AbstractRule implements Rule
{

	private String name;
	private final List<RuledCommand> actions = new ArrayList<RuledCommand>();
	private int salience;
	private boolean active;
	private Object fact;
	private Report context;

	public AbstractRule (String name, int salience)
	{
		this.name = name;
		this.salience = salience;
	}

	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * call each {@link RuledCommand} <code>execute()</code> method
	 */
	@Override
	public final void executeCommands()
	{
		for(Command command: getCommands())
		{
			command.execute();
		}
	}

	@Override
	public final boolean addCommand(RuledCommand action)
	{
		action.setRule(this);
		return this.actions.add(action);
	}

	@Override
	public final boolean removeCommand(RuledCommand action)
	{
		return this.actions.remove(action);
	}

	@Override
	public final List<RuledCommand> getCommands()
	{
		return this.actions;
	}

	@Override
	public int getSalience()
	{
		return salience;
	}

	@Override
	public boolean isActive()
	{
		return active;
	}

	@Override
	public boolean activate()
	{
		boolean oldState = active;
		active = true;
		return oldState;
	}

	@Override
	public boolean passivate()
	{
		boolean oldState = active;
		active = false;
		return oldState;
	}

	@Override
	public void clearCommands()
	{
		actions.clear();
	}

	@Override
	public Object getFact()
	{
		return fact;
	}

	protected void setFact(Object fact)
	{
		this.fact = fact;
	}

	protected void setReport(Report context)
	{
		this.context = context;
	}

	@Override
	public Report getReport()
	{
		return context;
	}

}
