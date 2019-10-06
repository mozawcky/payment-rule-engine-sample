package com.tvlk.payment.ruleengine.model.facts;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Facts extends org.jeasy.rules.api.Facts {
  private InvoiceFacts invoiceFacts;
  private PaymentMethodFacts paymentMethodFacts;
}
