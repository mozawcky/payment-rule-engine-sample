package com.tvlk.payment.ruleengine;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Rule;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RuleResult {
  String ruleName;
  Set<Rule> failRuleSet;
  Set<Rule> successRuleSet;

  public RuleResult(String ruleName) {
    this.ruleName = ruleName;
    this.failRuleSet = new HashSet<>();
    this.successRuleSet = new HashSet<>();
  }
}
