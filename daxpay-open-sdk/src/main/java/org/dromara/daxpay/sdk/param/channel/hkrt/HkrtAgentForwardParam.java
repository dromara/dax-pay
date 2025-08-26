package org.dromara.daxpay.sdk.param.channel.hkrt;

import org.dromara.daxpay.sdk.net.DaxPayAgentRequest;
import org.dromara.daxpay.sdk.response.DaxResult;
import org.dromara.daxpay.sdk.result.channel.hkrt.HkrtForwardResult;
import org.dromara.daxpay.sdk.util.JsonUtil;
import cn.hutool.core.lang.TypeReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * 海科接口转发参数
 * @author xxm
 * @since 2025/8/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "海科接口转发参数")
public class HkrtAgentForwardParam extends DaxPayAgentRequest<HkrtForwardResult> {
    @NotBlank(message = "接口路径不可为空")
    @Schema(description = "接口路径")
    private String method;

    @NotEmpty(message = "参数不可为空")
    @Schema(description = "参数")
    private Map<String,Object> param;

    @Override
    public String path() {
        return "/unipay/forward/hkrt/agent";
    }

    @Override
    public DaxResult<HkrtForwardResult> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxResult<HkrtForwardResult>>() {});
    }
}
