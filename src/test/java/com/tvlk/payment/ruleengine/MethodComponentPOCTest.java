package com.tvlk.payment.ruleengine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvlk.payment.ruleengine.core.TvlkDefaultRulesEngine;
import com.tvlk.payment.ruleengine.groovy.GroovyRuleFactory;
import com.tvlk.payment.ruleengine.model.facts.Facts;
import com.tvlk.payment.ruleengine.model.facts.InvoiceFacts;
import com.tvlk.payment.ruleengine.model.facts.PaymentMethodFacts;
import com.tvlk.payment.ruleengine.model.rules.PaymentConfigRules;
import com.tvlk.payment.ruleengine.model.rules.RuleDetail;
import com.tvlk.payment.ruleengine.model.rules.RuleResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.core.RulesEngineParameters;
import org.jeasy.rules.support.JsonRuleDefinitionReader;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class MethodComponentPOCTest {
  private ObjectMapper objectMapper = new ObjectMapper();
  private GroovyRuleFactory ruleFactory = new GroovyRuleFactory(new JsonRuleDefinitionReader());
  private TvlkDefaultRulesEngine rulesEngine = new TvlkDefaultRulesEngine(new RulesEngineParameters(true, false, false, RulesEngineParameters.DEFAULT_RULE_PRIORITY_THRESHOLD));

  @Test
  public void poc_test_case_1() throws Exception {
    PaymentConfigRules baseRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/1/method-availability-base-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);

    PaymentConfigRules flightRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/1/method-availability-flight-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);
    combineRules(baseRule, flightRule);

    PaymentConfigRules hotelRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/1/method-availability-hotel-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);
    combineRules(baseRule, hotelRule);


    List<PaymentConfigRules> finalRules = new ArrayList<>();
    finalRules.add(flightRule);
    finalRules.add(hotelRule);
    finalRules.add(baseRule);

    Rules rules = ruleFactory.createRules(finalRules);

    Facts facts = getDefaultFacts();
    facts.put("productType", "HOTEL");
    final Set<RuleResult> ruleResultSet = new HashSet<>();
    final Map<String, RuleResult> failConfigRules = new HashMap<>();
    final Map<String, RuleResult> successConfigRules = new HashMap<>();
    facts.put(Constants.FACTS_FAIL_RULE_MAP_KEY, failConfigRules);
    facts.put(Constants.FACTS_SUCCESS_RULE_MAP_KEY, successConfigRules);
    facts.put(Constants.FACTS_RULE_RESULT_SET_KEY, ruleResultSet);

    rulesEngine.fire(rules, facts);

    log.info("failConfigRules {}", objectMapper.writeValueAsString(failConfigRules));
    log.info("successConfigRules {}", objectMapper.writeValueAsString(successConfigRules));
    log.info("ruleResultSet {}", objectMapper.writeValueAsString(ruleResultSet));
  }

  @Test
  public void poc_test_case_3() throws Exception {
    PaymentConfigRules baseRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/3/method-availability-base-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);

    PaymentConfigRules flightRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/3/method-availability-flight-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);
    combineRules(baseRule, flightRule);

    PaymentConfigRules hotelRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/3/method-availability-hotel-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);
    combineRules(baseRule, hotelRule);


    List<PaymentConfigRules> finalRules = new ArrayList<>();
    finalRules.add(flightRule);
    finalRules.add(hotelRule);
    finalRules.add(baseRule);

    Rules rules = ruleFactory.createRules(finalRules);

    Facts facts = getDefaultFacts();
    //facts.put("productType", "HOTEL");
    final Set<RuleResult> ruleResultSet = new HashSet<>();
    final Map<String, RuleResult> failConfigRules = new HashMap<>();
    final Map<String, RuleResult> successConfigRules = new HashMap<>();
    facts.put(Constants.FACTS_FAIL_RULE_MAP_KEY, failConfigRules);
    facts.put(Constants.FACTS_SUCCESS_RULE_MAP_KEY, successConfigRules);
    facts.put(Constants.FACTS_RULE_RESULT_SET_KEY, ruleResultSet);

    rulesEngine.fire(rules, facts);

    log.info("failConfigRules {}", objectMapper.writeValueAsString(failConfigRules));
    log.info("successConfigRules {}", objectMapper.writeValueAsString(successConfigRules));
    log.info("ruleResultSet {}", objectMapper.writeValueAsString(ruleResultSet));
  }

  @Test
  public void poc_test_case_4() throws Exception {
    PaymentConfigRules baseRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/4/method-availability-base-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);

    PaymentConfigRules flightRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/4/method-availability-flight-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);
    combineRules(baseRule, flightRule);

    PaymentConfigRules flightSubRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/4/method-availability-flight-sub-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);
    combineRules(flightRule, flightSubRule);

    PaymentConfigRules hotelRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/4/method-availability-hotel-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);
    combineRules(baseRule, hotelRule);


    List<PaymentConfigRules> finalRules = new ArrayList<>();
    finalRules.add(flightRule);
    finalRules.add(flightSubRule);
    finalRules.add(hotelRule);
    finalRules.add(baseRule);

    Rules rules = ruleFactory.createRules(finalRules);

    Facts facts = getDefaultFacts();
    //facts.put("productType", "HOTEL");
    final Set<RuleResult> ruleResultSet = new HashSet<>();
    final Map<String, RuleResult> failConfigRules = new HashMap<>();
    final Map<String, RuleResult> successConfigRules = new HashMap<>();
    facts.put(Constants.FACTS_FAIL_RULE_MAP_KEY, failConfigRules);
    facts.put(Constants.FACTS_SUCCESS_RULE_MAP_KEY, successConfigRules);
    facts.put(Constants.FACTS_RULE_RESULT_SET_KEY, ruleResultSet);

    rulesEngine.fire(rules, facts);

    log.info("ruleList {}", objectMapper.writeValueAsString(finalRules));
    log.info("failConfigRules {}", objectMapper.writeValueAsString(failConfigRules));
    log.info("successConfigRules {}", objectMapper.writeValueAsString(successConfigRules));
    log.info("ruleResultSet {}", objectMapper.writeValueAsString(ruleResultSet));
  }

  @Test
  public void poc_test_case_5() throws Exception {
    PaymentConfigRules baseRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/5/method-availability-base-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);

    PaymentConfigRules flightRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/5/method-availability-flight-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);
    combineRules(baseRule, flightRule);

    PaymentConfigRules flightSubRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/5/method-availability-flight-sub-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);
    combineRules(flightRule, flightSubRule);

    PaymentConfigRules hotelRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/5/method-availability-hotel-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);
    combineRules(baseRule, hotelRule);


    List<PaymentConfigRules> finalRules = new ArrayList<>();
    finalRules.add(flightRule);
    finalRules.add(flightSubRule);
    finalRules.add(hotelRule);
    finalRules.add(baseRule);

    Rules rules = ruleFactory.createRules(finalRules);

    Facts facts = getDefaultFacts();
    //facts.put("productType", "HOTEL");
    final Set<RuleResult> ruleResultSet = new HashSet<>();
    final Map<String, RuleResult> failConfigRules = new HashMap<>();
    final Map<String, RuleResult> successConfigRules = new HashMap<>();
    facts.put(Constants.FACTS_FAIL_RULE_MAP_KEY, failConfigRules);
    facts.put(Constants.FACTS_SUCCESS_RULE_MAP_KEY, successConfigRules);
    facts.put(Constants.FACTS_RULE_RESULT_SET_KEY, ruleResultSet);

    rulesEngine.fire(rules, facts);

    log.info("ruleList {}", objectMapper.writeValueAsString(finalRules));
    log.info("failConfigRules {}", objectMapper.writeValueAsString(failConfigRules));
    log.info("successConfigRules {}", objectMapper.writeValueAsString(successConfigRules));
    log.info("ruleResultSet {}", objectMapper.writeValueAsString(ruleResultSet));
  }


  @Test
  public void poc_test_case_6() throws Exception {
    PaymentConfigRules baseRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/6/method-availability-base-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);

    PaymentConfigRules flightRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/6/method-availability-flight-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);
    combineRules(baseRule, flightRule);

    PaymentConfigRules flightSubRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/6/method-availability-flight-sub-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);
    combineRules(flightRule, flightSubRule);

    PaymentConfigRules flightSub2Rule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/6/method-availability-flight-sub-2-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);
    combineRules(flightRule, flightSub2Rule);

    PaymentConfigRules hotelRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/6/method-availability-hotel-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);
    combineRules(baseRule, hotelRule);


    List<PaymentConfigRules> finalRules = new ArrayList<>();
    finalRules.add(flightRule);
    finalRules.add(flightSubRule);
    finalRules.add(flightSub2Rule);
    finalRules.add(hotelRule);
    finalRules.add(baseRule);

    Rules rules = ruleFactory.createRules(finalRules);

    Facts facts = getDefaultFacts();
    facts.put("productType", "FLIGHT");
    facts.put("productKey", "FL02");
    final Set<RuleResult> ruleResultSet = new HashSet<>();
    final Map<String, RuleResult> failConfigRules = new HashMap<>();
    final Map<String, RuleResult> successConfigRules = new HashMap<>();
    facts.put(Constants.FACTS_FAIL_RULE_MAP_KEY, failConfigRules);
    facts.put(Constants.FACTS_SUCCESS_RULE_MAP_KEY, successConfigRules);
    facts.put(Constants.FACTS_RULE_RESULT_SET_KEY, ruleResultSet);

    rulesEngine.fire(rules, facts);

    log.info("ruleList {}", objectMapper.writeValueAsString(finalRules));
    log.info("failConfigRules {}", objectMapper.writeValueAsString(failConfigRules));
    log.info("successConfigRules {}", objectMapper.writeValueAsString(successConfigRules));
    log.info("ruleResultSet {}", objectMapper.writeValueAsString(ruleResultSet));
  }

  @Test
  public void poc_test_case_7() throws Exception {
    PaymentConfigRules baseRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/7/method-availability-base-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);

    PaymentConfigRules flightRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/7/method-availability-flight-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);
    combineRules(baseRule, flightRule);

    PaymentConfigRules flightSubRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/7/method-availability-flight-sub-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);
    combineRules(flightRule, flightSubRule);

    PaymentConfigRules flightSub2Rule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/7/method-availability-flight-sub-2-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);
    combineRules(flightRule, flightSub2Rule);

    PaymentConfigRules hotelRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/7/method-availability-hotel-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);
    combineRules(baseRule, hotelRule);

    PaymentConfigRules hotelSubRule = objectMapper.readValue(FileUtils.readFileToString(
        ResourceUtils.getFile("classpath:poc/method-availability/7/method-availability-hotel-sub-rule.json"),
        StandardCharsets.UTF_8), PaymentConfigRules.class);
    combineRules(hotelRule, hotelSubRule);


    List<PaymentConfigRules> finalRules = new ArrayList<>();
    finalRules.add(flightRule);
    finalRules.add(flightSubRule);
    finalRules.add(flightSub2Rule);
    finalRules.add(hotelRule);
    finalRules.add(hotelSubRule);
    finalRules.add(baseRule);

    Rules rules = ruleFactory.createRules(finalRules);

    Facts facts = getDefaultFacts();
    facts.put("productType", "HOTEL");
    facts.put("productKey", "");
    final Set<RuleResult> ruleResultSet = new HashSet<>();
    final Map<String, RuleResult> failConfigRules = new HashMap<>();
    final Map<String, RuleResult> successConfigRules = new HashMap<>();
    facts.put(Constants.FACTS_FAIL_RULE_MAP_KEY, failConfigRules);
    facts.put(Constants.FACTS_SUCCESS_RULE_MAP_KEY, successConfigRules);
    facts.put(Constants.FACTS_RULE_RESULT_SET_KEY, ruleResultSet);

    rulesEngine.fire(rules, facts);

    log.info("ruleList {}", objectMapper.writeValueAsString(finalRules));
    log.info("failConfigRules {}", objectMapper.writeValueAsString(failConfigRules));
    log.info("successConfigRules {}", objectMapper.writeValueAsString(successConfigRules));
    log.info("ruleResultSet {}", objectMapper.writeValueAsString(ruleResultSet));
  }


  private List<PaymentConfigRules> generateRules(PaymentConfigRules... rules) {
    List<PaymentConfigRules> rulesList = new ArrayList<>();

    for (int i = 0; i < rules.length - 1; i++) {
      combineRules(rules[i], rules[i+1]);

      rulesList.add(rules[i]);
    }

    return rulesList;
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
