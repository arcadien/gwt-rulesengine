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
 * {@link Session} is the interface on a {@link RulesEngine}. It allow
 * <i>fact</i> processing, using session {@link Report} or dedicated
 * {@link Report} for a special fact.
 * 
 * 
 * 
 * @author Aurélien Labrosse <aurelien.labrosse@gmail.com>
 * 
 */
public interface Session {

	
	/**
	 * Add a <i>fact</i> to this {@link Session}, to be processed
	 * along other <i>facts</i>.
	 * 
	 * 
	 * @param fact to be added
	 */
	public boolean addFact(Object fact);
	
	/**
	 * Retract a <i>fact</i> from this {@link Session}
	 * 
	 * @param fact to be retracted
	 */
	public boolean retractFact(Object fact);
	
	/**
	 * free all needed resources to allow GC to cleanup
	 */
	public void dispose();
	
	/**
	 * 
	 * Execute all rules against all registered facts.
	 * 
	 */
	public void fireAllRules();
	
	/**
	 * this one use a dedicated {@link Report}.
	 * 
	 * @param fact
	 * @param report
	 */
	public void processFact(Object fact, Report report);

	/**
	 * this one uses the {@link Report} passed at {@link Session}
	 * creation.
	 * 
	 * @param fact
	 */
	public void processFact(Object fact);

}
