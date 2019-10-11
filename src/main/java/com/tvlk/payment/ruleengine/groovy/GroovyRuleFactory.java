package com.tvlk.payment.ruleengine.groovy;

import com.tvlk.payment.ruleengine.core.UnitRuleGroupWithResult;
import com.tvlk.payment.ruleengine.model.rules.PaymentConfigRules;
import com.tvlk.payment.ruleengine.model.rules.RuleDetail;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.support.ActivationRuleGroup;
import org.jeasy.rules.support.CompositeRule;
import org.jeasy.rules.support.ConditionalRuleGroup;
import org.jeasy.rules.support.RuleDefinition;
import org.jeasy.rules.support.RuleDefinitionReader;
import org.jeasy.rules.support.UnitRuleGroup;

import java.io.Reader;
import java.util.Arrays;
import java.util.List;

public class GroovyRuleFactory {

  private RuleDefinitionReader reader;

  private static final List<String> ALLOWED_COMPOSITE_RULE_TYPES =
      Arrays.asList(
          UnitRuleGroup.class.getSimpleName(),
          ConditionalRuleGroup.class.getSimpleName(),
          ActivationRuleGroup.class.getSimpleName());

  public GroovyRuleFactory(RuleDefinitionReader reader) {
    this.reader = reader;
  }

  public Rule createRule(Reader ruleDescriptor) throws Exception {
    List<RuleDefinition> ruleDefinitions = reader.read(ruleDescriptor);
    if (ruleDefinitions.isEmpty()) {
      throw new IllegalArgumentException("rule descriptor is empty");
    }
    return createRule(ruleDefinitions.get(0));
  }

  /**
   * Create a set of {@link GroovyRule} from a Reader.
   *
   * @param rulesDescriptor as a Reader
   * @return a set of rules
   */
  public Rules createRules(Reader rulesDescriptor) throws Exception {
    Rules rules = new Rules();
    List<RuleDefinition> ruleDefinition = reader.read(rulesDescriptor);
    for (RuleDefinition groovyRuleDefinition : ruleDefinition) {
      rules.register(createRule(groovyRuleDefinition));
    }
    return rules;
  }

  public Rules createRules(PaymentConfigRules paymentConfigRules) {
    Rules rules = new Rules();
    rules.register(createCompositeRule(paymentConfigRules));
    return rules;
  }

  public Rules createRules(List<PaymentConfigRules> paymentConfigRulesList) {
    Rules rules = new Rules();
    for (PaymentConfigRules paymentConfigRules : paymentConfigRulesList) {
      rules.register(createCompositeRule(paymentConfigRules));
    }

    return rules;
  }

  private static Rule createRule(RuleDefinition ruleDefinition) {
    if (ruleDefinition.isCompositeRule()) {
      return createCompositeRule(ruleDefinition);
    } else {
      return createSimpleRule(ruleDefinition);
    }
  }

  private static Rule createSimpleRule(RuleDefinition ruleDefinition) {
    GroovyRule groovyRule =
        new GroovyRule()
            .name(ruleDefinition.getName())
            .description(ruleDefinition.getDescription())
            .priority(ruleDefinition.getPriority())
            .when(ruleDefinition.getCondition());
    for (String action : ruleDefinition.getActions()) {
      groovyRule.then(action);
    }
    return groovyRule;
  }

  private static Rule createCompositeRule(RuleDefinition ruleDefinition) {
    CompositeRule compositeRule;
    String name = ruleDefinition.getName();
    switch (ruleDefinition.getCompositeRuleType()) {
      case "UnitRuleGroup":
        compositeRule = new UnitRuleGroup(name);
        break;
      case "ActivationRuleGroup":
        compositeRule = new ActivationRuleGroup(name);
        break;
      case "ConditionalRuleGroup":
        compositeRule = new ConditionalRuleGroup(name);
        break;
      default:
        throw new IllegalArgumentException(
            "Invalid composite rule type, must be one of " + ALLOWED_COMPOSITE_RULE_TYPES);
    }
    compositeRule.setDescription(ruleDefinition.getDescription());
    compositeRule.setPriority(ruleDefinition.getPriority());

    for (RuleDefinition composingRuleDefinition : ruleDefinition.getComposingRules()) {
      compositeRule.addRule(createRule(composingRuleDefinition));
    }

    return compositeRule;
  }

  /**
   *
   * @param paymentConfigRules
   * @return
   */
  private static Rule createCompositeRule(PaymentConfigRules paymentConfigRules) {
    CompositeRule compositeRule = new UnitRuleGroupWithResult(paymentConfigRules.getId());
    compositeRule.setPriority(Integer.MAX_VALUE - paymentConfigRules.getPriority());
    for (RuleDetail ruleDetail : paymentConfigRules.getRuleDetails()) {
      compositeRule.addRule(ruleDetail.toGroovy());
    }
    return compositeRule;
  }
}
