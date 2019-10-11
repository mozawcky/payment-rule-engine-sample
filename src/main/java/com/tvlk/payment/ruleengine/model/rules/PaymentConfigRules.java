package com.tvlk.payment.ruleengine.model.rules;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class PaymentConfigRules {
  String id;
  String description;
  int priority;
  List<RuleDetail> ruleDetails;
}
