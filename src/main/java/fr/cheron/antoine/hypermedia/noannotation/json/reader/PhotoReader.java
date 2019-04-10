package fr.cheron.antoine.hypermedia.noannotation.json.reader;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.cheron.antoine.hypermedia.noannotation.domain.Photo;

public class PhotoReader implements ApplicationReader<Photo> {

  public Optional<Photo> read(String value) {
    try {
      final ObjectMapper JSON = new ObjectMapper();
      final JsonNode node = JSON.readTree(value);

      return Optional.of(new Photo(
          node.get("id").asText(),
          new URL(node.get("url").asText())
      ));
    } catch (IOException e) {
      return Optional.empty();
    }
  }

}
