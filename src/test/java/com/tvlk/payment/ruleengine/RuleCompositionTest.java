package com.tvlk.payment.ruleengine;

import com.tvlk.payment.ruleengine.model.facts.Facts;
import com.tvlk.payment.ruleengine.model.facts.InvoiceFacts;
import com.tvlk.payment.ruleengine.model.facts.PaymentMethodFacts;
import com.tvlk.payment.ruleengine.model.rules.RuleDetails;
import com.tvlk.payment.ruleengine.model.rules.Rules;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RuleCompositionTest {

  @Test
  public void testRuleComposition() throws Exception {
    Facts facts = getDefaultFacts();
    System.out.println("facts : " + facts);
    
  }

  private Rules getPaymentRules() {
    return new Rules();
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
