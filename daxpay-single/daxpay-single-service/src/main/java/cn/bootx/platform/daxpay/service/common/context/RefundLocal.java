package cn.bootx.platform.daxpay.service.common.context;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 异步退款信息
 * @author xxm
 * @since 2023/12/24
 */
@Data
@Accessors(chain = true)
public class RefundLocal {

    /**
     *  异步通道退款时发给网关的退款号, 用与将记录关联起来
     */
    private String gatewayRequestNo;

    /** 错误码 */
    private String errorCode;

    /** 错误内容 */
    private String errorMsg;
}
