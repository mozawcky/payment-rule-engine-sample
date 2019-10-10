package com.tvlk.payment.ruleengine.model.attributes;

import com.tvlk.payment.ruleengine.model.PaymentMethod;
import com.tvlk.payment.ruleengine.model.ProductType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@NoArgsConstructor
public class ProductPaymentMethodAttributes {
  private ProductType productType;
  private HashMap<PaymentMethod, PaymentMethodAttributes> attributes;
}
