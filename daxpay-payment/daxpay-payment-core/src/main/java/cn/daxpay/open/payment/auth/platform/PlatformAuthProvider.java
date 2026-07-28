package cn.daxpay.open.payment.auth.platform;

import cn.daxpay.open.payment.auth.core.AuthSourceEnum;
import cn.daxpay.open.payment.auth.core.AuthSession;
import cn.daxpay.open.payment.auth.develop.DevelopAuthService;
import cn.daxpay.open.payment.auth.ChannelAuthService;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.hutool.core.util.StrUtil;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.hutool.core.util.StrUtil;

/// # 平台级认证 Provider(策略)
///
/// 抽象平台级认证场景(支付宝 / 微信公众号 / 抖音 H5), 按 [AuthSession#getSource] 注册,
/// 供 [ChannelAuthService] 按会话来源 O(1) 查找。
///
/// ## 注册机制
/// 每个实现以 `@Component` 注册, 通过 [#sourceCode] 声明对应的 [AuthSession] source 常量。
/// 消费方([ChannelAuthService])注入 `List<PlatformAuthProvider>` 后按 sourceCode 建 Map 查找。
public interface PlatformAuthProvider {

    /// 认证来源标识
    ///
    /// 返回 [AuthSourceEnum] 枚举项, 由 [ChannelAuthService] 按 [.getCode] 索引 Provider。
    AuthSourceEnum sourceCode();

    /// 生成授权链接(平台级, 无商户上下文)
    ///
    /// @param returnPath 授权完成后前端回跳路径, 可空
    AuthUrlResult generateAuthUrl(String returnPath);

    /// 通过 authCode 换认证结果
    ///
    /// @param session 认证会话上下文(含 queryCode/returnPath); 可能为 null
    AuthResult auth(AuthCodeParam param, AuthSession session);

    /// 将会话中的 returnPath 回填到认证结果(平台级 Provider 共用)
    default void fillReturnPath(AuthResult authResult, AuthSession session) {
        if (session != null && StrUtil.isNotBlank(session.getReturnPath())) {
            authResult.setReturnPath(session.getReturnPath());
        }
    }
}
