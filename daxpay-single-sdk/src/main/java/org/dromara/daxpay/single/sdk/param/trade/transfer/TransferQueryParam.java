package org.dromara.daxpay.single.sdk.param.trade.transfer;

import org.dromara.daxpay.single.sdk.net.DaxPayRequest;
import org.dromara.daxpay.single.sdk.response.DaxPayResult;
import org.dromara.daxpay.single.sdk.model.trade.transfer.TransferOrderModel;
import org.dromara.daxpay.single.sdk.util.JsonUtil;
import cn.hutool.core.lang.TypeReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author xxm
 * @since 2024/6/20
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TransferQueryParam extends DaxPayRequest<TransferOrderModel> {

    /** 商户转账号 */
    private String bizTransferNo;

    /** 转账号 */
    private String transferNo;

    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/query/transfer";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<TransferOrderModel> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<TransferOrderModel>>() {});
    }
}
