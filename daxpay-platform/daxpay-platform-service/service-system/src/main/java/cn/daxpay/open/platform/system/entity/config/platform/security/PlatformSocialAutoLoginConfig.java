package cn.daxpay.open.platform.system.entity.config.platform.security;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/// # 应用内社交自动登录配置
///
/// 按终端(admin/merchant)分别配置是否在飞书/微信/企微等内置浏览器中自动发起 OAuth 登录。
/// 凭据仍存放于 iam_social_login_config / 平台级公众号配置, 本配置仅存策略开关与可选平台列表.
///
@Data
@Accessors(chain = true)
public class PlatformSocialAutoLoginConfig {

    /// 运营端自动登录策略
    private ClientAutoLogin admin = new ClientAutoLogin();

    /// 商户端自动登录策略
    private ClientAutoLogin merchant = new ClientAutoLogin();

    /// 单端自动登录项
    @Data
    @Accessors(chain = true)
    public static class ClientAutoLogin {

        /// 是否启用应用内自动登录
        private Boolean enabled = Boolean.FALSE;

        /// 可自动跳转的社交平台编码列表(如 feishu / weChat / weCom), 登录时按 UA 匹配其一
        private List<String> sources = new ArrayList<>();

        /// 兼容旧版单字段 source; 反序列化后由 [normalize] 合并进 sources, 新写入不再持久化
        @Deprecated
        private String source;

        /// 归一化: 旧 source 迁移为 sources[], 去空并去重
        public ClientAutoLogin normalize() {
            List<String> resolved = new ArrayList<>();
            if (CollUtil.isNotEmpty(sources)) {
                for (String item : sources) {
                    if (StrUtil.isNotBlank(item) && !resolved.contains(item)) {
                        resolved.add(item);
                    }
                }
            } else if (StrUtil.isNotBlank(source)) {
                resolved.add(source);
            }
            this.sources = resolved;
            this.source = null;
            return this;
        }

        /// 对外读取有效平台列表(已 normalize 后可直接取 sources)
        @JsonIgnore
        public List<String> resolveSources() {
            this.normalize();
            return this.sources == null ? List.of() : this.sources;
        }
    }
}
