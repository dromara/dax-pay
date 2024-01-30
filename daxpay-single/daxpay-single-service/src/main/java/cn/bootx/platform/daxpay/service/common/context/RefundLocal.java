package cn.bootx.platform.daxpay.service.common.context;

import cn.bootx.platform.daxpay.code.PayRefundStatusEnum;
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
     * 第三方支付网关生成的退款订单号, 用与将记录关联起来
     */
    private String gatewayOrderNo;

    /**
     * 退款状态, 默认为成功, 通常含有异步支付时, 才会出现别的状态
     */
    private PayRefundStatusEnum status = PayRefundStatusEnum.SUCCESS;

    /** 错误码 */
    private String errorCode;

    /** 错误内容 */
    private String errorMsg;
}
