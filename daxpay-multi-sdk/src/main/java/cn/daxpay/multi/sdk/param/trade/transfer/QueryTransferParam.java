package cn.daxpay.multi.sdk.param.trade.transfer;

import cn.daxpay.multi.sdk.result.trade.transfer.TransferOrderModel;
import cn.daxpay.multi.sdk.net.DaxPayRequest;
import cn.daxpay.multi.sdk.response.DaxPayResult;
import cn.daxpay.multi.sdk.util.JsonUtil;
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
public class QueryTransferParam extends DaxPayRequest<TransferOrderModel> {

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
