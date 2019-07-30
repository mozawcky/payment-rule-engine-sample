package com.tvlk.payment.ruleengine.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
  private String name;
  private BigDecimal amount;
  private String currency;
  private String providerName;
}
