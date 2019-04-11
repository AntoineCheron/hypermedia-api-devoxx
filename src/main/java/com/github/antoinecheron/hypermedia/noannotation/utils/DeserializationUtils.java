package com.github.antoinecheron.hypermedia.noannotation.utils;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

public abstract class DeserializationUtils {

  public static String readTextualNode(String fieldName, JsonParser parser, JsonNode node) throws JsonMappingException {
    return getNode(node, fieldName)
        .filter(JsonNode::isTextual)
        .map(JsonNode::textValue)
        .orElseThrow(() -> new JsonMappingException(parser, "Missing textual '" + fieldName + "' property"));
  }

  public static Integer readIntNode(String fieldName, JsonParser parser, JsonNode node) throws JsonMappingException {
    return getNode(node, fieldName)
        .filter(JsonNode::isInt)
        .map(JsonNode::intValue)
        .orElseThrow(() -> new JsonMappingException(parser, "Missing int '" + fieldName + "' property"));
  }

  public static Double readDoubleNode(String fieldName, JsonParser parser, JsonNode node) throws JsonMappingException {
    return getNode(node, fieldName)
        .filter((field) -> field.isDouble() || field.isInt())
        .map(JsonNode::doubleValue)
        .orElseThrow(() -> new JsonMappingException(parser, "Missing double '" + fieldName + "' property"));
  }

  public static Optional<JsonNode> getNode(JsonNode node, String name) {
    return Optional.ofNullable(node.get(name));
  }

}
