package org.dromara.daxpay.single.sdk.param.trade.pay;

import cn.hutool.core.lang.TypeReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.single.sdk.model.trade.pay.PaySyncModel;
import org.dromara.daxpay.single.sdk.net.DaxPayRequest;
import org.dromara.daxpay.single.sdk.response.DaxPayResult;
import org.dromara.daxpay.single.sdk.util.JsonUtil;

/**
 *支付同步参数
 * @author xxm
 * @since 2024/2/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class PaySyncParam extends DaxPayRequest<PaySyncModel> {

    /** 订单号 */
    private String orderNo;

    /** 商户订单号 */
    private String bizOrderNo;

    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/sync/order/pay";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<PaySyncModel> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<PaySyncModel>>() {});
    }
}
