package com.github.antoinecheron.hypermedia.notannotated.utils;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class MongoUtils {

  public static Query idQuery(String id) {
    return Query.query(Criteria.where("_id").is(id));
  }
}
