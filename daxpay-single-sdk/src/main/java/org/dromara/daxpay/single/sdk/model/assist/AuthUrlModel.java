package org.dromara.daxpay.single.sdk.model.assist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 获取OpenId授权链接和查询标识返回类
 * @author xxm
 * @since 2024/12/3
 */
@Data
public class AuthUrlModel {


    /** 授权访问链接 */
    @Schema(description = "授权访问链接")
    private String authUrl;

    /** 查询标识码, 用于查询是否获取到了OpenId */
    @Schema(description = "查询标识码")
    private String queryCode;
}
