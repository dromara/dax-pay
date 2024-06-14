package cn.daxpay.single.service.common.context;

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
    private String errorCode;

    /** 错误内容 */
    private String errorMsg;
}
