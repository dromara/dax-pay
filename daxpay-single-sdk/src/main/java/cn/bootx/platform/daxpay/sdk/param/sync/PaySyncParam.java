package cn.bootx.platform.daxpay.sdk.param.sync;

import cn.bootx.platform.daxpay.sdk.model.sync.PaySyncModel;
import cn.bootx.platform.daxpay.sdk.net.DaxPayRequest;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
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

    /** 支付ID */
    private Long paymentId;

    /** 业务号 */
    private String businessNo;

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
