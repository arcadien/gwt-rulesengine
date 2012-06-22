package net.bobosse.gwt.rulesengine.client.impl.rules;

import java.util.ArrayList;
import java.util.List;

import net.bobosse.gwt.rulesengine.client.Report;
import net.bobosse.gwt.rulesengine.client.Rule;
import net.bobosse.gwt.rulesengine.client.RuledCommand;

import com.allen_sauer.gwt.log.client.Log;
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
	private final List<Rule> followingRules = new ArrayList<Rule>();
	private final List<Rule> preceedingRules = new ArrayList<Rule>();

	private int salience;
	private boolean active;
	private Object fact;
	private Report report;

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
		if(null == fact)
		{
			Log.error(this
					+ " have fact set to null. Do you called setFact() in parent rule execute() method?");
			Log.error(this + " Using brand new Object. Don't expect matches!");
			return new Object();
		}
		else
		{
			return fact;
		}
	}

	protected void setFact(Object fact)
	{
		this.fact = fact;
	}

	protected void setReport(Report context)
	{
		this.report = context;
	}

	@Override
	public Report getReport()
	{
		if(null == report)
		{
			Log.error(this
					+ " have no report set. Do you called setReport() in parent rule execute() method?");
			Log.error(this
					+ " Reporting will be done in a temporary report and will be discarded");
			return new Report()
			{
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void add(int index, String element)
				{
				}
			};
		}
		else
		{
			return report;
		}
	}

	public void setFollowingRules(List<Rule> fRules)
	{
		followingRules.clear();
		followingRules.addAll(fRules);
	}

	public void setPreceedingRules(List<Rule> pRules)
	{
		preceedingRules.clear();
		preceedingRules.addAll(pRules);
	}

	@Override
	public List<Rule> getFollowing()
	{
		return followingRules;
	}

	@Override
	public List<Rule> getPreceeding()
	{
		return preceedingRules;
	}
}
