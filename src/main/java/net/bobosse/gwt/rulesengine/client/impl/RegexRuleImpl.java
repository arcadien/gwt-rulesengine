package net.bobosse.gwt.rulesengine.client.impl;

import java.util.ArrayList;
import java.util.List;

import net.bobosse.gwt.rulesengine.client.Report;
import net.bobosse.gwt.rulesengine.client.RuledCommand;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

/**
 * {@link RegexRuleImpl} is based on regex pattern. If pattern matches with the
 * processed fact (after a toString()), then its actions will be fired.
 * 
 * @author sesa202001
 * 
 */
public class RegexRuleImpl extends AbstractRuleImpl {

	private static final Logger logger = Logger.getLogger(RegexRuleImpl.class);

	private String pattern;

	public RegexRuleImpl(String name, String pattern, List<RuledCommand> actions,
			int salience) {
		super(name, actions);
		this.pattern = pattern;
	}

	public RegexRuleImpl(String name, String pattern, List<RuledCommand> actions) {
		this(name, pattern, actions, -1);
	}

	public RegexRuleImpl(String name, String pattern) {
		this(name, pattern, null, -1);
	}

	@Override
	public void execute(Object fact, Report context) {
		ArrayList<String> matches = new ArrayList<String>();
		matches = getMatches(fact.toString(), pattern);
		if (matches.size() > 0) {
			logger.log(Level.DEBUG, "'" + this + "' matched '" + fact + "'");
			try {
				setReport(context);
				setFact(fact);
				executeCommands();
			} catch (Exception e) {
			}
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
