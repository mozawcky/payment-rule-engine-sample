package com.tvlk.payment.ruleengine.model.rules;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Rules {
  private String id;
  private List<RuleDetails> ruleDetails;
}
