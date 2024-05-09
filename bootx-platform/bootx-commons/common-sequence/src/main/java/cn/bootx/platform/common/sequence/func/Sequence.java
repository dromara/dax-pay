package cn.bootx.platform.common.sequence.func;

import cn.bootx.platform.common.sequence.exception.SeqException;

/**
 * 序列号生成器接口
 *
 * @author xuan on 2018/1/10.
 */
public interface Sequence {

    /**
     * 生成下一个序列号
     * @return 序列号
     * @throws SeqException 序列号异常
     */
    long next() throws SeqException;

}
