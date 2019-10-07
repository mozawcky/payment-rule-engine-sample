package com.tvlk.payment.ruleengine.model.rules;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodRules {
  private String id;
  private List<RuleDetail> ruleDetails;
}
