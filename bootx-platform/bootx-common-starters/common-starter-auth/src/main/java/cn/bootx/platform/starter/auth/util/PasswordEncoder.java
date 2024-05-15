package cn.bootx.platform.starter.auth.util;

import cn.bootx.platform.starter.auth.configuration.AuthProperties;
import cn.dev33.satoken.secure.SaSecureUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 密码编码器
 *
 * @author xxm
 * @since 2021/7/30
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordEncoder {

    private final AuthProperties authProperties;

    /**
     * 对原始密码进行编码。 通常，好的编码算法应用 SHA-1 或更大的哈希值与 8 字节或更大的随机生成的盐值相结合。
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public String encode(String rawPassword) {
        return SaSecureUtil.md5BySalt(rawPassword, authProperties.getSalt());
    }

    /**
     * 验证从存储中获取的编码密码是否与提交的原始密码匹配后也被编码。 如果密码匹配，则返回 true，否则返回 false。 存储的密码本身永远不会被解码。 参数：
     * @param rawPassword – 编码和匹配的原始密码
     * @param encodedPassword —来自存储的编码密码，用于比较
     * @return 如果原始密码在编码后与存储中的编码密码匹配，则为 true
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        return StringUtils.equals(encodedPassword, encode(rawPassword));
    }

    /**
     * 获取默认密码
     */
    public String defaultPassword() {
        return SaSecureUtil.md5BySalt(authProperties.getDefaultPassword(), authProperties.getSalt());
    }

}
