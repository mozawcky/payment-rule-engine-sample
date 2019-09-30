package com.tvlk.payment.ruleengine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvlk.payment.ruleengine.model.facts.Facts;
import com.tvlk.payment.ruleengine.model.facts.InvoiceFacts;
import com.tvlk.payment.ruleengine.model.facts.PaymentMethodFacts;
import com.tvlk.payment.ruleengine.model.rules.ProductRules;
import com.tvlk.payment.ruleengine.model.rules.RuleDetails;
import com.tvlk.payment.ruleengine.model.rules.PaymentMethodRules;
import org.junit.Test;

import java.io.File;


public class RuleCompositionTest {

  ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void testRuleCombination() throws Exception {
    File paymentRulesFile = new File("src/test/resources/payment-rules.json");
    PaymentMethodRules paymentPaymentMethodRules = objectMapper.readValue(paymentRulesFile, PaymentMethodRules.class);
    File productRulesFile = new File("src/test/resources/product-rules.json");
    ProductRules productRules = objectMapper.readValue(productRulesFile, ProductRules.class);

    combine(paymentPaymentMethodRules, productRules.getPaymentMethodRules());

    System.out.println("asu");
  }

  private void combine(PaymentMethodRules baseRule, PaymentMethodRules newRule) {
    for (RuleDetails baseRuleDetails : baseRule.getRuleDetails()) {
      for (RuleDetails newRuleDetails : newRule.getRuleDetails()) {
        if (baseRuleDetails.getName().equals(newRuleDetails.getName())) {
          baseRuleDetails.setValue(newRuleDetails.getValue());
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
