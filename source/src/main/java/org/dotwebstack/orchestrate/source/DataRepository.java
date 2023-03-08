package org.dotwebstack.orchestrate.source;

import java.util.Map;
import org.dotwebstack.orchestrate.model.ObjectType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DataRepository {

  Mono<Map<String, Object>> findOne(ObjectRequest objectRequest);

  Flux<Map<String, Object>> find(CollectionRequest collectionRequest);

  default Flux<Map<String, Object>> findBatch(BatchRequest batchRequest) {
    throw new SourceException("Batch loading not supported.");
  }

  default boolean supportsBatchLoading(ObjectType objectType) {
    return false;
  }
}
