package com.tvlk.payment.ruleengine;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.RuleListener;

public class PaymentRuleListener implements RuleListener {
  @Override
  public boolean beforeEvaluate(Rule rule, Facts facts) {
    return false;  // TODO impl
  }

  @Override
  public void afterEvaluate(Rule rule, Facts facts, boolean evaluationResult) {
    // TODO impl
  }

  @Override
  public void beforeExecute(Rule rule, Facts facts) {
    // TODO impl
  }

  @Override
  public void onSuccess(Rule rule, Facts facts) {
    // TODO impl
  }

  @Override
  public void onFailure(Rule rule, Facts facts, Exception exception) {
    // TODO impl
  }
}
