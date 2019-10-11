package com.tvlk.payment.ruleengine.listener;

import com.tvlk.payment.ruleengine.Constants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.RuleListener;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Data
public class TvlkDefaultRuleListener implements RuleListener {

    @Override
    public boolean beforeEvaluate(final Rule rule, final Facts facts) {
        return true;
    }

    @Override
    public void afterEvaluate(final Rule rule, final Facts facts, final boolean evaluationResult) {
        final String ruleName = rule.getName();
        if (evaluationResult) {
            Set<Rule> successRules = facts.get(Constants.FACTS_SUCCESS_CONDITION_RULE_SET_KEY);
            // TODO: if rule is matched, we can perform action to get specific config details. E.g: BANK_TRANSFER, CC, ATM BNI
            Map<String, Set<Rule>> successConfigRules = facts.get(Constants.FACTS_SUCCESS_RULE_MAP_KEY);
            if (Objects.nonNull(successConfigRules)) {
                successConfigRules.put(ruleName, successRules);
            }
        } else {
            Set<Rule> failRules = facts.get(Constants.FACTS_FAIL_CONDITION_RULE_SET_KEY);
            Map<String, Set<Rule>> failConfigRules = facts.get(Constants.FACTS_FAIL_RULE_MAP_KEY);
            if (Objects.nonNull(failConfigRules)) {
                failConfigRules.put(ruleName, failRules);
            }
        }
    }

    @Override
    public void beforeExecute(final Rule rule, final Facts facts) {
        // To be empty
    }

    @Override
    public void onSuccess(final Rule rule, final Facts facts) {
        // To be empty
    }

    @Override
    public void onFailure(final Rule rule, final Facts facts, final Exception exception) {
        // To be empty
    }
}
