package com.tvlk.payment.ruleengine.model.rules;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRules {
  private String id;
  private PaymentMethodRules paymentMethodRules;
}
