package com.tvlk.payment.ruleengine.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Request {

  String productName;
  List<String> subProducts;
  BigDecimal amount;
  String currency;
}
