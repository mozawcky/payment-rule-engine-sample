package com.tvlk.payment.ruleengine.model.facts;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jeasy.rules.api.Facts;

@Data
@NoArgsConstructor
public class InvoiceFacts extends Facts {
  private String currency;
  private long amount;
  private long time;
  private String deviceInterface;
  private String productType;
  private String productKey;
}
