package org.dromara.daxpay.single.sdk.param.allocation;

import cn.hutool.core.lang.TypeReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.single.sdk.model.allocation.AllocationModel;
import org.dromara.daxpay.single.sdk.net.DaxPayRequest;
import org.dromara.daxpay.single.sdk.response.DaxPayResult;
import org.dromara.daxpay.single.sdk.util.JsonUtil;

/**
 * 分账完结参数
 * @author xxm
 * @since 2024/12/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AllocFinishParam extends DaxPayRequest<AllocationModel> {


    @Schema(description = "商户分账单号")
    private String bizAllocNo;

    @Schema(description = "分账单号")
    private String allocNo;

    /**
     * 方法请求路径
     *
     * @return 请求路径
     */
    @Override
    public String path() {
        return "/unipay/alloc/finish";
    }

    /**
     * 将请求返回结果反序列化为实体类
     *
     * @param json json字符串
     * @return 反序列后的对象
     */
    @Override
    public DaxPayResult<AllocationModel> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<AllocationModel>>() {});
    }
}
