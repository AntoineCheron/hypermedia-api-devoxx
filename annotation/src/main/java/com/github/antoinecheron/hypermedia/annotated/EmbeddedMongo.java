package com.github.antoinecheron.hypermedia.annotated;

import java.io.IOException;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmbeddedMongo {

  @Value("${db.host}") public String DB_HOST;
  @Value("${db.port}") public int DB_PORT;

  public void run() {
    MongodStarter starter = MongodStarter.getDefaultInstance();
    MongodExecutable mongodExecutable = null;

    try {
      IMongodConfig mongodConfig = new MongodConfigBuilder()
        .version(Version.Main.PRODUCTION)
        .net(new Net(DB_HOST, DB_PORT, Network.localhostIsIPv6()))
        .build();

      mongodExecutable = starter.prepare(mongodConfig);
      mongodExecutable.start();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (mongodExecutable != null)
        mongodExecutable.stop();
    }
  }

}
