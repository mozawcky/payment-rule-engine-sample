package com.tvlk.payment.ruleengine.model.rules;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleDetails {
  private String name;
  private String description;
  private String field;
  private String operator;
  private Object value;
}