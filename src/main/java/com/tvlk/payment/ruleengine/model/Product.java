package com.tvlk.payment.ruleengine.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Product {
  private String name;
  private BigDecimal amount;
  private String currency;
}
