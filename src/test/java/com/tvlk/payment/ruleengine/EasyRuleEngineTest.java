package com.tvlk.payment.ruleengine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvlk.payment.ruleengine.groovy.GroovyRuleFactory;
import com.tvlk.payment.ruleengine.model.MyFacts;
import com.tvlk.payment.ruleengine.model.Person;
import com.tvlk.payment.ruleengine.model.Person.GENDER;
import com.tvlk.payment.ruleengine.model.Product;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

import com.tvlk.payment.ruleengine.model.Request;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.spel.SpELRuleFactory;
import org.jeasy.rules.support.JsonRuleDefinitionReader;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EasyRuleEngineTest {

  SpELRuleFactory factory = new SpELRuleFactory(new JsonRuleDefinitionReader());
  ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void test() throws Exception {
    String pathname = "src/test/resources/adult-rule.json";
    File file = new File(pathname);
    Rules rules = factory.createRules(new FileReader(file));

    Facts facts = new Facts();
    Person person = new Person("Dennis Dao", 21, GENDER.MALE, true);
    facts.put("person", person);
    facts.put("log", log);

    RulesEngine rulesEngine = new DefaultRulesEngine();
    rulesEngine.fire(rules, facts);
  }

  @Test
  public void testCompositeRuleActivationGroup() throws Exception {
    String pathname = "src/test/resources/composite-rules.json";
    File file = new File(pathname);
    Rule rule = factory.createRule(new FileReader(file));

    Facts facts = new Facts();
    Person person = new Person("Dennis Dao", 21, GENDER.MALE, false);
    facts.put("person", person);
    facts.put("log", log);

    boolean ruleEvaluationResult = rule.evaluate(facts);
    log.info("ruleEvaluationResult ", ruleEvaluationResult);
  }

  @Test
  public void testCompositeRulePaymentOption() throws Exception {
    String pathname = "src/test/resources/bank-transfer-bni-rules.json";
    File file = new File(pathname);
    Rules rules = factory.createRules(new FileReader(file));
    Rule rule = factory.createRule(new FileReader(file));

    Facts facts = new Facts();
    Set<Rule> results = new TreeSet<>();
    MyFacts myFacts = new MyFacts("BNI");
    facts.put("facts", myFacts);
    facts.put("log", log);
    facts.put("now", LocalDateTime.now());
    facts.put("results", results);

    RulesEngine rulesEngine = new DefaultRulesEngine();
    // rulesEngine.fire(rules, facts);
    System.out.println();
    boolean ruleEvaluationResult = rule.evaluate(facts);
    log.info("ruleEvaluationResult ", ruleEvaluationResult);
    if (ruleEvaluationResult) {
      log.info("Matched rule {}", rule);
    }
  }

  @Test
  public void testCompositeRulePaymentOption_CC_SpEL() throws Exception {
    String pathname = "src/test/resources/credit-card-rules.json";
    File file = new File(pathname);
    Rules rules = factory.createRules(new FileReader(file));
    Rule rule = factory.createRule(new FileReader(file));

    Facts facts = new Facts();
    Product product = new Product("FLIGHT", new BigDecimal(100), "IDR", "BNI");
    facts.put("facts", product);
    facts.put("log", log);

    RulesEngine rulesEngine = new DefaultRulesEngine();
    // rulesEngine.fire(rules, facts);
    System.out.println();
    boolean ruleEvaluationResult = rule.evaluate(facts);
    log.info("Rule [{}] matched?, {}", rule, ruleEvaluationResult);
  }

  @Test
  public void testCompositeRulePaymentOption_CC_groovy() throws Exception {
    String pathname = "src/test/resources/credit-card-rules-groovy.json";
    File file = new File(pathname);
    GroovyRuleFactory groovyRuleFactory = new GroovyRuleFactory(new JsonRuleDefinitionReader());
    Rules rules = groovyRuleFactory.createRules(new FileReader(file));
    Rule rule = groovyRuleFactory.createRule(new FileReader(file));

    Facts facts = new Facts();
    Product product = new Product("FLIGHT", new BigDecimal(100), "IDR", "BNI");
    facts.put("facts", product);
    facts.put("log", log);

    boolean ruleEvaluationResult = rule.evaluate(facts);
    log.info("Rule [{}] matched?, {}", rule, ruleEvaluationResult);
  }

  @Test
  public void testCompositeRulePaymentOption_CC_Multiple_Matched_groovy() throws Exception {
    String pathname = "src/test/resources/credit-card-rules-multiple-matched-groovy.json";
    File file = new File(pathname);
    GroovyRuleFactory groovyRuleFactory = new GroovyRuleFactory(new JsonRuleDefinitionReader());
    Rules rules = groovyRuleFactory.createRules(new FileReader(file));

    Facts facts = new Facts();
    Product product = new Product("FLIGHT", new BigDecimal(100), "IDR", "BNI");
    facts.put("facts", product);
    facts.put("log", log);

    for (Rule rule : rules) {
      boolean ruleEvaluationResult = rule.evaluate(facts);
      log.info("Rule [{}] matched?, {}", rule, ruleEvaluationResult);
      Assert.assertTrue(ruleEvaluationResult);
    }
  }

  @Test
  public void testCompositeRulePaymentOption_CC_Single_Matched_groovy() throws Exception {
    String pathname = "src/test/resources/credit-card-rules-single-matched-groovy.json";
    File file = new File(pathname);
    GroovyRuleFactory groovyRuleFactory = new GroovyRuleFactory(new JsonRuleDefinitionReader());
    Rules rules = groovyRuleFactory.createRules(new FileReader(file));

    Facts facts = new Facts();
    Product product = new Product("FLIGHT", new BigDecimal(100), "IDR", "MANDIRI");
    facts.put("facts", product);
    facts.put("log", log);
    for (Rule rule : rules) {
      boolean ruleEvaluationResult = rule.evaluate(facts);
      log.info("Rule [{}] matched?, {}", rule, ruleEvaluationResult);
    }
  }

  @Test
  public void testCompositeRulePaymentOption_Domestic_Flight_Matched_groovy() throws Exception {
    String pathname = "src/test/resources/credit-card-rules-single-flight-product-groovy.json";
    File file = new File(pathname);
    GroovyRuleFactory groovyRuleFactory = new GroovyRuleFactory(new JsonRuleDefinitionReader());
    Rules rules = groovyRuleFactory.createRules(new FileReader(file));

    Facts facts = new Facts();
    Request request =
        new Request(
            "FLIGHT", Lists.list("FL01", "FL02"), new BigDecimal(100), "IDR", LocalDateTime.now());
    facts.put("request", request);
    facts.put("log", log);
    for (Rule rule : rules) {
      boolean ruleEvaluationResult = rule.evaluate(facts);
      log.info("Rule [{}] matched?, {}", rule, ruleEvaluationResult);
    }
  }
}
