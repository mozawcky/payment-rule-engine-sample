package com.tvlk.payment.ruleengine.core;

import org.jeasy.rules.api.RuleListener;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.api.RulesEngineListener;
import org.jeasy.rules.core.RulesEngineParameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractRuleEngine implements RulesEngine {

    RulesEngineParameters parameters;
    List<RuleListener> ruleListeners;
    List<RulesEngineListener> rulesEngineListeners;

    AbstractRuleEngine() {
        this(new RulesEngineParameters());
    }

    AbstractRuleEngine(final RulesEngineParameters parameters) {
        this.parameters = parameters;
        this.ruleListeners = new ArrayList<>();
        this.ruleListeners.add(new TvlkDefaultRuleListener());
        this.rulesEngineListeners = new ArrayList<>();
        this.rulesEngineListeners.add(new TvlkDefaultRulesEngineListener(parameters));
    }

    @Override
    public RulesEngineParameters getParameters() {
        return new RulesEngineParameters(
                parameters.isSkipOnFirstAppliedRule(),
                parameters.isSkipOnFirstFailedRule(),
                parameters.isSkipOnFirstNonTriggeredRule(),
                parameters.getPriorityThreshold()
        );
    }

    @Override
    public List<RuleListener> getRuleListeners() {
        return Collections.unmodifiableList(ruleListeners);
    }

    @Override
    public List<RulesEngineListener> getRulesEngineListeners() {
        return Collections.unmodifiableList(rulesEngineListeners);
    }

    public void registerRuleListener(RuleListener ruleListener) {
        ruleListeners.add(ruleListener);
    }

    public void registerRuleListeners(List<RuleListener> ruleListeners) {
        this.ruleListeners.addAll(ruleListeners);
    }

    public void registerRulesEngineListener(RulesEngineListener rulesEngineListener) {
        rulesEngineListeners.add(rulesEngineListener);
    }

    public void registerRulesEngineListeners(List<RulesEngineListener> rulesEngineListeners) {
        this.rulesEngineListeners.addAll(rulesEngineListeners);
    }
}
