package com.tvlk.payment.ruleengine.core;

import com.tvlk.payment.ruleengine.Constants;
import com.tvlk.payment.ruleengine.model.rules.RuleResult;
import com.tvlk.payment.ruleengine.model.rules.UnitRuleGroupWithResult;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.RuleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class TvlkDefaultRuleListener implements RuleListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(TvlkDefaultRuleListener.class);

  @Override
  public boolean beforeEvaluate(final Rule rule, final Facts facts) {
    if (rule instanceof UnitRuleGroupWithResult) {
      UnitRuleGroupWithResult ruleGroupWithResult = (UnitRuleGroupWithResult) rule;
      ruleGroupWithResult.getRuleResult().setFailRuleSet(new HashSet<>());
      ruleGroupWithResult.getRuleResult().setSuccessRuleSet(new HashSet<>());
    }
    return true;
  }

  @Override
  public void afterEvaluate(final Rule rule, final Facts facts, final boolean evaluationResult) {
    final String ruleName = rule.getName();
    final Set<RuleResult> ruleResultSet = facts.get(Constants.FACTS_RULE_RESULT_SET_KEY);
    final RuleResult ruleResult = facts.get(Constants.FACTS_RULE_RESULT_KEY);
    if (Objects.nonNull(ruleResultSet)) {
      ruleResultSet.add(ruleResult);
    }

    if (evaluationResult) {
      LOGGER.debug("Rule '{}' triggered", ruleName);
      Map<String, RuleResult> successConfigRules = facts.get(Constants.FACTS_SUCCESS_RULE_MAP_KEY);
      if (Objects.nonNull(successConfigRules)) {
        successConfigRules.put(ruleName, ruleResult);
      }
    } else {
      LOGGER.debug("Rule '{}' has been evaluated to false, it has not been executed", ruleName);
      Map<String, RuleResult> failConfigRules = facts.get(Constants.FACTS_FAIL_RULE_MAP_KEY);
      if (Objects.nonNull(failConfigRules)) {
        failConfigRules.put(ruleName, ruleResult);
      }
    }
  }

  @Override
  public void beforeExecute(final Rule rule, final Facts facts) {

  }

  @Override
  public void onSuccess(final Rule rule, final Facts facts) {
    LOGGER.debug("Rule '{}' performed successfully", rule.getName());
  }

  @Override
  public void onFailure(final Rule rule, final Facts facts, final Exception exception) {
    LOGGER.error("Rule '" + rule.getName() + "' performed with error", exception);
  }
}
