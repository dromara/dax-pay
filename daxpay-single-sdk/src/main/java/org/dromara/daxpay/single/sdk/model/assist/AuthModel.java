package org.dromara.daxpay.single.sdk.model.assist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.dromara.daxpay.single.sdk.code.ChannelAuthStatusEnum;

/**
 * 认证结果
 * @author xxm
 * @since 2024/12/3
 */
@Data
public class AuthModel {

    @Schema(description = "OpenId")
    private String openId;

    /**
     * 支付宝存量商户部分返回的是用户ID
     */
    @Schema(description = "用户ID")
    private String userId;

    /**
     * 微信会返回accessToken，用于获取用户信息
     */
    @Schema(description = "AccessToken")
    private String accessToken;

    /**
     * 状态
     * @see ChannelAuthStatusEnum
     */
    @Schema(description = "状态")
    private String status;
}
