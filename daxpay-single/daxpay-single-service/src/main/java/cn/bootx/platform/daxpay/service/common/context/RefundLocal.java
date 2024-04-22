package cn.bootx.platform.daxpay.service.common.context;

import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrderExtra;
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
    private String outRefundNo;

    /**
     * 退款状态, 默认为成功, 通常含有异步支付时, 才会出现别的状态
     */
    private RefundStatusEnum status = RefundStatusEnum.SUCCESS;

    /** 错误码 */
    private String errorCode;

    /** 错误内容 */
    private String errorMsg;

    /** 退款订单 */
    private RefundOrder refundOrder;

    /** 退款订单扩展 */
    private RefundOrderExtra runOrderExtra;
}
