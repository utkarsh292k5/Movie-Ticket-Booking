package com.ticketbooking.service;

import com.ticketbooking.dto.SeatAvailabilityDTO;
import com.ticketbooking.exception.SeatLockedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * CRITICAL SERVICE: Handles seat locking using Redis
 * Prevents double booking through distributed locks with TTL
 */
@Service
public class SeatLockService {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    // Lock duration: 5 minutes (300 seconds)
    private static final long LOCK_TTL_SECONDS = 300;
    
    /**
     * Lock seats for a user during booking process
     * Key format: seat-lock:{showId}:{seatId}
     * Value: userId (who locked it)
     * TTL: 5 minutes (auto-unlock)
     */
    public String lockSeats(Long showId, List<Long> seatIds, Long userId) {
        List<Long> lockedSeats = new ArrayList<>();
        String lockKey = UUID.randomUUID().toString();
        
        try {
            // Try to lock all requested seats
            for (Long seatId : seatIds) {
                String redisKey = buildSeatLockKey(showId, seatId);
                
                // SetIfAbsent = SETNX in Redis (atomic operation)
                Boolean locked = redisTemplate.opsForValue()
                        .setIfAbsent(redisKey, userId.toString(), LOCK_TTL_SECONDS, TimeUnit.SECONDS);
                
                if (Boolean.FALSE.equals(locked)) {
                    // Seat already locked by someone else
                    // Release all previously locked seats
                    unlockSeats(showId, lockedSeats);
                    throw new SeatLockedException("Seat " + seatId + " is already locked by another user");
                }
                
                lockedSeats.add(seatId);
            }
            
            // Store lock metadata for this session
            String lockMetadataKey = "lock-session:" + lockKey;
            redisTemplate.opsForValue().set(lockMetadataKey, 
                showId + ":" + String.join(",", seatIds.stream().map(String::valueOf).toArray(String[]::new)),
                LOCK_TTL_SECONDS, TimeUnit.SECONDS);
            
            return lockKey;
            
        } catch (Exception e) {
            // Release any locks if error occurs
            unlockSeats(showId, lockedSeats);
            throw e;
        }
    }
    
    /**
     * Unlock seats (on payment failure, timeout, or manual unlock)
     */
    public void unlockSeats(Long showId, List<Long> seatIds) {
        for (Long seatId : seatIds) {
            String redisKey = buildSeatLockKey(showId, seatId);
            redisTemplate.delete(redisKey);
        }
    }
    
    /**
     * Check if specific seat is locked
     */
    public boolean isSeatLocked(Long showId, Long seatId) {
        String redisKey = buildSeatLockKey(showId, seatId);
        return Boolean.TRUE.equals(redisTemplate.hasKey(redisKey));
    }
    
    /**
     * Get all locked seat IDs for a show
     */
    public List<Long> getLockedSeats(Long showId, List<Long> allSeatIds) {
        List<Long> lockedSeats = new ArrayList<>();
        for (Long seatId : allSeatIds) {
            if (isSeatLocked(showId, seatId)) {
                lockedSeats.add(seatId);
            }
        }
        return lockedSeats;
    }
    
    /**
     * Verify lock ownership before booking confirmation
     */
    public boolean verifyLockOwnership(Long showId, List<Long> seatIds, Long userId) {
        for (Long seatId : seatIds) {
            String redisKey = buildSeatLockKey(showId, seatId);
            String lockedBy = (String) redisTemplate.opsForValue().get(redisKey);
            
            if (lockedBy == null || !lockedBy.equals(userId.toString())) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Extend lock time (if user needs more time)
     */
    public void extendLock(Long showId, List<Long> seatIds) {
        for (Long seatId : seatIds) {
            String redisKey = buildSeatLockKey(showId, seatId);
            redisTemplate.expire(redisKey, LOCK_TTL_SECONDS, TimeUnit.SECONDS);
        }
    }
    
    private String buildSeatLockKey(Long showId, Long seatId) {
        return String.format("seat-lock:%d:%d", showId, seatId);
    }
}

