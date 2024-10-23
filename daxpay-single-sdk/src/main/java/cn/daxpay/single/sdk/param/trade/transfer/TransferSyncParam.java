package cn.daxpay.single.sdk.param.trade.transfer;

import cn.daxpay.single.sdk.model.trade.refund.RefundSyncModel;
import cn.daxpay.single.sdk.net.DaxPayRequest;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.daxpay.single.sdk.util.JsonUtil;
import cn.hutool.core.lang.TypeReference;
import lombok.Getter;
import lombok.Setter;

/**
 * 转账订单同步参数
 * @author xxm
 * @since 2024/2/7
 */
@Getter
@Setter
public class TransferSyncParam extends DaxPayRequest<RefundSyncModel> {

    /** 商户转账号 */
    private String bizTransferNo;

    /** 转账号 */
    private String transferNo;

    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/sync/order/transfer";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<RefundSyncModel> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<RefundSyncModel>>() {});
    }
}
