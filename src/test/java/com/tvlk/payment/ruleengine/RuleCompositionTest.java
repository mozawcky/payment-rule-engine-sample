package com.tvlk.payment.ruleengine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvlk.payment.ruleengine.model.facts.Facts;
import com.tvlk.payment.ruleengine.model.facts.InvoiceFacts;
import com.tvlk.payment.ruleengine.model.facts.PaymentMethodFacts;
import com.tvlk.payment.ruleengine.model.rules.PaymentMethodRules;
import com.tvlk.payment.ruleengine.model.rules.ProductRules;
import com.tvlk.payment.ruleengine.model.rules.RuleDetail;
import java.io.File;

import org.junit.Test;

public class RuleCompositionTest {

  ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void testRuleCombination() throws Exception {
    File paymentRulesFile = new File("src/test/resources/payment-rules.json");
    PaymentMethodRules paymentMethodRules =
        objectMapper.readValue(paymentRulesFile, PaymentMethodRules.class);
    File productRulesFile = new File("src/test/resources/product-rules.json");
    ProductRules productRules = objectMapper.readValue(productRulesFile, ProductRules.class);

    combine(paymentMethodRules, productRules.getPaymentMethodRules());
  }

  private void combine(PaymentMethodRules baseRule, PaymentMethodRules newRule) {
    for (RuleDetail baseRuleDetail : baseRule.getRuleDetails()) {
      for (RuleDetail newRuleDetail : newRule.getRuleDetails()) {
        if (baseRuleDetail.getName().equals(newRuleDetail.getName())) {
          baseRuleDetail.setValue(newRuleDetail.getValue());
        }
      }
    }
  }

  private PaymentMethodRules getPaymentRules() {
    return new PaymentMethodRules();
  }

  private Facts getDefaultFacts() {
    Facts facts = new Facts();
    facts.setInvoiceFacts(getDefaultInvoiceFacts());
    facts.setPaymentMethodFacts(getDefaultPaymentMethodFacts());

    return facts;
  }

  private InvoiceFacts getDefaultInvoiceFacts() {
    InvoiceFacts invoiceFacts = new InvoiceFacts();
    invoiceFacts.setCurrency("IDR");
    invoiceFacts.setAmount(123123);
    invoiceFacts.setTime(System.currentTimeMillis());
    invoiceFacts.setDeviceInterface("DESKTOP");
    invoiceFacts.setProductType("FLIGHT");
    invoiceFacts.setProductKey("FLX01");

    return invoiceFacts;
  }

  private PaymentMethodFacts getDefaultPaymentMethodFacts() {
    PaymentMethodFacts paymentMethodFacts = new PaymentMethodFacts();
    paymentMethodFacts.setPaymentMethod("CREDIT_CARD");

    return paymentMethodFacts;
  }
}
