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
package net.bobosse.gwt.rulesengine.client.impl.commands;

import net.bobosse.gwt.rulesengine.client.Report;

/**
 * Simple command that insert a string in the {@link Report} as of :<br />
 * 
 * <code> 'toto' plays ball</code><br />
 * Where toto is a <code>toString()</code> result of processed <i>fact</i>, and "ball",
 * the triggered rule name. "plays" is a verb which is set as string value trough constructor.
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
