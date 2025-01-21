package org.dromara.daxpay.single.sdk.param.allocation;

import cn.hutool.core.lang.TypeReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.daxpay.single.sdk.model.allocation.AllocSyncModel;
import org.dromara.daxpay.single.sdk.net.DaxPayRequest;
import org.dromara.daxpay.single.sdk.response.DaxPayResult;
import org.dromara.daxpay.single.sdk.util.JsonUtil;

/**
 * 分账同步请求参数
 * @author xxm
 * @since 2024/4/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "分账同步请求参数")
public class AllocSyncParam extends DaxPayRequest<AllocSyncModel> {

    @Schema(description = "分账号")
    @Size(max = 32, message = "分账号不可超过32位")
    private String allocNo;

    @Schema(description = "商户分账号")
    @Size(max = 100, message = "商户分账号不可超过100位")
    private String bizAllocNo;

    /**
     * 方法请求路径
     *
     * @return 请求路径
     */
    @Override
    public String path() {
        return "/unipay/alloc/sync";
    }

    /**
     * 将请求返回结果反序列化为实体类
     *
     * @param json json字符串
     * @return 反序列后的对象
     */
    @Override
    public DaxPayResult<AllocSyncModel> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<AllocSyncModel>>() {});
    }
}
