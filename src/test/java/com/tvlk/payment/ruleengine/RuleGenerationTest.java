package com.tvlk.payment.ruleengine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvlk.payment.ruleengine.groovy.GroovyRuleFactory;
import com.tvlk.payment.ruleengine.model.facts.Facts;
import com.tvlk.payment.ruleengine.model.facts.InvoiceFacts;
import com.tvlk.payment.ruleengine.model.facts.PaymentMethodFacts;
import com.tvlk.payment.ruleengine.model.rules.PaymentConfigRules;
import com.tvlk.payment.ruleengine.model.rules.RuleDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.support.JsonRuleDefinitionReader;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class RuleGenerationTest {
  private ObjectMapper objectMapper = new ObjectMapper();
  private GroovyRuleFactory ruleFactory = new GroovyRuleFactory(new JsonRuleDefinitionReader());
  private List<PaymentConfigRules> paymentConfigRules = new ArrayList<>();

  @Before
  @Test
  public void ruleGenerationTest() throws IOException {
    // Loading base rule
    PaymentConfigRules base = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:zaky-dennis-base-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);

    PaymentConfigRules paymentRules = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:zaky-dennis-payment-rules.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);

    combineRules(base, paymentRules);

    PaymentConfigRules productRules = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:zaky-dennis-product-rules.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);

    combineRules(paymentRules, productRules);

    PaymentConfigRules subProductRules = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:zaky-dennis-sub-product-rules.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);

    combineRules(productRules, subProductRules);

    // Add all in one rule list with order is sub_product rule, product rule, payment rule
    List<PaymentConfigRules> finalRuleList = new ArrayList<>();
    finalRuleList.add(subProductRules);
    finalRuleList.add(productRules);
    finalRuleList.add(paymentRules);

    paymentConfigRules = finalRuleList;

    log.info(finalRuleList.toString());

    log.info(objectMapper.writeValueAsString(finalRuleList));
  }

  @Test
  public void ruleEngineTest() throws IOException {
    Facts facts = getDefaultFacts();
    log.info("facts {} ", facts.asMap());
    try {
      Rules rules = ruleFactory.createRules(paymentConfigRules.get(0));
      for (Rule rule : rules) {
        boolean ruleEvaluationResult = rule.evaluate(facts);
        log.info("Rule [{}] matched?, {}", rule, ruleEvaluationResult);
      }
    } catch (Exception e) {
      log.error("Exception : ", e);
    }
  }

  private void combineRules(PaymentConfigRules from, PaymentConfigRules to) {
    Set<String> toRuleNames = new HashSet<>();
    for (RuleDetail ruleDetail : to.getRuleDetails()) {
      toRuleNames.add(ruleDetail.getName());
    }
    for (RuleDetail ruleDetail : from.getRuleDetails()) {
      if (!toRuleNames.contains(ruleDetail.getName())) {
        to.getRuleDetails().add(ruleDetail);
      }
    }
  }

  private Facts getDefaultFacts() {
    Facts facts = new Facts();
    populateFacts(facts, getDefaultInvoiceFacts());
    populateFacts(facts, getDefaultPaymentMethodFacts());
    return facts;
  }

  private InvoiceFacts getDefaultInvoiceFacts() {
    InvoiceFacts invoiceFacts = new InvoiceFacts();
    invoiceFacts.setCurrency("IDR");
    invoiceFacts.setAmount(456456);
    invoiceFacts.setTime(System.currentTimeMillis());
    invoiceFacts.setDeviceInterface("DESKTOP");
    invoiceFacts.setProductType("FLIGHT");
    invoiceFacts.setProductKey("FL01");
    return invoiceFacts;
  }

  private void populateFacts(org.jeasy.rules.api.Facts facts, Object object) {
    Field[] fields = object.getClass().getDeclaredFields();
    for (Field field : fields) {
      try {
        field.setAccessible(true);
        facts.put(field.getName(), field.get(object));
      } catch (IllegalAccessException e) {
        log.error(e.getMessage(), e);
      }
    }
    log.info(facts.asMap().toString());
  }

  private PaymentMethodFacts getDefaultPaymentMethodFacts() {
    PaymentMethodFacts paymentMethodFacts = new PaymentMethodFacts();
    paymentMethodFacts.setPaymentMethod("CREDIT_CARD");
    return paymentMethodFacts;
  }
}
