package org.dromara.daxpay.sdk.param.channel.leshua;

import org.dromara.daxpay.sdk.net.DaxPayAgentRequest;
import org.dromara.daxpay.sdk.response.DaxResult;
import org.dromara.daxpay.sdk.result.channel.leshua.LeshuaForwardResult;
import org.dromara.daxpay.sdk.util.JsonUtil;
import cn.hutool.core.lang.TypeReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 乐刷代理商接口转发参数
 * @author xxm
 * @since 2025/8/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "乐刷代理商接口转发参数")
public class LeshuaAgentForwardParam  extends DaxPayAgentRequest<LeshuaForwardResult> {
    @NotBlank(message = "接口路径不可为空")
    @Schema(description = "接口路径")
    private String method;

    @NotBlank(message = "参数不可为空")
    @Schema(description = "参数")
    private String param;

    @Override
    public String path() {
        return "/unipay/forward/leshua/agent";
    }

    @Override
    public DaxResult<LeshuaForwardResult> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxResult<LeshuaForwardResult>>() {});
    }
}
