package cn.bootx.platform.common.sequence.util;

import cn.bootx.platform.common.core.exception.FatalException;
import cn.bootx.platform.common.sequence.configuration.SequenceProperties;
import cn.bootx.platform.common.sequence.func.Sequence;
import cn.bootx.platform.common.sequence.impl.DefaultRangeSequence;
import cn.bootx.platform.common.sequence.range.SeqRangeConfig;
import cn.bootx.platform.common.sequence.range.SeqRangeManager;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;

import java.util.Objects;

/**
 * 序列号生成工具类
 *
 * @author xxm
 * @since 2022/12/27
 */
@UtilityClass
public class SequenceUtil {

    /**
     * 创建一个序列号生成器, 步长区间等树形见
     * @param name 序列生成器的Key
     */
    public Sequence create(String name) {
        SeqRangeManager rangeManager = SpringUtil.getBean(SeqRangeManager.class);
        SequenceProperties sequenceProperties = SpringUtil.getBean(SequenceProperties.class);
        if (Objects.isNull(rangeManager)) {
            throw new FatalException("序列号生成器创建失败，缺少区间管理器");
        }
        SeqRangeConfig seqRangeConfig = new SeqRangeConfig()
                .setStep(sequenceProperties.getStep())
                .setRangeStart(sequenceProperties.getRangeStep())
                .setRangeStep(sequenceProperties.getRangeStart());
        return new DefaultRangeSequence(rangeManager, seqRangeConfig, name);
    }

    /**
     * 创建一个自定义的序列号生成器
     * @param step 步长
     * @param rangeStep 区间步长
     * @param rangeStart 区间起始值
     * @param name 序列生成器的Key
     * @return 序列生成器
     */
    public Sequence create(int step, int rangeStep, int rangeStart, String name) {
        SeqRangeManager rangeManager = SpringUtil.getBean(SeqRangeManager.class);
        if (Objects.isNull(rangeManager)) {
            throw new FatalException("序列号生成器创建失败，缺少区间管理器");
        }
        SeqRangeConfig seqRangeConfig = new SeqRangeConfig()
                .setStep(step)
                .setRangeStart(rangeStep)
                .setRangeStep(rangeStart);
        return new DefaultRangeSequence(rangeManager, seqRangeConfig, name);
    }

}
