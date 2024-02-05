package cn.bootx.platform.daxpay.sdk.model.refund;

import cn.bootx.platform.daxpay.sdk.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.sdk.net.DaxPayResponseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 退款响应参数
 * @author xxm
 * @since 2023/12/18
 */
@Getter
@Setter
@ToString
public class RefundModel extends DaxPayResponseModel {

    /** 退款ID */
    private Long refundId;

    /** 退款号, 如果请求时未传, 默认为退款ID */
    private String refundNo;

    /**
     * 退款状态
     * @see RefundStatusEnum
     */
    private String status;
}
