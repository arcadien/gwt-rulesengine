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
package net.bobosse.gwt.rulesengine.client.impl.engines;

import net.bobosse.gwt.rulesengine.client.Report;
import net.bobosse.gwt.rulesengine.client.Rule;
import net.bobosse.gwt.rulesengine.client.RulesEngine;

import com.allen_sauer.gwt.log.client.Log;

/**
 * This basic {@link RulesEngine} implementation first calls the first
 * {@link Rule}. If {@link Rule} matches something in the <i>fact</i>, then this
 * match is "consumed" and a Match entry is recorded in the report.<br />
 * <code>execute()</code> then returns a list of potential {@link Rule} to
 * execute. This process continue until all <i>fact</i> is consumed or not
 * following {@link Rule} is specified in report.
 * 
 * 
 * @author Aurélien Labrosse <aurelien.labrosse@gmail.com>
 * 
 */
public class StringAnalysisRulesEngine extends AbstractRulesEngine {


	@Override
	public void processFact(Object fact, Report report, OrderMode mode) {
		for (Rule rule : getRules(mode)) {
			Log.debug("#processFact() executes rule " + rule);
			rule.execute(fact, report);
			throw new IllegalStateException("Not implemented yet");
		}
	}

}
