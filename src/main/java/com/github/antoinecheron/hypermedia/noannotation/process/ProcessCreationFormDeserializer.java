package com.github.antoinecheron.hypermedia.noannotation.process;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import com.github.antoinecheron.hypermedia.noannotation.utils.DeserializationUtils;

public class ProcessCreationFormDeserializer extends StdDeserializer<ProcessCreationForm> {

  public ProcessCreationFormDeserializer() {
    super(ProcessCreationForm.class);
  }

  @Override
  public ProcessCreationForm deserialize(JsonParser parser, DeserializationContext context) throws IOException {

    final JsonNode rootNode = parser.getCodec().readTree(parser);

    final String name = DeserializationUtils.Strings.required("name", parser, rootNode);
    final String createdBy = DeserializationUtils.Strings.required("createdBy", parser, rootNode);
    final String client = DeserializationUtils.Strings.required("client", parser, rootNode);
    final String advisor = DeserializationUtils.Strings.required("advisor", parser, rootNode);
    final String manager = DeserializationUtils.Strings.required("manager", parser, rootNode);
    final ProcessCategory category = ProcessCategory.valueOf(DeserializationUtils.Strings.required("category", parser, rootNode));
    final String description = DeserializationUtils.Strings.required("description", parser, rootNode);

    return new ProcessCreationForm(name, createdBy, client, advisor, manager, category, description);
  }

}
