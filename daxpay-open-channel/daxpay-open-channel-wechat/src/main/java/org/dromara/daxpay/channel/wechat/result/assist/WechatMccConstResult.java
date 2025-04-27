package org.dromara.daxpay.channel.wechat.result.assist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 微信经营类目
 * @author xxm
 * @since 2025/1/27
 */
@Data
@Accessors(chain = true)
@Schema(title = "微信经营类目")
public class WechatMccConstResult {

    /** 行业ID */
    @Schema(description = "行业ID")
    private String id;

    /** 行业名称 */
    @Schema(description = "行业名称")
    private String name;

    /** 主体类型 */
    @Schema(description = "主体类型")
    private String parentCode;

    /** 结算规则ID */
    @Schema(description = "结算规则ID")
    private String ruleId;

}
