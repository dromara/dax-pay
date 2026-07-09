package cn.daxpay.open.payment.merchant.param.wxverify;

import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信域名验证文件
///
@Data
@Accessors(chain = true)
@Schema(title = "微信域名验证文件")
public class WxDomainVerifyParam {

    /// 主键
    @Schema(description = "主键")
    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    private Long id;

    /// 备注
    @Schema(description = "备注")
    private String remark;

}
