package cn.daxpay.multi.sdk.param.refund;

import cn.daxpay.multi.sdk.model.refund.RefundOrderModel;
import cn.daxpay.multi.sdk.net.DaxPayRequest;
import cn.daxpay.multi.sdk.response.DaxPayResult;
import cn.daxpay.multi.sdk.util.JsonUtil;
import cn.hutool.core.lang.TypeReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询退款订单参数类
 * @author xxm
 * @since 2024/1/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryRefundParam extends DaxPayRequest<RefundOrderModel> {


    /** 退款号 */
    private String refundNo;

    /** 商户退款号 */
    private String bizRefundNo;

    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/query/refundOrder";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<RefundOrderModel> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<RefundOrderModel>>() {});
    }
}
