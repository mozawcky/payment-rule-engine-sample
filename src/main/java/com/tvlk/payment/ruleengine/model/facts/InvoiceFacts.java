package com.tvlk.payment.ruleengine.model.facts;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvoiceFacts {
  private long amount;
  private long time;
  private String deviceInterface;
  private String productType;
  private String productKey;
}
