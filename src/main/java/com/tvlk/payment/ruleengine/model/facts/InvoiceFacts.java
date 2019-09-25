package com.tvlk.payment.ruleengine.model.facts;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvoiceFacts {
  private String currency;
  private long amount;
  private long time;
  private String deviceInterface;
  private String productType;
  private String productKey;
}
