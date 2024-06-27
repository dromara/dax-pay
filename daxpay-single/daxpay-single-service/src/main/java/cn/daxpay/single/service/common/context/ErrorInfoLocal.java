package cn.daxpay.single.service.common.context;

import cn.bootx.platform.common.core.exception.BizException;
import cn.daxpay.single.core.code.DaxPayErrorCode;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 错误信息
 * @author xxm
 * @since 2024/6/14
 */
@Data
@Accessors(chain = true)
public class ErrorInfoLocal {

    /** 错误码 */
    private int errorCode;

    /** 错误内容 */
    private String errorMsg;

    /**
     * 自动根据传入的异常对象对象进行处理
     */
    public void setException(Exception e) {
        // 如果业务异常, 获取错误码和错误信息
        if (e instanceof BizException) {
            BizException be = (BizException) e;
            this.errorCode = be.getCode();
            this.errorMsg = be.getMessage();
        }
        // 如果是空指针, 专门记录
        else if (e instanceof NullPointerException) {
            this.errorCode = DaxPayErrorCode.UNCLASSIFIED_ERROR;
            this.errorMsg = "空指针异常";
        }
        // 如果是其他异常, 归类到为止异常中
        else {
            this.errorCode = DaxPayErrorCode.SYSTEM_UNKNOWN_ERROR;
            this.errorMsg = "未归类异常，请联系管理人员进行排查";
        }
    }
}
