package com.tvlk.payment.ruleengine.model.facts;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Facts {
  private InvoiceFacts invoiceFacts;
  private PaymentMethodFacts paymentMethodFacts;
}
