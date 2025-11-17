package org.dromara.daxpay.sdk.result.channel.vbill;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * 随行付接口转发返回结果
 * @author xxm
 * @since 2025/8/24
 */
@Data
@Accessors(chain = true)
@Schema(title = "随行付接口转发返回结果")
public class VbillForwardResult {

    @Schema(description = "是否成功")
    private boolean success;

    @Schema(description = "错误信息")
    private String errorMsg;

    @Schema(description = "返回结果")
    private Map<String, Object> map;
}
