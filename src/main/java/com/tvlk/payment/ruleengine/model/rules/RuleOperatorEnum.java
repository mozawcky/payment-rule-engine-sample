package com.tvlk.payment.ruleengine.model.rules;

public enum RuleOperatorEnum {
  EQUAL(".equals"),
  IN(".contains"),
  LTE("<="),
  LT("<"),
  GTE(">="),
  GT(">");

  private String operator;

  RuleOperatorEnum(String operator) {
    this.operator = operator;
  }

  public String getOperator() {
    return operator;
  }
}
