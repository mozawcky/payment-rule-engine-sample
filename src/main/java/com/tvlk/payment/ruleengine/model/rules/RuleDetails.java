package com.tvlk.payment.ruleengine.model.rules;

import com.tvlk.payment.ruleengine.groovy.GroovyRule;
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
  private RuleOperatorEnum operator;
  private Object value;

  public GroovyRule toGroovy() {

    String condition = "";
    switch (operator) {
      case EQUAL:
        condition = field + ".equals(" + "'" + value + "')";
        break;
      case IN:
        condition = value + ".contains(" + field + "')";
        break;
      case LTE:
        condition = field + "<=" + value;
        break;
      case GTE:
        condition = field + ">=" + value;
        break;
    }

    return new GroovyRule()
        .name(name)
        .description(description)
        .when(condition);
  }
}
