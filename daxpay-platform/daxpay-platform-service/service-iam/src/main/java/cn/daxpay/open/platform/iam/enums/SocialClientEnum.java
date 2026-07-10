package cn.daxpay.open.platform.iam.enums;

import java.util.Arrays;

import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformUrlConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

/// # 社交登录终端编码
///
/// 用于按 client(admin/merchant) 解析端点配置中对应的前端 baseUrl.
/// 默认值 ADMIN: client 参数无法识别时回退到管理端配置.
///
@Getter
@AllArgsConstructor
public enum SocialClientEnum {

    /// 运营管理端
    ADMIN("admin") {
        @Override
        public String resolveBaseUrl(PlatformUrlConfig config) {
            return config.getAdminBaseUrl();
        }
    },

    /// 商户管理端
    MERCHANT("merchant") {
        @Override
        public String resolveBaseUrl(PlatformUrlConfig config) {
            return config.getMerchantBaseUrl();
        }
    };

    /// 终端编码
    private final String code;

    /// 从端点配置中解析当前终端对应的 baseUrl
    public abstract String resolveBaseUrl(PlatformUrlConfig config);

    /// 根据编码查找枚举, 无法识别时回退到 ADMIN(容错)
    public static SocialClientEnum of(String code) {
        return Arrays.stream(values())
            .filter(e -> e.code.equalsIgnoreCase(code))
            .findFirst()
            .orElse(ADMIN);
    }
}
