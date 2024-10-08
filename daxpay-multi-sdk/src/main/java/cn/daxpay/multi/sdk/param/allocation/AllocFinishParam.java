package cn.daxpay.multi.sdk.param.allocation;

import cn.daxpay.multi.sdk.result.allocation.AllocResult;
import cn.daxpay.multi.sdk.net.DaxPayRequest;
import cn.daxpay.multi.sdk.response.DaxPayResult;
import cn.daxpay.multi.sdk.util.JsonUtil;
import cn.hutool.core.lang.TypeReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分账完结参数
 * @author xxm
 * @since 2024/4/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AllocFinishParam extends DaxPayRequest<AllocResult> {

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
    public DaxPayResult<AllocResult> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<AllocResult>>() {});
    }

}
