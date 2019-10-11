package com.tvlk.payment.ruleengine.core;

import com.tvlk.payment.ruleengine.Constants;
import com.tvlk.payment.ruleengine.model.rules.UnitRuleGroupWithResult;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.RuleListener;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngineListener;
import org.jeasy.rules.core.RulesEngineParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public final class TvlkDefaultRulesEngine extends AbstractRuleEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(TvlkDefaultRulesEngine.class);

    /**
     * Create a new {@link TvlkDefaultRulesEngine} with default parameters.
     */
    public TvlkDefaultRulesEngine() {
        super();
    }

    /**
     * Create a new {@link TvlkDefaultRulesEngine}.
     *
     * @param parameters of the engine
     */
    public TvlkDefaultRulesEngine(final RulesEngineParameters parameters) {
        super(parameters);
    }

    @Override
    public void fire(Rules rules, Facts facts) {
        triggerListenersBeforeRules(rules, facts);
        doFire(rules, facts);
        triggerListenersAfterRules(rules, facts);
    }

    void doFire(Rules rules, Facts facts) {
        for (Rule rule : rules) {
            final String name = rule.getName();
            final int priority = rule.getPriority();
            if (priority > parameters.getPriorityThreshold()) {
                LOGGER.debug("Rule priority threshold ({}) exceeded at rule '{}' with priority={}, next rules will be skipped",
                        parameters.getPriorityThreshold(), name, priority);
                break;
            }
            if (!shouldBeEvaluated(rule, facts)) {
                LOGGER.debug("Rule '{}' has been skipped before being evaluated",
                    name);
                continue;
            }
            if (rule.evaluate(facts)) {
                triggerListenersAfterEvaluate(rule, facts, true);
                try {
                    triggerListenersBeforeExecute(rule, facts);
                    rule.execute(facts);
                    triggerListenersOnSuccess(rule, facts);
                    if (parameters.isSkipOnFirstAppliedRule()) {
                        LOGGER.debug("Next rules will be skipped since parameter skipOnFirstAppliedRule is set");
                        break;
                    }
                } catch (Exception exception) {
                    triggerListenersOnFailure(rule, exception, facts);
                    if (parameters.isSkipOnFirstFailedRule()) {
                        LOGGER.debug("Next rules will be skipped since parameter skipOnFirstFailedRule is set");
                        break;
                    }
                }
            } else {
                triggerListenersAfterEvaluate(rule, facts, false);
                if (parameters.isSkipOnFirstNonTriggeredRule()) {
                    LOGGER.debug("Next rules will be skipped since parameter skipOnFirstNonTriggeredRule is set");
                    break;
                }
            }
        }
    }

    @Override
    public Map<Rule, Boolean> check(Rules rules, Facts facts) {
        triggerListenersBeforeRules(rules, facts);
        Map<Rule, Boolean> result = doCheck(rules, facts);
        triggerListenersAfterRules(rules, facts);
        return result;
    }

    private Map<Rule, Boolean> doCheck(Rules rules, Facts facts) {
        LOGGER.debug("Checking rules");
        Map<Rule, Boolean> result = new HashMap<>();
        for (Rule rule : rules) {
            if (shouldBeEvaluated(rule, facts)) {
                result.put(rule, rule.evaluate(facts));
            }
        }
        return result;
    }

    private void triggerListenersOnFailure(final Rule rule, final Exception exception, Facts facts) {
        for (RuleListener ruleListener : ruleListeners) {
            ruleListener.onFailure(rule, facts, exception);
        }
    }

    private void triggerListenersOnSuccess(final Rule rule, Facts facts) {
        for (RuleListener ruleListener : ruleListeners) {
            ruleListener.onSuccess(rule, facts);
        }
    }

    private void triggerListenersBeforeExecute(final Rule rule, Facts facts) {
        for (RuleListener ruleListener : ruleListeners) {
            ruleListener.beforeExecute(rule, facts);
        }
    }

    private boolean triggerListenersBeforeEvaluate(Rule rule, Facts facts) {
        if (rule instanceof UnitRuleGroupWithResult) {
            facts.put(Constants.FACTS_RULE_RESULT_KEY, ((UnitRuleGroupWithResult) rule).getRuleResult());
        }

        for (RuleListener ruleListener : ruleListeners) {
            if (!ruleListener.beforeEvaluate(rule, facts)) {
                return false;
            }
        }
        return true;
    }

    private void triggerListenersAfterEvaluate(Rule rule, Facts facts, boolean evaluationResult) {
        for (RuleListener ruleListener : ruleListeners) {
            ruleListener.afterEvaluate(rule, facts, evaluationResult);
        }
    }

    private void triggerListenersBeforeRules(Rules rule, Facts facts) {
        for (RulesEngineListener rulesEngineListener : rulesEngineListeners) {
            rulesEngineListener.beforeEvaluate(rule, facts);
        }
    }

    private void triggerListenersAfterRules(Rules rule, Facts facts) {
        for (RulesEngineListener rulesEngineListener : rulesEngineListeners) {
            rulesEngineListener.afterExecute(rule, facts);
        }
    }

    private boolean shouldBeEvaluated(Rule rule, Facts facts) {
        return triggerListenersBeforeEvaluate(rule, facts);
    }

}
