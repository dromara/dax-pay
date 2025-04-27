package cn.bootx.platform.starter.cache.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 缓存清除服务
 * @author xxm
 * @since 2025/4/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CacheClearService {
    private final RedisTemplate<String,?> redisTemplate;

    /**
     * 根据前缀清除缓存
     */
    public void clearCacheByPrefix(List<String> prefixes){
        prefixes.forEach(prefix->redisTemplate.delete(redisTemplate.keys(prefix + "*")));
    }
}
