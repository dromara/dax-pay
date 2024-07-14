package cn.bootx.platform.core.exception;


import cn.bootx.platform.core.code.CommonErrorCode;

/**
 * 数据不存在异常
 *
 * @author xxm
 * @since 2022/1/10
 */
public class DataNotExistException extends BizInfoException {

    public DataNotExistException(String msg) {
        super(CommonErrorCode.DATA_NOT_EXIST, msg);
    }

    public DataNotExistException() {
        super(CommonErrorCode.DATA_NOT_EXIST, "数据不存在");
    }

}
