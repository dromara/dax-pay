package cn.daxpay.open.platform.system.result.config.infra;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台站点配置返回结果
///
@Data
@Accessors(chain = true)
@Schema(title = "平台站点配置")
public class PlatformWebsiteConfigResult {

    @Schema(description = "系统名称")
    private String systemName;

    @Schema(description = "公司全称")
    private String companyName;

    @Schema(description = "公司电话")
    private String companyPhone;

    @Schema(description = "公司邮箱")
    private String companyEmail;

    @Schema(description = "系统完整 logo")
    private String wholeLogo;

    @Schema(description = "系统简化 logo")
    private String simpleLogo;

    @Schema(description = "工信部 ICP 备案信息")
    private String icpInfo;

    @Schema(description = "工信部 ICP 链接地址")
    private String icpLink;

    @Schema(description = "公网安备案信息")
    private String mpsInfo;

    @Schema(description = "公网安备案链接地址")
    private String mpsLink;

    @Schema(description = "中国支付清算协会备案信息")
    private String pcacInfo;

    @Schema(description = "中国支付清算协会备案链接地址")
    private String pcacLink;

    @Schema(description = "电信增值业务许可信息")
    private String icpPlusInfo;

    @Schema(description = "电信增值业务许可链接地址")
    private String icpPlusLink;

    @Schema(description = "版权信息")
    private String copyright;

    @Schema(description = "版权信息链接")
    private String copyrightLink;
}
