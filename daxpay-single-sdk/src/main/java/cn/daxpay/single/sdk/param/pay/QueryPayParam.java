package cn.daxpay.single.sdk.param.pay;

import cn.daxpay.single.sdk.model.pay.PayOrderModel;
import cn.daxpay.single.sdk.net.DaxPayRequest;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付单查询参数
 * @author xxm
 * @since 2024/1/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryPayParam extends DaxPayRequest<PayOrderModel> {

    /** 订单号 */
    private String orderNo;

    /** 商户订单号 */
    private String bizOrderNoeNo;

    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/query/payOrder";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<PayOrderModel> toModel(String json) {
        return JSONUtil.toBean(json, new TypeReference<DaxPayResult<PayOrderModel>>() {}, false);
    }
}
