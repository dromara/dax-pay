package cn.daxpay.open.platform.iam.entity.social;

import cn.daxpay.open.platform.iam.convert.social.SocialConfigConvert;
import cn.daxpay.open.platform.iam.result.social.SocialConfigResult;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import cn.daxpay.open.platform.common.mybatisplus.handler.type.JsonbStringTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 第三方平台登录配置
///
/// 记录各社交平台(appId/appSecret 等)的配置, 全局唯一(按 source 区分), 管理端可动态维护.
/// 回调地址不再单独配置, 由端点配置(PlatformUrlConfig)的 baseUrl 自动生成: {baseUrl}/auth/oauth-callback/{source}.
/// 平台特有参数(如企业微信 agentId)统一存放在 extra jsonb 字段, 避免表结构随平台扩展频繁变更.
/// `configured` 标识是否已完成配置: 配置页内存合并时缺失项为 false, 用户保存配置后才为 true.
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "iam_social_config", autoResultMap = true)
public class SocialConfig extends MpBaseEntity implements ToResult<SocialConfigResult> {

    /// 平台编码
    /// @see SocialSourceEnum
    private String source;

    /// 客户端ID(appId/corpid)
    private String clientId;

    /// 客户端密钥(appSecret/corpsecret, 加密存储 AES-256-GCM)
    /// 编辑时未修改由前端不传字段(undefined) + 默认 NOT_NULL 策略跳过更新, 详见 Service.update
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String clientSecret;

    /// 平台特有配置(如企业微信 agentId), 以 jsonb 存储, 此处为原始 JSON 文本
    @TableField(typeHandler = JsonbStringTypeHandler.class)
    private String extra;

    /// 是否已配置(内存合并时缺失项为 false, 保存配置后为 true)
    private boolean configured;

    /// 是否启用
    private Boolean enabled;

    @Override
    public SocialConfigResult toResult() {
        return SocialConfigConvert.CONVERT.toResult(this);
    }
}
