package com.tvlk.payment.ruleengine.model.configdetail;

import com.tvlk.payment.ruleengine.model.PaymentMethod;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ConfigDetail {
  private String id;
  private List<PaymentMethod> paymentMethodList;
  private ConfigDetail nextNode;
}
