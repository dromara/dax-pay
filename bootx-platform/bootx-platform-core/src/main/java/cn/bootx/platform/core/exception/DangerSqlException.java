package cn.bootx.platform.core.exception;


import cn.bootx.platform.core.code.CommonErrorCode;

/**
 * SQL相关异常
 *
 * @author xxm
 * @since 2023/3/9
 */
public class DangerSqlException extends BizException {

    public DangerSqlException(String msg) {
        super(CommonErrorCode.DANGER_SQL, msg);
    }

    public DangerSqlException() {
        super(CommonErrorCode.DANGER_SQL, "危险SQL异常");
    }

}
