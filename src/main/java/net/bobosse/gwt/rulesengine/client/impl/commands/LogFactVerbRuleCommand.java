package net.bobosse.gwt.rulesengine.client.impl.commands;

import net.bobosse.gwt.rulesengine.client.Report;

/**
 * Simple command that insert a string in the {@link Report} as of :<br />
 * 
 * <code> 'toto' plays ball</code><br />
 * Where toto is a <code>toString()</code> result of processed fact, and "ball",
 * the triggered rule name. "plays" is a verb wich is set trough constructor.
 * 
 * 
 * @author Aurélien Labrosse <aurelien.labrosse@gmail.com>
 * 
 */
public class LogFactVerbRuleCommand extends AbstractRuledCommand
{

	String verb;

	/**
	 * 
	 * @param verb
	 *            the word between fact and parent rule's name.<br />
	 *            ex. : 'toto' plays ball<br />
	 *            Here, verb is "plays".
	 * 
	 */
	public LogFactVerbRuleCommand (String verb)
	{
		this.verb = verb;
	}

	@Override
	public void execute()
	{
		getRule().getReport().add(
				"'" + getRule().getFact() + "' " + verb + " '"
						+ getRule().getName() + "'");

	}
}
