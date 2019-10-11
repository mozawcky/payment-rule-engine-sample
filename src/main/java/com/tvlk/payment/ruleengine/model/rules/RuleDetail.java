package com.tvlk.payment.ruleengine.model.rules;

import com.tvlk.payment.ruleengine.groovy.GroovyRule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jeasy.rules.api.Rule;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleDetail {

  private String name;
  private String description;
  private String field;
  private RuleOperatorEnum operator;
  private Object value;

  public GroovyRule toGroovy() {

    String condition;
    switch (operator) {
      case EQUAL:
        condition = field + operator.getOperator() + "(" + "'" + value + "')";
        break;
      case IN:
        condition = value.toString() + operator.getOperator() + "(" + field + ")";
        break;
      case LTE:
      case LT:
      case GTE:
      case GT:
        condition = field + operator.getOperator() + value;
        break;
      default:
        condition = "Boolean.FALSE";
        break;
    }

    return new GroovyRule()
        .name(name)
        .description(description)
        .when(condition);
  }
}
