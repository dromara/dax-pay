package cn.bootx.platform.common.sequence.exception;

import cn.bootx.platform.common.core.code.CommonCode;
import cn.bootx.platform.common.core.exception.FatalException;

import java.io.Serializable;

/**
 * 序列号生成异常
 *
 * @author xuan on 2018/1/10.
 */
public class SeqException extends FatalException implements Serializable {

    private static final long serialVersionUID = -5762184157486575048L;

    public SeqException(String message) {
        super(CommonCode.FAIL_CODE, message);
    }

}
