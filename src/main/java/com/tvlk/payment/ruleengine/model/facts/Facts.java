package com.tvlk.payment.ruleengine.model.facts;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Facts {
  private InvoiceFacts invoiceFacts;
  private PaymentMethodFacts paymentMethodFacts;
}
