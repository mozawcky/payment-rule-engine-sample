package com.tvlk.payment.ruleengine.model.configdetail;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfigDetailTree {
  ConfigDetail root;

  public ConfigDetail addNode(ConfigDetail node) {
    if (null == root) {
      root = node;
      return root;
    }
    return root.addNode(node);
  }
}
