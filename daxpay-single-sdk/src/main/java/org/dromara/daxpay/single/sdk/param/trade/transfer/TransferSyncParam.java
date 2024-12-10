package org.dromara.daxpay.single.sdk.param.trade.transfer;

import cn.hutool.core.lang.TypeReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.single.sdk.model.trade.transfer.TransferSyncModel;
import org.dromara.daxpay.single.sdk.net.DaxPayRequest;
import org.dromara.daxpay.single.sdk.response.DaxPayResult;
import org.dromara.daxpay.single.sdk.util.JsonUtil;

/**
 * 转账订单同步参数
 * @author xxm
 * @since 2024/2/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class TransferSyncParam extends DaxPayRequest<TransferSyncModel> {

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
    public DaxPayResult<TransferSyncModel> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<TransferSyncModel>>() {});
    }
}
