package com.tvlk.payment.ruleengine.groovy;

import com.tvlk.payment.ruleengine.Constants;
import com.tvlk.payment.ruleengine.model.rules.RuleResult;
import lombok.Data;
import org.jeasy.rules.api.Action;
import org.jeasy.rules.api.Condition;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.core.BasicRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@Data
public class GroovyRule extends BasicRule {

  private Condition condition = Condition.FALSE;
  private List<Action> actions = new ArrayList<>();

  /**
   * Create a new Groovy rule.
   */
  public GroovyRule() {
    super(Rule.DEFAULT_NAME, Rule.DEFAULT_DESCRIPTION, Rule.DEFAULT_PRIORITY);
  }

  /**
   * Set rule name.
   *
   * @param name of the rule
   * @return this rule
   */
  public GroovyRule name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Set rule description.
   *
   * @param description of the rule
   * @return this rule
   */
  public GroovyRule description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Set rule priority.
   *
   * @param priority of the rule
   * @return this rule
   */
  public GroovyRule priority(int priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Specify the rule's condition as Groovy expression.
   *
   * @param condition of the rule
   * @return this rule
   */
  public GroovyRule when(String condition) {
    this.condition = new GroovyCondition(condition);
    return this;
  }

  /**
   * Add an action specified as an Groovy expression to the rule.
   *
   * @param action to add to the rule
   * @return this rule
   */
  public GroovyRule then(String action) {
    this.actions.add(new GroovyAction(action));
    return this;
  }

  @Override
  public boolean evaluate(Facts facts) {
    boolean result = condition.evaluate(facts);
    RuleResult ruleResult = facts.get(Constants.FACTS_RULE_RESULT_KEY);
    if (result) {
      if (Objects.nonNull(ruleResult)) {
        ruleResult.getSuccessRuleSet().add(this);
      }
    } else {
      if (Objects.nonNull(ruleResult)) {
        ruleResult.getFailRuleSet().add(this);
      }
    }
    return result;
  }

  @Override
  public void execute(Facts facts) throws Exception {
    for (Action action : actions) {
      action.execute(facts);
    }
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", GroovyRule.class.getSimpleName() + "[", "]")
        .add("condition=" + condition)
        .add("actions=" + actions)
        .add("name='" + name + "'")
        .add("description='" + description + "'")
        .add("priority=" + priority)
        .toString();
  }
}
