package com.tvlk.payment.ruleengine.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Request {

  String productName;
  List<String> subProducts;
  BigDecimal amount;
  String currency;
  LocalDateTime transactionTime;
}
