package org.dromara.daxpay.service.common.param.config;

import cn.bootx.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 站点显示内容配置
 * @author xxm
 * @since 2025/6/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "站点显示内容配置")
public class PlatformWebsiteConfigPram extends BaseResult {

    /** 系统名称 */
    @Schema(description = "系统名称")
    private String systemName;

    /** 公司全称 */
    @Schema(description = "公司全称")
    private String companyName;

    /** 公司电话 */
    @Schema(description = "公司电话")
    private String companyPhone;

    /** 公司邮箱 */
    @Schema(description = "公司邮箱")
    private String companyEmail;

    /** 系统完整logo */
    @Schema(description = "系统完整logo")
    private String wholeLogo;

    /** 系统简化Logo */
    @Schema(description = "系统简化Logo")
    private String simpleLogo;

    /** 工信部ICP备案信息 */
    @Schema(description = "工信部ICP备案信息")
    private String icpInfo;

    /** 工信部ICP链接地址 */
    @Schema(description = "工信部ICP链接地址")
    private String icpLink;

    /** 公网安备案信息 */
    @Schema(description = "公网安备案信息")
    private String mpsInfo;

    /** 公网安备案链接地址 */
    @Schema(description = "公网安备案链接地址")
    private String mpsLink;

    /** 中国支付清算协会备案信息 */
    @Schema(description = "中国支付清算协会备案信息")
    private String pcacInfo;

    /** 中国支付清算协会备案链接地址 */
    @Schema(description = "中国支付清算协会备案链接地址")
    private String pcacLink;

    /** 电信增值业务许可信息 */
    @Schema(description = "电信增值业务许可信息")
    private String icpPlusInfo;

    /** 电信增值业务许可链接地址 */
    @Schema(description = "电信增值业务许可链接地址")
    private String icpPlusLink;

    /** 版权信息 */
    @Schema(description = "版权信息")
    private String copyright;

    /** 版权信息链接 */
    @Schema(description = "版权信息链接")
    private String copyrightLink;
}
