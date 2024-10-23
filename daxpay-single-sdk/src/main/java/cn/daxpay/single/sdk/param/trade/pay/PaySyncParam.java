package cn.daxpay.single.sdk.param.trade.pay;

import cn.daxpay.single.sdk.net.DaxPayRequest;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.daxpay.single.sdk.model.trade.pay.PaySyncModel;
import cn.daxpay.single.sdk.util.JsonUtil;
import cn.hutool.core.lang.TypeReference;
import lombok.Getter;
import lombok.Setter;

/**
 *支付同步参数
 * @author xxm
 * @since 2024/2/5
 */
@Getter
@Setter
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
