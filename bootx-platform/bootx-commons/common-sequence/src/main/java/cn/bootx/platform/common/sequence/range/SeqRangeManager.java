package cn.bootx.platform.common.sequence.range;

import cn.bootx.platform.common.sequence.exception.SeqException;

/**
 * 区间管理器
 *
 * @author xuan on 2018/1/10.
 */
public interface SeqRangeManager {

    /**
     * 获取指定区间名的下一个区间
     * @param name 区间名
     * @param seqRangeConfig 序列号区间配置
     * @return 返回区间
     * @throws SeqException 异常
     */
    SeqRange nextRange(String name, SeqRangeConfig seqRangeConfig) throws SeqException;

}
