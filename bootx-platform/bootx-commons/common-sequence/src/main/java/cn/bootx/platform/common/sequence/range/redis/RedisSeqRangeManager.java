package cn.bootx.platform.common.sequence.range.redis;

import cn.bootx.platform.common.redis.RedisClient;
import cn.bootx.platform.common.sequence.configuration.SequenceProperties;
import cn.bootx.platform.common.sequence.exception.SeqException;
import cn.bootx.platform.common.sequence.range.SeqRangeConfig;
import cn.bootx.platform.common.sequence.range.SeqRange;
import cn.bootx.platform.common.sequence.range.SeqRangeManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Redis区间管理器
 *
 * @author xxm
 * @since 2021/8/6
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "bootx.common.sequence", value = "type", havingValue = "redis")
public class RedisSeqRangeManager implements SeqRangeManager {

    private final RedisClient redisClient;

    private final SequenceProperties sequenceProperties;

    /**
     * 获取指定区间名的下一个区间
     * @param name 区间名
     * @param seqRangeConfig 序列号区间配置
     * @return 返回区间
     * @throws SeqException 异常
     */
    @Override
    public SeqRange nextRange(String name, SeqRangeConfig seqRangeConfig) throws SeqException {
        // 第一次不存在，进行初始化,不存在就set，存在就忽略
        redisClient.setIfAbsent(this.getRealKey(name), String.valueOf(seqRangeConfig.getRangeStart()));
        int rangeStep = seqRangeConfig.getRangeStep();
        Long max = redisClient.increment(this.getRealKey(name), rangeStep);
        long min = max - rangeStep;
        return new SeqRange(min, max, seqRangeConfig.getStep());
    }

    /**
     * 获取key
     */
    private String getRealKey(String name) {
        return sequenceProperties.getKeyPrefix() + name;
    }

}
