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

import java.util.Arrays;

import net.bobosse.gwt.rulesengine.client.Report;
import net.bobosse.gwt.rulesengine.client.Rule;
import net.bobosse.gwt.rulesengine.client.RulesEngine.OrderMode;
import net.bobosse.gwt.rulesengine.client.Session;
import net.bobosse.gwt.rulesengine.client.impl.commands.LogFactVerbRuleCommand;
import net.bobosse.gwt.rulesengine.client.impl.rules.RegexRule;

import org.junit.Test;

public class StringAnalysisRulesEngineTest {

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

//		// preceding rules set
//		article.setPreceedingRules(Arrays.asList(new Rule[] { par, ws }));
//		animals.setPreceedingRules(Arrays.asList(new Rule[] { ws, article }));
//		verbs.setPreceedingRules(Arrays.asList(new Rule[] { article, ws }));
//		preterit.setPreceedingRules(Arrays.asList(new Rule[] { soup, ws }));
//		soup.setPreceedingRules(Arrays.asList(new Rule[] { article, ws }));
//		par.setPreceedingRules(Arrays.asList(new Rule[] { preterit, ws }));
//		ws.setPreceedingRules(Arrays.asList(new Rule[] { article, animals,
//				verbs, preterit, soup, par }));

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

}
