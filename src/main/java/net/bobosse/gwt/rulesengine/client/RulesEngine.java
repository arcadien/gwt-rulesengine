/**
 * Copyright  2012 Aurélien Labrosse <aurelien.labrosse@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.bobosse.gwt.rulesengine.client;

/**
 * A simple rules engine that backs a {@link Rule} structure and try to match
 * them against a <code>fact</code>. If a {@link Rule} matches, then all its
 * {@link RuledCommand} are fired. Else, nothing is done and the next
 * {@link Rule} is processed until no rule remains.<br />
 * 
 * @author Aurélien Labrosse <aurelien.labrosse@gmail.com>
 */
public interface RulesEngine {

	/**
	 * How the result of <code>getRules(OrderMode mode)</code> will be sorted.<br />
	 * <ul>
	 * <li>OrderMode.SALIENCE : sorted by salience value. remember default is
	 * -1. If two rules has same salience value, the older one (early added)
	 * will appear first.</li>
	 * <li>OrderMode.INSERT : sorted in same order they were added</li>
	 * </ul>
	 * 
	 */
	public enum OrderMode {
		SALIENCE, INSERT
	}

	/**
	 * Add a rule to a {@link RulesEngine}
	 * 
	 * @param rule to add to a {@link RulesEngine}
	 * @return a RuleHandler
	 */
	RuleHandler addRule(Rule rule);

	/**
	 * Create a session that holds no hard references to <i>facts</i> or {@link Rule},
	 * and don't need to be disposed.
	 * 
	 * @param mode
	 * @param report
	 * @return
	 */
	public Session createStatelessSession(final OrderMode mode,
			final Report report);
}
