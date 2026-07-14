package cn.daxpay.open.platform.system.entity.config.platform.infra;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台站点显示内容配置
///
/// 系统名称、公司信息、Logo、备案与版权等展示配置.
/// 全局唯一, 通过 [PlatformConfigTypeEnum.WEBSITE] 以 JSON 存于 `system_platform_config`.
///
@Data
@Accessors(chain = true)
public class PlatformWebsiteConfig {

    /// 系统名称
    private String systemName;

    /// 公司全称
    private String companyName;

    /// 公司电话
    private String companyPhone;

    /// 公司邮箱
    private String companyEmail;

    /// 系统完整 logo(公开文件 filename)
    private String wholeLogo;

    /// 系统简化 logo(公开文件 filename)
    private String simpleLogo;

    /// 工信部 ICP 备案信息
    private String icpInfo;

    /// 工信部 ICP 链接地址
    private String icpLink;

    /// 公网安备案信息
    private String mpsInfo;

    /// 公网安备案链接地址
    private String mpsLink;

    /// 中国支付清算协会备案信息
    private String pcacInfo;

    /// 中国支付清算协会备案链接地址
    private String pcacLink;

    /// 电信增值业务许可信息
    private String icpPlusInfo;

    /// 电信增值业务许可链接地址
    private String icpPlusLink;

    /// 版权信息
    private String copyright;

    /// 版权信息链接
    private String copyrightLink;
}
