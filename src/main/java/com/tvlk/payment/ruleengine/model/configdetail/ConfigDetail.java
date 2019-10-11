package com.tvlk.payment.ruleengine.model.configdetail;

import com.tvlk.payment.ruleengine.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigDetail {
  private String id;
  private List<PaymentMethod> paymentMethodList;
  private ConfigDetail nextNode;
  private ConfigDetail parent;

  public ConfigDetail(String id, List<PaymentMethod> paymentMethodList) {
    this.id = id;
    this.paymentMethodList = paymentMethodList;
  }

  public ConfigDetail addNode(ConfigDetail node) {
    if (this.nextNode == null) {
      node.setParent(this);
      this.nextNode = node;
      return node;
    }
    return this.nextNode.addNode(node);
  }
}
