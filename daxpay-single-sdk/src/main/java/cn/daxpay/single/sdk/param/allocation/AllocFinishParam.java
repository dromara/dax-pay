package cn.daxpay.single.sdk.param.allocation;

import cn.daxpay.single.sdk.model.allocation.AllocationModel;
import cn.daxpay.single.sdk.net.DaxPayRequest;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分账完结参数
 * @author xxm
 * @since 2024/4/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AllocFinishParam extends DaxPayRequest<AllocationModel> {

    /** 分账单号 */
    private String allocNo;

    /** 商户分账单号 */
    private String bizAllocNo;

    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/allocation/finish";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<AllocationModel> toModel(String json) {
        return JSONUtil.toBean(json, new TypeReference<DaxPayResult<AllocationModel>>() {}, false);
    }

}
