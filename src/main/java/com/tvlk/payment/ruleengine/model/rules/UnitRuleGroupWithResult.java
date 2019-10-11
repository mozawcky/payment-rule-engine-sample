package com.tvlk.payment.ruleengine.model.rules;

import com.tvlk.payment.ruleengine.model.rules.RuleResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.support.UnitRuleGroup;

@Slf4j
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UnitRuleGroupWithResult extends UnitRuleGroup {

  private final RuleResult ruleResult;

  public UnitRuleGroupWithResult() {
    super();
    this.ruleResult = new RuleResult(this.name);
  }

  public UnitRuleGroupWithResult(String name) {
    super(name);
    this.ruleResult = new RuleResult(name);
  }
}
