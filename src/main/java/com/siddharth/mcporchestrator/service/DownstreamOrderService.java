package com.siddharth.mcporchestrator.service;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Map;
@Service
public class DownstreamOrderService {
  private final CircuitBreaker circuitBreaker;
  public DownstreamOrderService(){CircuitBreakerConfig config=CircuitBreakerConfig.custom().failureRateThreshold(50).minimumNumberOfCalls(5).slidingWindowSize(10).waitDurationInOpenState(Duration.ofSeconds(10)).build(); circuitBreaker=CircuitBreakerRegistry.of(config).circuitBreaker("order-service");}
  public Map<String,Object> getOrder(String orderId){return circuitBreaker.executeSupplier(()->simulatedLookup(orderId));}
  private Map<String,Object> simulatedLookup(String orderId){if(orderId==null||orderId.isBlank())throw new IllegalArgumentException("orderId is required"); return Map.of("orderId",orderId,"status","PROCESSING","source","mock-order-service");}
}
