package com.tvlk.payment.ruleengine.model.rules;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodRules {
  private String id;
  private List<RuleDetails> ruleDetails;
}
