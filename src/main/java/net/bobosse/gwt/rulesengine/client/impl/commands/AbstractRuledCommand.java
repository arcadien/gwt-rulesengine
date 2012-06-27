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

import net.bobosse.gwt.rulesengine.client.Rule;
import net.bobosse.gwt.rulesengine.client.RuledCommand;

import com.allen_sauer.gwt.log.client.Log;

/**
 * Base {@link RuledCommand} implementation that handles <code>this.rule</code>
 * management only.
 * 
 * @author Aurélien Labrosse <aurelien.labrosse@gmail.com>
 */
public abstract class AbstractRuledCommand implements RuledCommand {
	private Rule rule;

	@Override
	public void setRule(Rule rule) {
		Log.debug(this + " linked to rule '" + rule + "'");
		this.rule = rule;
	}

	@Override
	public Rule getRule() {
		return rule;
	}
}
