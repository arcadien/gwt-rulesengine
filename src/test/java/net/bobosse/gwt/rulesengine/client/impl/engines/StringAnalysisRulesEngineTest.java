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
package net.bobosse.gwt.rulesengine.client.impl.engines;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.bobosse.gwt.rulesengine.client.Report;
import net.bobosse.gwt.rulesengine.client.Rule;
import net.bobosse.gwt.rulesengine.client.RulesEngine.OrderMode;
import net.bobosse.gwt.rulesengine.client.Session;
import net.bobosse.gwt.rulesengine.client.impl.commands.AbstractRuledCommand;
import net.bobosse.gwt.rulesengine.client.impl.commands.LogFactVerbRuleCommand;
import net.bobosse.gwt.rulesengine.client.impl.rules.RegexRule;

import org.junit.Assert;
import org.junit.Test;

import com.allen_sauer.gwt.log.client.Log;

public class StringAnalysisRulesEngineTest {

	/**
	 * Created with a list of string as suggestions, they put them in the report
	 * each time they match.
	 * 
	 * @author Aurélien Labrosse <aurelien.labrosse@gmail.com>
	 * 
	 */
	static final class SuggestStringCommand extends AbstractRuledCommand {

		private String[] suggestions;
		private String srep;

		/**
		 * 
		 * @param suggestions
		 *            what to push to report when match
		 */
		public SuggestStringCommand(String[] suggestions) {
			this.suggestions = suggestions;
		}

		public String[] getSuggestions() {
			return suggestions;
		}

		@Override
		public void execute() {
			getRule().getReport().add(suggestions);
		}

		@Override
		public String toString() {
			if (srep == null) {
				StringBuffer buff = new StringBuffer();
				buff.append("SuggestStringCommand[");
				int i = 0;
				for (String s : suggestions) {
					buff.append("\"" + s + "\"");
					if (i < suggestions.length - 1) {
						buff.append(",");
					}
					i++;
				}
				buff.append("]");
				srep = buff.toString();
			}
			return srep;
		}
	}

	/**
	 * Scenario 1 : <br />
	 * 
	 */
	@Test
	public void scenario1() {

		// 1. le chien mange la soupe
		String sentence1 = "le chien mange la soupe";

		// 2. la soupe est mangée par le chien
		String sentence2 = "la soupe est mangée par le chien";

		// 3. la bonne soupe est mangée par le chien poilu
		String sentence3 = "la bonne soupe chaude est mangée goulument par ce bon chien poilu";

		// first rule, set salience 1
		RegexRule article = new RegexRule("match articles", "^(le|la)");
		article.addCommand(new LogFactVerbRuleCommand("match"));

		RegexRule ws = new RegexRule("match space", "^\\s*");
		ws.addCommand(new LogFactVerbRuleCommand("match"));

		RegexRule animals = new RegexRule("match animals", "^(chien)");
		animals.addCommand(new LogFactVerbRuleCommand("match"));

		RegexRule verbs = new RegexRule("match verbs", "^(mange)");
		verbs.addCommand(new LogFactVerbRuleCommand("match"));

		RegexRule preterit = new RegexRule("match verbs", "^(est mangée)");
		preterit.addCommand(new LogFactVerbRuleCommand("match"));

		RegexRule soup = new RegexRule("match nouns", "^(soupe)");
		soup.addCommand(new LogFactVerbRuleCommand("match"));

		RegexRule par = new RegexRule("match par", "^(par)");
		par.addCommand(new LogFactVerbRuleCommand("match"));

		// following rules set
		article.setFollowingRules(Arrays
				.asList(new Rule[] { soup, ws, animals }));
		animals.setFollowingRules(Arrays.asList(new Rule[] { verbs, ws }));
		verbs.setFollowingRules(Arrays.asList(new Rule[] { article, ws }));
		preterit.setFollowingRules(Arrays.asList(new Rule[] { par, ws }));
		soup.setFollowingRules(Arrays.asList(new Rule[] { preterit, ws }));
		par.setFollowingRules(Arrays.asList(new Rule[] { article, ws }));
		ws.setFollowingRules(Arrays.asList(new Rule[] { article, animals,
				verbs, preterit, soup, par }));

		// // preceding rules set
		// article.setPreceedingRules(Arrays.asList(new Rule[] { par, ws }));
		// animals.setPreceedingRules(Arrays.asList(new Rule[] { ws, article
		// }));
		// verbs.setPreceedingRules(Arrays.asList(new Rule[] { article, ws }));
		// preterit.setPreceedingRules(Arrays.asList(new Rule[] { soup, ws }));
		// soup.setPreceedingRules(Arrays.asList(new Rule[] { article, ws }));
		// par.setPreceedingRules(Arrays.asList(new Rule[] { preterit, ws }));
		// ws.setPreceedingRules(Arrays.asList(new Rule[] { article, animals,
		// verbs, preterit, soup, par }));

		StringAnalysisRulesEngine engine = new StringAnalysisRulesEngine();

		// only set first network rule
		engine.addRule(article);
		Session session = engine.createStatelessSession(OrderMode.INSERT, null);

		Report report1 = new Report();

		System.out.println("\n----------- report1 --------------");
		session.processFact(sentence1, report1);
		for (Object str : report1) {
			System.out.println(str);
		}

		System.out.println("\n----------- report2 --------------");
		Report report2 = new Report();
		session.processFact(sentence2, report2);
		for (Object str : report2) {
			System.out.println(str);
		}

		System.out.println("\n----------- report3 --------------");
		Report report3 = new Report();
		session.processFact(sentence3, report3);
		for (Object str : report3) {
			System.out.println(str);
		}
	}

	/**
	 * Scenario 2 : <br />
	 * We have :"le chien mange la soupe" when each word is triggered, the rest
	 * word is suggested via a string list.<br />
	 * ex: "le" match, propose "chien", "chien" match propose "mange" and so on. <br />
	 * Algo :
	 * <ul>
	 * <li>Rules used without tree structure (ie not using setFollowing and
	 * setPreceding()</li>
	 * </ul>
	 */
	@Test
	public void scenario2() {

		// 1. le chien mange la soupe

		SuggestStringCommand sstr1 = new SuggestStringCommand(new String[] {
				"chien", "mange", "la", "soupe" });
		SuggestStringCommand sstr2 = new SuggestStringCommand(new String[] {
				"mange", "la", "soupe" });
		SuggestStringCommand sstr3 = new SuggestStringCommand(new String[] {
				"la", "soupe" });
		SuggestStringCommand sstr4 = new SuggestStringCommand(
				new String[] { "soupe" });
		SuggestStringCommand sstr5 = new SuggestStringCommand(
				new String[] { "NIL" });

		SuggestStringCommand sstr0 = new SuggestStringCommand(
				new String[] { "le" });

		RegexRule start = new RegexRule("START", "^$");
		start.addCommand(new LogFactVerbRuleCommand("match"));
		start.addCommand(sstr0);

		// first rule, set salience 1
		RegexRule le = new RegexRule("match 'le'", "^le");
		le.addCommand(new LogFactVerbRuleCommand("match"));
		le.addCommand(sstr1);

		RegexRule ws = new RegexRule("match space", "^\\s");
		ws.addCommand(new LogFactVerbRuleCommand("match"));

		RegexRule animals = new RegexRule("match 'chien'", "^chien");
		animals.addCommand(new LogFactVerbRuleCommand("match"));
		animals.addCommand(sstr2);

		RegexRule verbs = new RegexRule("match 'mange'", "^mange");
		verbs.addCommand(new LogFactVerbRuleCommand("match"));
		verbs.addCommand(sstr3);

		RegexRule la = new RegexRule("match 'la'", "^la");
		la.addCommand(new LogFactVerbRuleCommand("match"));
		la.addCommand(sstr4);

		RegexRule soup = new RegexRule("match 'soupe'", "^soupe");
		soup.addCommand(new LogFactVerbRuleCommand("match"));
		soup.addCommand(sstr5);

		StringAnalysisRulesEngine engine = new StringAnalysisRulesEngine();

		// engine.addRule(start);
		engine.addRule(le);
		engine.addRule(ws);
		engine.addRule(animals);
		engine.addRule(verbs);
		engine.addRule(la);
		engine.addRule(soup);

		Session session = engine.createStatelessSession(OrderMode.INSERT, null);

		Report report1 = new Report();

		session.processFact("", report1);
		List<String[]> suggest = extractSuggestionsFromReport(report1, "START");

		session.processFact("le", report1);
		suggest = extractSuggestionsFromReport(report1, "le");

		session.processFact("le chien", report1);
		suggest = extractSuggestionsFromReport(report1, "le chien");

		// session.processFact("le chien mange", report1);
		// suggest = extractSuggestionsFromReport(report1, "le chien mange");
		//
		// session.processFact("le chien mange la", report1);
		// suggest = extractSuggestionsFromReport(report1, "le chien mange la");
		//
		// session.processFact("le chien mange la soupe soupe", report1);
		// suggest = extractSuggestionsFromReport(report1,
		// "le chien mange la soupe");

		// report must here contains a list of String[], the older one beeing
		// the older one:
		// 1. (matched 'first rule?') sstr0{"??"}
		// 2. (matched space, ws). sstr1{"chien", "mange","la","soupe"}
		// 3. (matched space, ws). sstr2{"mange","la","soupe"}
		// 4. (matched space, ws). sstr3{"la","soupe"}
		// 5. (matched space, ws). sstr4{"soupe"}
		// 6. (reached end, ws). sstr4{"NIL"}

		// ensure traces before any failure
		System.out.flush();

		Assert.assertEquals("See test case : we need 5 entries here.", 5,
				suggest.size());

		int counter = 1;
		String[] expected = null;
		for (String[] suggestSet : suggest) {
			if (counter == 1)
				expected = sstr0.getSuggestions();
			if (counter == 2)
				expected = sstr1.getSuggestions();
			if (counter == 3)
				expected = sstr2.getSuggestions();
			if (counter == 4)
				expected = sstr3.getSuggestions();
			if (counter == 5)
				expected = sstr4.getSuggestions();
			if (counter == 6)
				expected = sstr5.getSuggestions();

			Assert.assertArrayEquals("Bad suggestion, sir.", expected,
					suggestSet);
		}
	}

	private List<String[]> extractSuggestionsFromReport(Report report1,
			String title) {

		Log.debug("----------- " + title + " ------------");

		List<String[]> suggest = new ArrayList<String[]>();

		for (Object entries : report1) {
			if (entries instanceof String[]) {
				suggest.add((String[]) entries);
			} else {
				Log.debug(entries.toString());
			}
		}
		int suggestSetIndex = 0;

		for (String[] suggestSet : suggest) {

			System.out.println("--- SuggestSet : " + suggestSetIndex);
			for (int i = 0; i < suggestSet.length; i++) {
				Log.debug("suggestion " + i + " " + suggestSet[i]);

			}
			suggestSetIndex++;
		}
		return suggest;
	}
}
