package com.tvlk.payment.ruleengine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvlk.payment.ruleengine.core.DefaultRulesEngine;
import com.tvlk.payment.ruleengine.groovy.GroovyRuleFactory;
import com.tvlk.payment.ruleengine.model.facts.Facts;
import com.tvlk.payment.ruleengine.model.facts.InvoiceFacts;
import com.tvlk.payment.ruleengine.model.facts.PaymentMethodFacts;
import com.tvlk.payment.ruleengine.model.rules.PaymentConfigRules;
import com.tvlk.payment.ruleengine.model.rules.RuleDetail;
import com.tvlk.payment.ruleengine.model.rules.RuleResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.core.RulesEngineParameters;
import org.jeasy.rules.support.JsonRuleDefinitionReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class RuleGenerationTest {
  private ObjectMapper objectMapper = new ObjectMapper();
  private GroovyRuleFactory ruleFactory = new GroovyRuleFactory(new JsonRuleDefinitionReader());
  private List<PaymentConfigRules> paymentConfigRulesList = new ArrayList<>();
  private JsonNode configDetail;
  private DefaultRulesEngine rulesEngine;
  private List<Rules> rulesList = new ArrayList<>();
  private Rules testRules;


  @Before
  @Test
  public void generateRules() throws Exception {
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

    paymentConfigRulesList = finalRuleList;
    log.info(finalRuleList.toString());
    log.info(objectMapper.writeValueAsString(finalRuleList));

    // Initialize rule engine
    rulesEngine = new DefaultRulesEngine(new RulesEngineParameters(true, false, false, RulesEngineParameters.DEFAULT_RULE_PRIORITY_THRESHOLD));

    // Derive Rules from PaymentConfigRules
    for (PaymentConfigRules paymentConfigRules : paymentConfigRulesList) {
      Rules rules = ruleFactory.createRules(paymentConfigRules);
      rulesList.add(rules);
    }

    testRules = ruleFactory.createRules(paymentConfigRulesList);
    log.info(testRules.toString());
  }

  @Before
  @Test
  public void generateConfigDetailTree() throws IOException {
    configDetail = objectMapper.readTree(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:config-detail/flight-payment-method-availability-tree.json"),
        StandardCharsets.UTF_8));
  }

  @Test
  public void ruleEngineTest() {
    Facts facts = getDefaultFacts();
    log.info("facts {} ", facts.asMap());
    try {
      Rules rules = ruleFactory.createRules(paymentConfigRulesList.get(0));
      for (Rule rule : rules) {
        boolean ruleEvaluationResult = rule.evaluate(facts);
        log.info("Rule [{}] matched?, {}", rule, ruleEvaluationResult);
      }
    } catch (Exception e) {
      log.error("Exception : ", e);
    }
  }

  @Test
  public void paymentConfigUnitRuleGroup1stMatchedTest() {
    Facts facts = getDefaultFacts();
    final Map<String, RuleResult> failConfigRules = new HashMap<>();
    final Map<String, RuleResult> successConfigRules = new HashMap<>();
    facts.put(Constants.FACTS_RULE_RESULT_KEY, failConfigRules);
    facts.put(Constants.FACTS_SUCCESS_RULE_MAP_KEY, successConfigRules);

    rulesEngine.fire(testRules, facts);
    log.info("failConfigRules {}", failConfigRules);
    log.info("successConfigRules {}", successConfigRules);
    Assert.assertTrue(failConfigRules.isEmpty());
    Assert.assertEquals(1, successConfigRules.size());
    Assert.assertNotNull(successConfigRules.get("FLIGHT_SUB"));
  }

  /**
   * In case of having multiple products in the request, we should:
   * 1. Create list of facts per product type such as Hotel, Flight, Hotel+Flight
   * 2. Evaluate each fact against the combined rules.
   * 3. Each fact will match a rule which return a config details.
   * 4. After evaluation we can have few matched rules for all facts. Take the intersection of those config details
   * Flight with sub product FLO1 have config details of [BANK_TRANSFER, CC, ATM]
   * Hotel with sub product HO11 have config details of [BANK_TRANSFER, CC]
   * => final config details for this payment option request is [BANK_TRANSFER, CC]
   */
  @Test
  public void paymentConfigUnitRuleGroup2ndMatchedTest() {
    Facts facts = getDefaultFacts();
    facts.put("productKey", "FL02"); // Change fact value to make it fail when being evaluated by sample sub product rule

    final Map<String, Set<Rule>> failConfigRules = new HashMap<>();
    final Map<String, Set<Rule>> successConfigRules = new HashMap<>();
    facts.put(Constants.FACTS_FAIL_RULE_MAP_KEY, failConfigRules);
    facts.put(Constants.FACTS_SUCCESS_RULE_MAP_KEY, successConfigRules);

    rulesEngine.fire(testRules, facts);
    log.info("failConfigRules {}", failConfigRules);
    log.info("successConfigRules {}", successConfigRules);
    Assert.assertFalse(failConfigRules.isEmpty());
    Assert.assertEquals(1, successConfigRules.size());
    Assert.assertNotNull(successConfigRules.get("FLIGHT"));
  }

  @Test
  public void bank_transfer_flight_sub_test() throws Exception {
    boolean result = false;
    Rule resultRule = null;

    List<PaymentConfigRules> rulesList = generateBankTransferFlightRule();
    Facts facts = getDefaultFacts();

    log.info("facts {} ", facts.asMap());

    try {
      ListIterator<PaymentConfigRules> rulesListIterator = rulesList.listIterator();

      while (!result && rulesListIterator.hasNext()) {
        Rules rules = ruleFactory.createRules(rulesListIterator.next());

        for (Rule rule : rules) {
          result = rule.evaluate(facts);
          log.info("Rule [{}] matched?, {}", rule, result);
          if (result) {
            resultRule = rule;
          }
        }
      }

      JsonNode locatedNode = null;
      Iterator<JsonNode> attributeIterable = configDetail.iterator();
      while (attributeIterable.hasNext() && !attributeIterable.next().path("id").equals("FLIGHT_SUB")) {
        locatedNode = attributeIterable.next();
      }

      log.info("Rule : " + resultRule.getDescription());
      log.info("Attribute : " + locatedNode.path("paymentMethodList"));
    } catch (Exception e) {
      log.error("Exception : ", e);
    }
  }

  private List<PaymentConfigRules> generateBankTransferFlightRule() throws IOException {
    PaymentConfigRules baseRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:rules/bank-transfer-availability-base-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);

    PaymentConfigRules productRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:rules/bank-transfer-availability-flight-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);
    combineRules(baseRule, productRule);

    PaymentConfigRules subProductRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:rules/bank-transfer-availability-flight-sub-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);
    combineRules(productRule, subProductRule);

    List<PaymentConfigRules> finalRuleList = new ArrayList<>();
    finalRuleList.add(subProductRule);
    finalRuleList.add(productRule);
    finalRuleList.add(baseRule);

    return finalRuleList;
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

  /**
   * @param facts
   * @param object
   */
  private void populateFacts(Facts facts, Object object) {
    if (Objects.isNull(facts) || Objects.isNull(object)) {
      throw new IllegalArgumentException("Invalid facts or object");
    }
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
