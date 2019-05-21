package com.github.antoinecheron.hypermedia.annotated.process;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.antoinecheron.hypermedia.annotated.utils.DeserializationUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

public class ProcessDeserializer extends StdDeserializer<Process> {

  public ProcessDeserializer() {
    super(Process.class);
  }

  @Override
  public Process deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    final JsonNode rootNode = parser.getCodec().readTree(parser);

    final var processCreationForm = rootNode.traverse(parser.getCodec()).readValueAs(ProcessCreationForm.class);
    final var id = DeserializationUtils.Strings.required("id", parser, rootNode);
    final Integer step = DeserializationUtils.Integers.required("step", parser, rootNode);
    final ProcessState state = ProcessState.valueOf(DeserializationUtils.Strings.required("state", parser, rootNode));
    final Optional<Date> startDate = DeserializationUtils.Dates.optional("startDate", rootNode);
    final Date lastUpdate = DeserializationUtils.Dates.required("lastUpdate", parser, rootNode);
    final Optional<Date> endDate = DeserializationUtils.Dates.optional("endDate", rootNode);

    return Process.fromProcessCreationForm(
      processCreationForm, id, step, state, startDate, lastUpdate, endDate);
  }

}
