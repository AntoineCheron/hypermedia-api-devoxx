package com.github.antoinecheron.hypermedia.noannotation.json.reader;

import java.util.Optional;

public interface ApplicationReader<T> {

  Optional<T> read(String input);

}
