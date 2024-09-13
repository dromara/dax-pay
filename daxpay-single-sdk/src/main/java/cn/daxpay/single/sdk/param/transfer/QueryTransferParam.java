package cn.daxpay.single.sdk.param.transfer;

import cn.daxpay.single.sdk.model.transfer.TransferOrderModel;
import cn.daxpay.single.sdk.net.DaxPayRequest;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
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
        return "/unipay/query/transferOrder";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<TransferOrderModel> toModel(String json) {
        return JSONUtil.toBean(json, new TypeReference<DaxPayResult<TransferOrderModel>>() {}, false);
    }
}
