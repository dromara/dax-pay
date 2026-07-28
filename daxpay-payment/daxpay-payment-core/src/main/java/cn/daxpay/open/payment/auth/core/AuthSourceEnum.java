package cn.daxpay.open.payment.auth.core;

/// # 认证来源标识（平台级 Provider 路由键）
///
/// 标识本次认证使用的平台级配置来源, 由 [cn.daxpay.open.payment.auth.platform.PlatformAuthProvider]
/// 在 generateAuthUrl 时写入 [AuthSession#getSource], 认证分发层据此 O(1) 查找对应 Provider。
public enum AuthSourceEnum {
    /// 平台级支付宝配置(系统支付宝配置调试场景)
    PLATFORM_ALIPAY("platform_alipay"),
    /// 平台级微信公众号配置(系统公众号配置调试场景)
    PLATFORM_MP("platform_mp"),
    /// 平台级抖音 H5 应用配置(抖音支付调试场景)
    PLATFORM_DOUYIN("platform_douyin");

    private final String code;

    AuthSourceEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
