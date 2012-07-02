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
package net.bobosse.gwt.rulesengine.client.impl.rules;

import net.bobosse.gwt.rulesengine.client.Report;

/**
 * simple rule that matches if a fact or its <code>toString()</code> output is
 * null or length == 0.
 * 
 * @author Aurélien Labrosse <aurelien.labrosse@gmail.com>
 * 
 */
public class NullOrEmptyRule extends AbstractRule {

	public NullOrEmptyRule(String name, int salience) {
		super(name, salience);
	}

	@Override
	public boolean execute(Object fact, Report report) {
		setFact(fact);
		setReport(report);
		boolean status = null == fact || fact.toString().length() == 0;
		if (null == fact || fact.toString().length() == 0) {
			executeCommands();
		}
		return status;
	}
}
