package org.dromara.daxpay.single.sdk.param.trade.pay;

import lombok.experimental.Accessors;
import org.dromara.daxpay.single.sdk.net.DaxPayRequest;
import org.dromara.daxpay.single.sdk.response.DaxPayResult;
import org.dromara.daxpay.single.sdk.model.trade.pay.PayOrderModel;
import org.dromara.daxpay.single.sdk.util.JsonUtil;
import cn.hutool.core.lang.TypeReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付单查询参数
 * @author xxm
 * @since 2024/1/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class PayQueryParam extends DaxPayRequest<PayOrderModel> {

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
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<PayOrderModel>>() {});
    }
}
