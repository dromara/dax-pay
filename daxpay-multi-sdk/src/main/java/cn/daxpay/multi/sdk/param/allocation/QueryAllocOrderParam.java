package cn.daxpay.multi.sdk.param.allocation;

import cn.daxpay.multi.sdk.result.allocation.AllocOrderModel;
import cn.daxpay.multi.sdk.net.DaxPayRequest;
import cn.daxpay.multi.sdk.response.DaxPayResult;
import cn.daxpay.multi.sdk.util.JsonUtil;
import cn.hutool.core.lang.TypeReference;
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
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<AllocOrderModel>>() {});
    }
}
