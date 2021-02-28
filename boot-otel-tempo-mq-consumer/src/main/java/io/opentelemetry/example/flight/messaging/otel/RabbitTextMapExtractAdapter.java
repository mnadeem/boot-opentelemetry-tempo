package io.opentelemetry.example.flight.messaging.otel;

import io.opentelemetry.context.propagation.TextMapPropagator;
import java.util.Map;

public class RabbitTextMapExtractAdapter implements TextMapPropagator.Getter<Map<String, Object>> {

  public static final RabbitTextMapExtractAdapter GETTER = new RabbitTextMapExtractAdapter();

  @Override
  public Iterable<String> keys(Map<String, Object> carrier) {
    return carrier.keySet();
  }

  @Override
  public String get(Map<String, Object> carrier, String key) {
    Object obj = carrier.get(key);
    return obj == null ? null : obj.toString();
  }
}