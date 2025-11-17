package org.dromara.daxpay.payment.merchant.param.info;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商户状态信息查询参数
 * @author xxm
 * @since 2025/9/21
 */
@Data
@Accessors(chain = true)
@Schema(title = "商户状态信息查询参数")
public class MerchantStatusQuery {

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "商户名称")
    private String mchName;

    @Schema(description = "主体认证状态")
    private String profileAuth;

}
