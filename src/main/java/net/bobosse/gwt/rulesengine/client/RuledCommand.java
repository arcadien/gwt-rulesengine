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

import com.google.gwt.user.client.Command;

public interface RuledCommand extends Command
{
	/**
	 * Set rule that may trigger this action
	 * 
	 * @param rule
	 *            that may trigger this action (back reference. @fixme needed?)
	 */
	void setRule(Rule rule);

	/**
	 * @return {@link Rule} that may trigger this action
	 * 
	 */
	Rule getRule();
}
