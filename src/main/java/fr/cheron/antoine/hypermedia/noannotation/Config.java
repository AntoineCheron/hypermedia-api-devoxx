package fr.cheron.antoine.hypermedia.noannotation;

import com.typesafe.config.ConfigFactory;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class Config {

  public static final com.typesafe.config.Config configuration = ConfigFactory.defaultApplication().resolve();

  public static final String HOST = configuration.getString("http.host");
  public static final int PORT = configuration.getInt("http.port");

  public static final Scheduler APPLICATION_SCHEDULER = Schedulers.elastic();

  public static ReactiveRedisConnectionFactory getRedisConnectionFactory() {
    final var factory = new LettuceConnectionFactory(
        new RedisStandaloneConfiguration("localhost", 6379)
    );
    factory.afterPropertiesSet();
    return factory;
  }

}
