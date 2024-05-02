package cn.bootx.platform.daxpay.sdk.param.allocation;

import cn.bootx.platform.daxpay.sdk.model.allocation.AllocationModel;
import cn.bootx.platform.daxpay.sdk.net.DaxPayRequest;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 分账请求接口(目前未支持)
 * @author xxm
 * @since 2024/2/7
 */
@Getter
@Setter
public class AllocationParam extends DaxPayRequest<AllocationModel> {

    /** 支付订单号 */
    private String orderNo;

    /** 商户订单号 */
    private String bizOrderNo;

    /** 商户分账单号(保证唯一) */
    private String bizAllocationNo;

    /** 分账描述 */
    private String description;

    /**
     * 分账组ID, 不传输分账组使用默认分账组进行分账
     */
    private Long allocationGroupId;

    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/allocation";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<AllocationModel> toModel(String json) {
        return JSONUtil.toBean(json, new TypeReference<DaxPayResult<AllocationModel>>() {}, false);
    }
}
