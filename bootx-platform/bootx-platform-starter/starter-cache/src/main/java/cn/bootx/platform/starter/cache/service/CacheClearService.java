package cn.bootx.platform.starter.cache.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        prefixes.forEach(prefix->{
            deleteStringKeysWithPrefix(prefix,500);
        });
    }

    /**
     * 扫描删除前缀的缓存
     */
    private void deleteStringKeysWithPrefix(String prefix, int batchSize) {
        ScanOptions options = ScanOptions.scanOptions()
                .match(prefix + "*")
                .count(batchSize)
                .build();

        List<String> deleteKeys;
        try (Cursor<String> cursor = redisTemplate.scan(options)) {
            deleteKeys = new ArrayList<>();
            while (cursor.hasNext()) {
                deleteKeys.add(cursor.next());
                //
                if (deleteKeys.size() >= batchSize) {
                    redisTemplate.delete(deleteKeys);
                    deleteKeys.clear();
                }
            }
        }
        // 删除剩余的 key
        if (!deleteKeys.isEmpty()) {
            redisTemplate.delete(deleteKeys);
            deleteKeys.clear();
        }
    }
}
