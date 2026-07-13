package cn.daxpay.open.platform.iam.enums;

import java.util.Arrays;
import java.util.Optional;

import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformUrlConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

/// # 社交登录终端编码
///
/// 用于按 client(admin/merchant) 解析端点配置中对应的前端 baseUrl.
/// 仅支持 admin / merchant, 不识别时由业务层拒绝(不再静默回退 admin).
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

    /// 根据编码查找, 无法识别返回 empty
    public static Optional<SocialClientEnum> findByCode(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }
        return Arrays.stream(values())
            .filter(e -> e.code.equalsIgnoreCase(code))
            .findFirst();
    }

    /// 根据编码查找枚举, 无法识别时回退到 ADMIN(仅兼容旧调用)
    public static SocialClientEnum of(String code) {
        return findByCode(code).orElse(ADMIN);
    }
}
