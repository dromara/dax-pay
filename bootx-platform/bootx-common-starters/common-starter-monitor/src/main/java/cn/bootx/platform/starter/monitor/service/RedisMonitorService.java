package cn.bootx.platform.starter.monitor.service;

import cn.bootx.platform.common.core.rest.dto.KeyValue;
import cn.bootx.platform.starter.monitor.entity.RedisMonitorResult;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Redis监控信息
 *
 * @author xxm
 * @since 2022/6/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisMonitorService {

    private final RedisTemplate<String, ?> redisTemplate;

    /**
     * 获取Redis服务信息
     */
    public RedisMonitorResult getRedisInfo() {
        // 系统信息
        Properties properties = redisTemplate.execute((RedisCallback<Properties>) RedisServerCommands::info);
        // 获取当前选定数据库中可用键的总数
        Long dbSize = redisTemplate.execute(RedisServerCommands::dbSize);
        // 命令统计
        Properties commandStats = Optional
            .ofNullable(
                    redisTemplate.execute((RedisCallback<Properties>) connection -> connection.info("commandstats")))
            .orElse(new Properties());
        List<KeyValue> keyValues = commandStats.stringPropertyNames().stream().map((key) -> {
            String value = commandStats.getProperty(key);
            return new KeyValue().setKey(StrUtil.removePrefix(key, "cmdstat_"))
                .setValue(StrUtil.subBetween(value, "calls=", ",usec"));
        }).collect(Collectors.toList());

        RedisMonitorResult result = new RedisMonitorResult();
        result.setInfo(properties);
        result.setDbSize(dbSize);
        result.setCommandStats(keyValues);
        return result;
    }

}
