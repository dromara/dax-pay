package cn.bootx.platform.common.core.exception;

import java.io.Serializable;

import static cn.bootx.platform.common.core.code.CommonErrorCode.DATA_OUT_OF_DATE;

/**
 * 乐观锁异常
 *
 * @author xxm
 * @since 2020/4/15 14:11
 */
public class OptimisticLockException extends SystemException implements Serializable {

    private static final long serialVersionUID = -1605410024618499135L;

    public OptimisticLockException() {
        super(DATA_OUT_OF_DATE, "数据不存在或者已过期");
    }

}
