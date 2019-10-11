package com.tvlk.payment.ruleengine.core;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngineListener;
import org.jeasy.rules.core.RulesEngineParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

class TvlkDefaultRulesEngineListener implements RulesEngineListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TvlkDefaultRulesEngineListener.class);

    private RulesEngineParameters parameters;

    TvlkDefaultRulesEngineListener(RulesEngineParameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public void beforeEvaluate(Rules rules, Facts facts) {
        if (!rules.isEmpty()) {
            logEngineParameters();
            log(rules);
            log(facts);
            LOGGER.debug("Rules evaluation started");
        } else {
            LOGGER.warn("No rules registered! Nothing to apply");
        }
    }

    @Override
    public void afterExecute(Rules rules, Facts facts) {

    }

    private void logEngineParameters() {
        LOGGER.debug(parameters.toString());
    }

    private void log(Rules rules) {
        LOGGER.debug("Registered rules:");
        for (Rule rule : rules) {
            LOGGER.debug("Rule { name = '{}', description = '{}', priority = '{}'}",
                    rule.getName(), rule.getDescription(), rule.getPriority());
        }
    }

    private void log(Facts facts) {
        LOGGER.debug("Known facts:");
        for (Map.Entry<String, Object> fact : facts) {
            LOGGER.debug("Fact { {} : {} }",
                    fact.getKey(), String.valueOf(fact.getValue()));
        }
    }
}
