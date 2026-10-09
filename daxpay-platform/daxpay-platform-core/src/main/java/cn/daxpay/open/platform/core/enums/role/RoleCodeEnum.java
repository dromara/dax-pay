package cn.daxpay.open.platform.core.enums.role;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 角色编码枚举
///
/// 开源版身份域仅 admin / merchant；内置角色与之对称。
@Getter
@RequiredArgsConstructor
public enum RoleCodeEnum implements I18nSupport {

    /// 运营管理员
    ADMIN_ADMIN("admin_admin"),
    /// 商户管理员
    MERCHANT_ADMIN("merchant_admin"),
    /// 商户普通用户(创建商户子用户时的默认角色, 授权面=仪表板+交易订单+回调通知)
    MERCHANT_USER("merchant_user");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.role_code";
    }

}
