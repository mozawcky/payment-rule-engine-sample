package com.tvlk.payment.ruleengine.model.rules;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RuleDetails {
  private String name;
  private String description;
  private String field;
  private String operator;
  private Object value;
}
