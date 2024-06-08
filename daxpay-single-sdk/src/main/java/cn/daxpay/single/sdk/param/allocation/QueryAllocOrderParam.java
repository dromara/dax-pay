package cn.daxpay.single.sdk.param.allocation;

import cn.daxpay.single.sdk.model.allocation.AllocOrderModel;
import cn.daxpay.single.sdk.net.DaxPayRequest;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 分账订单查询参数
 * @author xxm
 * @since 2024/6/3
 */
@Getter
@Setter
public class QueryAllocOrderParam extends DaxPayRequest<AllocOrderModel> {

    /** 分账单号 */
    private String allocNo;

    /** 商户分账单号 */
    private String bizAllocNo;

    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/query/allocationOrder";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<AllocOrderModel> toModel(String json) {
        return JSONUtil.toBean(json, new TypeReference<DaxPayResult<AllocOrderModel>>() {}, false);
    }
}
