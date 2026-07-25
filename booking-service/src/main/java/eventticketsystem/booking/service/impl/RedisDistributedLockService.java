package eventticketsystem.booking.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class RedisDistributedLockService {

    private final StringRedisTemplate redisTemplate;

    public RedisDistributedLockService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // LEARNING NOTE — TTL-only release risk:
    // This lock expires automatically via TTL — there is no explicit release.
    // Risk: if the critical section takes longer than the TTL, the lock expires
    // while still executing → a second request acquires the same lock → both
    // run concurrently → potential overselling.
    // Production fix: fencing token — store a unique value (e.g. UUID) on acquire,
    // and only delete the key if the stored value still matches yours.
    // We skip that here deliberately (breadth > depth), but the DB-level
    // pessimistic lock in TicketInventoryRepository provides a safety net.
    public boolean acquireLock(String key, Duration ttl){
        try {
            Boolean locked = redisTemplate.opsForValue().setIfAbsent(key, "locked", ttl);
            return Boolean.TRUE.equals(locked);
        } catch (RedisConnectionFailureException e){
            log.error("Redis is down");
            return false;
        }
    }
}
