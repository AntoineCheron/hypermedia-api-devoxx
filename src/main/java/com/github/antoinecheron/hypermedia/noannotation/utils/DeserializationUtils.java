package com.github.antoinecheron.hypermedia.noannotation.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

public abstract class DeserializationUtils {

  public static class Strings {
    public static String readOrElse(String fieldName, String defaultValue, JsonNode node) {
      return optional(fieldName, node).orElse(defaultValue);
    }

    public static String readOrEmpty(String fieldName, JsonNode node) {
      return readOrElse(fieldName, "", node);
    }

    public static String required(String fieldName, JsonParser parser, JsonNode node) throws JsonMappingException {
      return optional(fieldName, node)
        .orElseThrow(() -> new JsonMappingException(parser, "Missing textual '" + fieldName + "' property"));
    }

    public static Optional<String> optional(String fieldName, JsonNode node) {
      return getNode(node, fieldName)
        .filter(JsonNode::isTextual)
        .map(JsonNode::textValue);
    }
  }

  public static class Integers {
    public static Integer required(String fieldName, JsonParser parser, JsonNode node) throws JsonMappingException {
      return optional(fieldName, node)
        .orElseThrow(() -> new JsonMappingException(parser, "Missing int '" + fieldName + "' property"));
    }

    public static Optional<Integer> optional(String fieldName, JsonNode node) {
      return getNode(node, fieldName)
        .filter(JsonNode::isInt)
        .map(JsonNode::intValue);
    }
  }

  public static class Doubles {
    public static Double required(String fieldName, JsonParser parser, JsonNode node) throws JsonMappingException {
      return optional(fieldName, node)
        .orElseThrow(() -> new JsonMappingException(parser, "Missing double '" + fieldName + "' property"));
    }

    public static Optional<Double> optional(String fieldName, JsonNode node) {
      return getNode(node, fieldName)
        .filter((field) -> field.isDouble() || field.isInt())
        .map(JsonNode::doubleValue);
    }
  }

  public static class Booleans {
    public static Boolean required(String fieldName, JsonParser parser, JsonNode node) throws JsonMappingException {
      return optional(fieldName, node)
        .orElseThrow(() -> new JsonMappingException(parser, "Missing boolean '" + fieldName + "' property"));
    }

    public static Optional<Boolean> optional(String fieldName, JsonNode node) {
      return getNode(node, fieldName)
        .filter(JsonNode::isBoolean)
        .map(JsonNode::asBoolean);
    }
  }

  public static class Collections {
    public static List<JsonNode> asList(String fieldName, JsonNode node) {
      return getNode(node, fieldName)
        .filter(JsonNode::isArray)
        .map((jsonArr) -> {
          final ArrayList<JsonNode> mutableList = new ArrayList<>();
          jsonArr.elements().forEachRemaining(mutableList::add);
          return List.copyOf(mutableList);
        })
        .orElse(List.of());
    }

    public static List<String> string(String fieldName, JsonNode node) {
      return asList(fieldName, node)
        .parallelStream()
        .filter(JsonNode::isTextual)
        .map(JsonNode::asText)
        .collect(Collectors.toList());
    }
  }

  public static class Maps {
    public static Map<String, String> asMapString(String fieldName, JsonNode node) {
      return asStream(fieldName, node)
        .filter(entry -> entry.getValue().isTextual())
        .collect(Collectors.toMap(
          Map.Entry::getKey,
          (entry) -> entry.getValue().asText()
        ));
    }

    private static Stream<Map.Entry<String, JsonNode>> asStream(String fieldName, JsonNode node) {
      return getNode(node, fieldName)
        .filter(JsonNode::isObject)
        .map((jsonObject) -> {
          final ArrayList<Map.Entry<String, JsonNode>> mutableList = new ArrayList<>();
          jsonObject.fields().forEachRemaining(mutableList::add);
          return List.copyOf(mutableList).stream();
        })
        .orElse(Stream.empty());
    }

  }

  public static class Dates {
    public static Date required(String fieldName, JsonParser parser, JsonNode node) throws JsonMappingException  {
      return optional(fieldName, node)
        .orElseThrow(() -> new JsonMappingException(parser, "Missing date '" + fieldName + "' property"));
    }

    public static Optional<Date> optional(String fieldName, JsonNode node) {
      return getNode(node, fieldName)
        .filter(JsonNode::isTextual)
        .map(JsonNode::asText)
        .flatMap(dateAsText -> {
          try {
            return Optional.of(DateFormat.getDateInstance().parse(dateAsText));
          } catch (ParseException e) {
            return Optional.empty();
          }
        });
    }
  }

  public static Optional<JsonNode> getNode(JsonNode node, String name) {
    return Optional.ofNullable(node.get(name));
  }

}
