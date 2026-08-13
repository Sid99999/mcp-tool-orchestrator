package com.siddharth.mcporchestrator.service;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
@Service
public class RateLimitService {
  private static final int LIMIT=60; private static final Duration WINDOW=Duration.ofMinutes(1); private final StringRedisTemplate redis;
  public RateLimitService(StringRedisTemplate redis){this.redis=redis;}
  public void check(String tenantId,String toolName){String key="mcp:rl:"+tenantId+":"+toolName; Long count=redis.opsForValue().increment(key); if(count!=null&&count==1L)redis.expire(key,WINDOW); if(count!=null&&count>LIMIT)throw new RateLimitExceededException("Rate limit exceeded for tenant="+tenantId+", tool="+toolName);}
  public static class RateLimitExceededException extends RuntimeException { public RateLimitExceededException(String message){super(message);} }
}
