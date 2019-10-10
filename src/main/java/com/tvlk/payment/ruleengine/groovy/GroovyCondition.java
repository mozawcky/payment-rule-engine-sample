package com.tvlk.payment.ruleengine.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.MissingPropertyException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Condition;
import org.jeasy.rules.api.Facts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@Data
@AllArgsConstructor
public class GroovyCondition implements Condition {

  private static final Logger LOGGER = LoggerFactory.getLogger(GroovyCondition.class);

  private String expression;

  @Override
  public boolean evaluate(Facts facts) {
    boolean result = false;
    try {
      Binding binding = new Binding();
      GroovyShell shell = new GroovyShell(binding);
      facts.forEach((i) -> binding.setVariable(i.getKey(), i.getValue()));
      result = (Boolean) shell.evaluate(this.expression);
      LOGGER.info("Executing expression {}, result {}", this.expression, result);
    } catch (MissingPropertyException e) {
      log.error("Missing property for expression {}", this.expression);
    }
    return result;
  }
}
