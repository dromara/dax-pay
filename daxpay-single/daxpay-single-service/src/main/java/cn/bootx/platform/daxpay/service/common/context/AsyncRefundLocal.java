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
public class AsyncRefundLocal {

    /** 退款请求号(调用支付网关时用的) */
    private String refundRequestNo;

    /** 错误码 */
    private String errorCode;

    /** 错误内容 */
    private String errorMsg;
}
