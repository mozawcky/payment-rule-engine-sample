package com.tvlk.payment.ruleengine.listener;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.RuleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class TvlkDefaultRuleListener implements RuleListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TvlkDefaultRuleListener.class);

    @Override
    public boolean beforeEvaluate(final Rule rule, final Facts facts) {
        return true;
    }

    @Override
    public void afterEvaluate(final Rule rule, final Facts facts, final boolean evaluationResult) {
        final String ruleName = rule.getName();
        if (evaluationResult) {
            LOGGER.debug("Rule '{}' triggered", ruleName);
            Set<Rule> successRules = facts.get("successRules");
            Map<String, Set<Rule>> successConfigRules = facts.get("successConfigRules");
            if (Objects.nonNull(successConfigRules)) {
                successConfigRules.put(rule.getName(), successRules);
            }
        } else {
            LOGGER.debug("Rule '{}' has been evaluated to false, it has not been executed", ruleName);
            Set<Rule> failRules = facts.get("failRules");
            Map<String, Set<Rule>> failConfigRules = facts.get("failConfigRules");
            if (Objects.nonNull(failConfigRules)) {
                failConfigRules.put(rule.getName(), failRules);
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
