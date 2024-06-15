package cn.daxpay.single.service.dto.extra;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 微信OpenId查询结果
 * @author xxm
 * @since 2024/6/15
 */
@Data
@Accessors(chain = true)
@Schema(title = "微信OpenId查询结果")
public class WeChatOpenIdResult {

    @Schema(description = "OpenId")
    private String openId;

    /**
     * 状态
     * pending:查询中
     * success:查询成功
     * fail:查询失败
     */
    @Schema(description = "状态")
    private String status;
}
