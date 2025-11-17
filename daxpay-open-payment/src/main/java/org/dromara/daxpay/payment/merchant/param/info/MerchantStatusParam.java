package org.dromara.daxpay.payment.merchant.param.info;

import cn.bootx.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商户状态信息参数
 * @author xxm
 * @since 2025/9/19
 */
@Data
@Accessors(chain = true)
@Schema(title = "商户状态信息参数")
public class MerchantStatusParam {

    @Schema(description = "主键")
    @NotNull(message = "主键不能为空", groups = ValidationGroup.edit.class)
    private Long id;

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "主体认证")
    private String profileAuth;
}
