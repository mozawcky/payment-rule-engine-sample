package com.tvlk.payment.ruleengine.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jeasy.rules.api.Action;
import org.jeasy.rules.api.Facts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@AllArgsConstructor
public class GroovyAction implements Action {

  private static final Logger LOGGER = LoggerFactory.getLogger(GroovyAction.class);

  private String expression;

  @Override
  public void execute(Facts facts) {
    Binding binding = new Binding();
    GroovyShell shell = new GroovyShell(binding);
    facts.forEach((i)-> binding.setVariable(i.getKey(), i.getValue()));
    shell.evaluate(this.expression);
  }
}
