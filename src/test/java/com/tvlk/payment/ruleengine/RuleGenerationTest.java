package com.tvlk.payment.ruleengine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvlk.payment.ruleengine.model.rules.PaymentConfigRules;
import com.tvlk.payment.ruleengine.model.rules.RuleDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class RuleGenerationTest {
  private ObjectMapper objectMapper = new ObjectMapper();

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

    log.info(base.toString());
    log.info(paymentRules.toString());
    log.info(productRules.toString());
    log.info(subProductRules.toString());
    log.info(finalRuleList.toString());

    log.info(objectMapper.writeValueAsString(finalRuleList));
  }

  private void combineRules(PaymentConfigRules from, PaymentConfigRules to) {
    Set<String> toRuleNames = new HashSet<>();
    for (RuleDetails ruleDetails : to.getRuleDetails()) {
      toRuleNames.add(ruleDetails.getName());
    }
    for (RuleDetails ruleDetails : from.getRuleDetails()) {
      if (!toRuleNames.contains(ruleDetails.getName())) {
        to.getRuleDetails().add(ruleDetails);
      }
    }
  }
}
