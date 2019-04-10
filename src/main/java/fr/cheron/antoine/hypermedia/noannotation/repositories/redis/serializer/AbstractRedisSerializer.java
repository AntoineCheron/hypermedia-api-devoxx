package fr.cheron.antoine.hypermedia.noannotation.repositories.redis.serializer;

import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import fr.cheron.antoine.hypermedia.noannotation.json.reader.ApplicationReader;

public abstract class AbstractRedisSerializer<T> implements RedisSerializer<T> {

  private final ApplicationReader<T> reader;
  private final String genericTypeName;

  public AbstractRedisSerializer(ApplicationReader<T> reader) {
    this.reader = reader;
    this.genericTypeName = ((ParameterizedType) getClass()
        .getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
  }

  @Override
  public byte[] serialize(T input) throws SerializationException {
    final ObjectMapper JSON = new ObjectMapper();
    try {
      return JSON.writeValueAsBytes(input);
    } catch (JsonProcessingException e) {
      throw new SerializationException(e.getMessage());
    }
  }

  @Override
  public T deserialize(byte[] bytes) throws SerializationException {
    final var input = new String(bytes, StandardCharsets.UTF_8);
    return reader.read(input)
        .orElseThrow(() -> new SerializationException(
            "Cannot deserialize " + input + " into an instance of " + this.genericTypeName
        ));
  }

}
