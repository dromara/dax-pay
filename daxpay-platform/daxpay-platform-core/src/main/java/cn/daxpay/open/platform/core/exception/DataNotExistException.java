package cn.daxpay.open.platform.core.exception;

import cn.daxpay.open.platform.core.code.CommonErrorCode;

/// # 数据不存在异常
///
/// 固定使用 `CommonErrorCode.DATA_NOT_EXIST` 错误码，message 为 i18n key。
public class DataNotExistException extends BizInfoException {

    /// 使用默认数据不存在错误码与 messageKey
    public DataNotExistException(String messageKey) {
        super(CommonErrorCode.DATA_NOT_EXIST, messageKey);
    }

    /// 使用默认数据不存在错误码、messageKey 与占位符参数
    public DataNotExistException(String messageKey, Object... args) {
        super(CommonErrorCode.DATA_NOT_EXIST, messageKey, args);
    }

    /// 无参构造，使用通用 key `error.common.dataNotExist`
    public DataNotExistException() {
        super(CommonErrorCode.DATA_NOT_EXIST, "error.common.dataNotExist");
        initMessageKey("error.common.dataNotExist");
    }
}
