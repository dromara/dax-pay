package cn.bootx.platform.daxpay.sdk.param.sync;

import cn.bootx.platform.daxpay.sdk.model.sync.RefundSyncModel;
import cn.bootx.platform.daxpay.sdk.net.DaxPayRequest;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 退款同步参数
 * @author xxm
 * @since 2024/2/7
 */
@Getter
@Setter
public class RefundSyncParam extends DaxPayRequest<RefundSyncModel> {

    /** 退款号 */
    private String refundNo;

    /** 商户退款号 */
    private String bizRefundNo;

    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/syncRefund";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<RefundSyncModel> toModel(String json) {
        return JSONUtil.toBean(json, new TypeReference<DaxPayResult<RefundSyncModel>>() {}, false);
    }
}
