package cn.bootx.platform.common.core.exception;

import static cn.bootx.platform.common.core.code.CommonErrorCode.DANGER_SQL;

/**
 * SQL相关异常
 *
 * @author xxm
 * @since 2023/3/9
 */
public class DangerSqlException extends BizException {

    public DangerSqlException(String msg) {
        super(DANGER_SQL, msg);
    }

    public DangerSqlException() {
        super(DANGER_SQL, "危险SQL异常");
    }

}
