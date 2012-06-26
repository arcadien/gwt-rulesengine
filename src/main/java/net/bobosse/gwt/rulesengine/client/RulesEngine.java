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
package net.bobosse.gwt.rulesengine.client;

/**
 * A simple rules engine that backs a {@link Rule} structure and try to match
 * them against a <code>fact</code>. If a {@link Rule} matches, then all its
 * {@link RuledCommand} are fired. Else, nothing is done and the next
 * {@link Rule} is processed until no rule remains.<br />
 * 
 * @author sesa202001
 * 
 */
public interface RulesEngine
{

	RuleHandler addRule(Rule rule);

	/**
	 * process a fact trough rules
	 * 
	 * @param fact
	 */
	void processFact(Object fact);

	/**
	 * process a fact trough rules and specify a specific {@link Report}
	 * 
	 * @param fact
	 * @param report
	 */
	void processFact(Object fact, Report report);

	Report getReport();

	void clearReport();

}
