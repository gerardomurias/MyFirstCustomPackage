package com.automationanywhere.botcommand.demo;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;

import java.util.Map;

public class AttributeValueUtil {
  public static String getStringValue(Map<String, Value> attributes, String attributeName) {
    if (attributes != null) {
      if (!attributes.containsKey(attributeName)) {
        throw new BotCommandException(String.format("attribute map does not contain attribute '%s'", attributeName));
      }
      if (!(attributes.get(attributeName) instanceof StringValue)) {
        throw new BotCommandException(String.format("attribute '%s' type mismatch, expected StringValue, but found '%s'",
            attributeName, attributes.get(attributeName).get().getClass().getName()));
      }
      return (String) attributes.get(attributeName).get();
    } else {
      throw new BotCommandException("attributes map is null");
    }
  }
}
