package cn.bootx.platform.common.sequence.impl;

import cn.bootx.platform.common.sequence.exception.SeqException;
import cn.bootx.platform.common.sequence.range.SeqRange;
import cn.bootx.platform.common.sequence.range.SeqRangeConfig;
import cn.bootx.platform.common.sequence.range.SeqRangeManager;
import cn.bootx.platform.common.sequence.func.Sequence;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 序列号区间生成器接口默认实现
 *
 * @author xxm
 * @since 2021/8/6
 */
@Setter
@Getter
@RequiredArgsConstructor
public class DefaultRangeSequence implements Sequence {

    /**
     * 获取区间是加一把重入锁防止资源冲突
     */
    private final Lock lock = new ReentrantLock();

    /**
     * 序列号区间管理器
     */
    private final SeqRangeManager seqRangeManager;

    /**
     * 当前序列号区间
     */
    private volatile SeqRange currentRange;

    /**
     * 序列号区间配置
     */
    private final SeqRangeConfig seqRangeConfig;

    /**
     * 需要获取区间的业务名称
     */
    private final String name;

    /**
     * 获取下一个值
     */
    @Override
    public long next() throws SeqException {
        // 当前区间不存在，重新获取一个区间
        if (null == currentRange) {
            lock.lock();
            try {
                if (null == currentRange) {
                    currentRange = seqRangeManager.nextRange(name, seqRangeConfig);
                }
            }
            finally {
                lock.unlock();
            }
        }

        // 当value值为-1时，表明区间的序列号已经分配完，需要重新获取区间
        long value = currentRange.getAndIncrement();
        if (value == -1) {
            lock.lock();
            try {
                while (true) {
                    if (currentRange.isOver()) {
                        currentRange = seqRangeManager.nextRange(name, seqRangeConfig);
                    }
                    value = currentRange.getAndIncrement();
                    if (value == -1) {
                        continue;
                    }
                    break;
                }
            }
            finally {
                lock.unlock();
            }
        }
        if (value < 0) {
            throw new SeqException("序列值溢出, value = " + value);
        }
        return value;
    }

}
