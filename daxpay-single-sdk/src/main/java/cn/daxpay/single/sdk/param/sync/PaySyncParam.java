package cn.daxpay.single.sdk.param.sync;

import cn.daxpay.single.sdk.model.sync.PaySyncModel;
import cn.daxpay.single.sdk.net.DaxPayRequest;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
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
        return "/unipay/syncPay";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<PaySyncModel> toModel(String json) {
        return JSONUtil.toBean(json, new TypeReference<DaxPayResult<PaySyncModel>>() {}, false);
    }
}
