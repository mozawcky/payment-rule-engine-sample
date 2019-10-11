/**
 * The MIT License
 * <p>
 * Copyright (c) 2019, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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

class TvlkDefaultRuleListener implements RuleListener {

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
    final RuleResult ruleResult = facts.get(Constants.FACTS_RULE_RESULT_KEY);
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
