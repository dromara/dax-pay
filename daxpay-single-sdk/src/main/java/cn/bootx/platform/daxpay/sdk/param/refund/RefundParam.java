package cn.bootx.platform.daxpay.sdk.param.refund;

import cn.bootx.platform.daxpay.sdk.model.RefundModel;
import cn.bootx.platform.daxpay.sdk.net.DaxPayRequest;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.*;

import java.util.List;

/**
 * 退款参数，适用于组合支付的订单退款操作中，
 * @author xxm
 * @since 2023/12/18
 */
@Getter
@Setter
@ToString
public class RefundParam extends DaxPayRequest<RefundModel> {

    /** 支付单ID */
    private Long paymentId;

    /** 业务号 */
    private String businessNo;

    /**
     * 部分退款需要传输支付通道参数参数
     */
    private boolean refundAll;

    /**
     * 退款号可以为空, 但不可以重复, 如果退款号为空, 则系统会自动生成退款号, 与退款ID一致
     */
    private String refundNo;

    /**
     * 部分退款时必传
     */
    private List<RefundChannelParam> refundChannels;

    /** 退款原因 */
    private String reason;

    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/refund";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<RefundModel> toModel(String json) {
        return JSONUtil.toBean(json, new TypeReference<DaxPayResult<RefundModel>>() {}, false);
    }
}
