package com.tvlk.payment.ruleengine.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Action;
import org.jeasy.rules.api.Facts;

import java.util.Objects;

@Slf4j
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GroovyAction implements Action {

  String expression;

  @Override
  public void execute(Facts facts) {
    if (Objects.isNull(facts)) {
      throw new IllegalArgumentException("Invalid facts [null]");
    }
    log.debug("Evaluating action {}", this.expression);
    final Binding binding = new Binding();
    final GroovyShell shell = new GroovyShell(binding);
    facts.forEach((i) -> binding.setVariable(i.getKey(), i.getValue()));
    shell.evaluate(this.expression);
  }
}
