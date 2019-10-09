package com.tvlk.payment.ruleengine.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Action;
import org.jeasy.rules.api.Facts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@Slf4j
@Data
@AllArgsConstructor
public class GroovyAction implements Action {

  private String expression;

  @Override
  public void execute(Facts facts) {
     if (Objects.isNull(facts)) {
       throw new IllegalArgumentException("Invalid facts [null]");
     }
    final Binding binding = new Binding();
    final GroovyShell shell = new GroovyShell(binding);
    facts.forEach((i) -> binding.setVariable(i.getKey(), i.getValue()));
    shell.evaluate(this.expression);
  }
}
