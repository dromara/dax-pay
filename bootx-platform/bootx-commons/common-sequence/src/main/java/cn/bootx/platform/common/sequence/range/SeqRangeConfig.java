package cn.bootx.platform.common.sequence.range;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 序列号区间配置
 *
 * @author xxm
 * @since 2022/1/21
 */
@Data
@Accessors(chain = true)
public class SeqRangeConfig {

    /**
     * 序列生成器步长
     */
    private int step = 1;

    /**
     * 序列生成器区间步长
     */
    private int rangeStep = 1000;

    /**
     * 序列生成器区间起始位置
     */
    private long rangeStart = 0;

}
