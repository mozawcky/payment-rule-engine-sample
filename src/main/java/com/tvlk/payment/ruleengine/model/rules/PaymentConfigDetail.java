package com.tvlk.payment.ruleengine.model.rules;

import com.tvlk.payment.ruleengine.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentConfigDetail {
  Map<PaymentMethod, PaymentMethodConfig> paymentMethodConfigMap;
}
