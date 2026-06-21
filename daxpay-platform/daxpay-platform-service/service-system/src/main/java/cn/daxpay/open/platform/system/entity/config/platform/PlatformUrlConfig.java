package cn.daxpay.open.platform.system.entity.config.platform;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台端点配置
///
/// 各端(管理端/商户端/支付网关/后端 API)的访问地址, 用于第三方登录回调 URL 自动生成、
/// 支付回调地址拼接等场景. 全局唯一, 通过 [PlatformConfigTypeEnum.URL] 存储于系统配置表.
///
/// getter 会自动去除 URL 尾部斜杠, 方便后续拼接路径.
///
@Data
@Accessors(chain = true)
public class PlatformUrlConfig {

    /// 管理端访问地址(如 https://admin.daxpay.com)
    /// 第三方登录回调等场景依赖此配置, client=admin 时使用
    private String adminBaseUrl;

    /// 商户端访问地址(如 https://merchant.daxpay.com)
    /// client=merchant 时使用
    private String merchantBaseUrl;

    /// 支付网关前端地址(如 https://pay.daxpay.com)
    /// 收银台/支付网关页面的访问地址
    private String paymentGatewayBaseUrl;

    /// 后端 API 地址(如 https://api.daxpay.com)
    /// 用于支付回调通知等后端发起的场景
    private String backendBaseUrl;

    /// 去除尾部斜杠, 方便后续拼接路径
    public String getAdminBaseUrl() {
        return StrUtil.removeSuffix(adminBaseUrl, "/");
    }

    /// 去除尾部斜杠, 方便后续拼接路径
    public String getMerchantBaseUrl() {
        return StrUtil.removeSuffix(merchantBaseUrl, "/");
    }

    /// 去除尾部斜杠, 方便后续拼接路径
    public String getPaymentGatewayBaseUrl() {
        return StrUtil.removeSuffix(paymentGatewayBaseUrl, "/");
    }

    /// 去除尾部斜杠, 方便后续拼接路径
    public String getBackendBaseUrl() {
        return StrUtil.removeSuffix(backendBaseUrl, "/");
    }
}
