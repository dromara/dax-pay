package cn.daxpay.open.platform.system.param.config.infra;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台站点配置参数
///
/// 全部选填, 未配置的字段前端展示时回落默认品牌文案.
///
@Data
@Accessors(chain = true)
@Schema(title = "平台站点配置参数")
public class PlatformWebsiteConfigParam {

    @Size(max = 50, message = "{validation.field.systemName.size}")
    @Schema(description = "系统名称")
    private String systemName;

    @Size(max = 100, message = "{validation.field.companyName.size}")
    @Schema(description = "公司全称")
    private String companyName;

    @Schema(description = "公司电话")
    private String companyPhone;

    @Email(message = "{validation.field.companyEmail.email}")
    @Schema(description = "公司邮箱")
    private String companyEmail;

    @Size(max = 50, message = "{validation.field.companyWechat.size}")
    @Schema(description = "客服/商务微信号")
    private String companyWechat;

    @Schema(description = "系统亮色 logo")
    private String logo;

    @Schema(description = "系统暗色 logo, 不传则复用亮色")
    private String logoDark;

    @Schema(description = "工信部 ICP 备案信息")
    private String icpInfo;

    @Pattern(regexp = "^$|^https?://.+$", message = "{validation.field.icpLink.pattern}")
    @Schema(description = "工信部 ICP 链接地址")
    private String icpLink;

    @Schema(description = "公网安备案信息")
    private String mpsInfo;

    @Pattern(regexp = "^$|^https?://.+$", message = "{validation.field.mpsLink.pattern}")
    @Schema(description = "公网安备案链接地址")
    private String mpsLink;

    @Schema(description = "中国支付清算协会备案信息")
    private String pcacInfo;

    @Pattern(regexp = "^$|^https?://.+$", message = "{validation.field.pcacLink.pattern}")
    @Schema(description = "中国支付清算协会备案链接地址")
    private String pcacLink;

    @Schema(description = "电信增值业务许可信息")
    private String icpPlusInfo;

    @Pattern(regexp = "^$|^https?://.+$", message = "{validation.field.icpPlusLink.pattern}")
    @Schema(description = "电信增值业务许可链接地址")
    private String icpPlusLink;

    @Size(max = 200, message = "{validation.field.copyright.size}")
    @Schema(description = "版权信息")
    private String copyright;
}
