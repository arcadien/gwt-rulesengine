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

import java.util.ArrayList;

import net.bobosse.gwt.rulesengine.client.Report;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

/**
 * {@link RegexRule} is based on regex pattern. If pattern matches with the
 * processed fact (after a toString()), then its actions will be fired. <br />
 * 
 * Here is an online JS regex tester :
 * http://www.regular-expressions.info/javascriptexample.html
 * 
 * @author Aurélien Labrosse <aurelien.labrosse@gmail.com>
 * 
 */
public class RegexRule extends AbstractRule {
	
	/**
	 * this rule trigger regex regex
	 */
	private String pattern;

	/**
	 * Full constructor
	 * 
	 * @param name
	 *            rule's name
	 * @param pattern
	 *            rule's match pattern
	 * @param actions
	 *            to execute() when rule matches
	 * @param salience
	 *            priority level ( -100 &lt; salience &gt; 100)
	 * 
	 */
	public RegexRule(String name, String pattern, int salience) {
		super(name, salience);
		this.pattern = pattern;
	}

	/**
	 * Constructor with only name and pattern : no default rule and salience set
	 * to -1.
	 * 
	 * @param name
	 *            rule's name
	 * @param pattern
	 *            rule's match pattern
	 */
	public RegexRule(String name, String pattern) {
		this(name, pattern, -1);
	}

	/**
	 * child class can't acceed <code>pattern</code>.
	 * 
	 * @param string
	 * @return list of string's matches, according to rule's pattern.
	 */
	protected ArrayList<String> getMatches(String string) {
		return getMatches(string, pattern);
	}

	@Override
	public void execute(Object fact, Report report) {
		ArrayList<String> matches = new ArrayList<String>();
		matches = getMatches(fact.toString(), pattern);
		if (matches.size() > 0) {
			Log.debug("'" + this + "' matched '" + fact + "'");

			setReport(report);
			setFact(fact);
			
			executeCommands();

		}
	}

	@Override
	public String toString() {
		return getName();
	}

	private ArrayList<String> getMatches(String input, String pattern) {
		ArrayList<String> matches = new ArrayList<String>();
		RegExp regExp = RegExp.compile(pattern, "g");
		for (MatchResult matcher = regExp.exec(input); matcher != null; matcher = regExp
				.exec(input)) {
			matches.add(matcher.getGroup(0));
		}
		return matches;
	}
}
