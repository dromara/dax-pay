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

    @Schema(description = "客服/商务微信号")
    private String companyWechat;

    @Schema(description = "系统亮色 logo")
    private String logo;

    @Schema(description = "系统暗色 logo, 不传则复用亮色")
    private String logoDark;

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

    /// 找回密码入口是否可用(只读, 不入库; 按邮件发件箱配置是否就绪计算, 供登录页隐藏找回密码入口)
    @Schema(description = "找回密码入口是否可用, 按邮件发件箱配置是否就绪计算")
    private Boolean forgetPasswordEnabled;

    /// 配置内容哈希(只读, 不入库; 供客户端缓存比对, 一致则跳过 re-apply)
    @Schema(description = "配置内容哈希, 供客户端缓存比对")
    private String contentHash;
}
