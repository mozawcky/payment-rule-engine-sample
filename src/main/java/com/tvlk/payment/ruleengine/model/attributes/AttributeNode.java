package com.tvlk.payment.ruleengine.model.attributes;

import com.tvlk.payment.ruleengine.model.PaymentMethod;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AttributeNode {
  private String id;
  private List<PaymentMethod> paymentMethodList;
  private AttributeNode nextNode;
}
