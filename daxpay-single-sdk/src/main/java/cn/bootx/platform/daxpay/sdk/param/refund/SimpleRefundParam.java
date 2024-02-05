package cn.bootx.platform.daxpay.sdk.param.refund;

import cn.bootx.platform.daxpay.sdk.model.refund.RefundModel;
import cn.bootx.platform.daxpay.sdk.net.DaxPayRequest;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 简单退款参数，只可以用于非组合的支付订单
 * @author xxm
 * @since 2023/12/18
 */
@Getter
@Setter
@ToString
public class SimpleRefundParam extends DaxPayRequest<RefundModel> {

    /**
     * 支付单ID, 优先级高于业务号
     */
    private Long paymentId;

    /** 业务号 */
    private String businessNo;

    /**
     * 退款号可以为空, 但不可以重复, 如果退款号为空, 则系统会自动生成退款号, 与退款ID一致
     */
    private String refundNo;

    /**
     * 是否全部退款, 部分退款需要传输refundModes参数
     */
    private boolean refundAll;

    /** 退款金额 */
    private Integer amount;

    /** 退款原因 */
    private String reason;

    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/simpleRefund";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<RefundModel> toModel(String json) {
        return JSONUtil.toBean(json, new TypeReference<DaxPayResult<RefundModel>>() {}, false);
    }
}
