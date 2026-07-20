package cn.daxpay.open.payment.auth;

import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 通道认证分发门面
///
/// 统一对外的「生成授权链接 / 授权码换用户标识」入口, 按会话来源与 authType 分流到
/// [PlatformAuthService](平台级配置) 或 [ChannelAuthService](商户级产品策略)。
/// Controller / 调试入口只做协议适配, 不在 Web 层写业务分支。
///
/// ## 分发优先级(auth)
/// 1. session.source = platform_alipay / platform_mp / platform_douyin → 平台服务对应方法
/// 2. authType=alipay 且无 session → 平台支付宝(小程序等直连兜底)
/// 3. 其余 → 通道策略
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelAuthFacade {

    private final AuthSessionStore authSessionStore;
    private final PlatformAuthService platformAuthService;
    private final ChannelAuthService channelAuthService;

    /// 生成授权链接: 支付宝走平台级 OAuth, 其余按支付产品走通道策略
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param) {
        if (isAlipayAuth(param.getAuthType())) {
            // 透传 returnPath, 供码牌等业务页授权完成后回跳
            return platformAuthService.generateAlipayAuthUrl(param.getReturnPath());
        }
        return channelAuthService.generateAuthUrl(param);
    }

    /// 通过 AuthCode 换取认证结果, 成功后销毁会话(一次使用)
    public AuthResult auth(AuthCodeParam param) {
        AuthSession session = authSessionStore.loadSession(param.getAuthToken());
        // 会话已失效(且非支付宝直连兜底场景): 提示重新生成, 避免下游抛"不支持的能力: null"
        // 支付宝平台级 OAuth 不依赖 session 字段, 由 doAuth 内的兜底分支处理, 保持原行为
        if (session == null && !isAlipayAuth(param.getAuthType())) {
            // 授权链接已失效, 请重新生成
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL,
                    "pay.error.assist.authSessionExpired");
        }
        AuthResult result = doAuth(param, session);
        // 平台级 auth 方法未回填 returnPath 时, 从会话补齐
        if (session != null && StrUtil.isNotBlank(session.getReturnPath())
                && StrUtil.isBlank(result.getReturnPath())) {
            result.setReturnPath(session.getReturnPath());
        }
        // 成功后失效 authToken, 避免 TTL 内重复消费会话上下文
        authSessionStore.deleteSession(param.getAuthToken());
        return result;
    }

    /// 按会话来源把授权码回调分到平台或通道处理
    private AuthResult doAuth(AuthCodeParam param, AuthSession session) {
        if (isPlatformAlipay(session)) {
            return platformAuthService.authAlipay(param, session);
        }
        if (isPlatformMp(session)) {
            return platformAuthService.authWechatMp(param, session);
        }
        if (isPlatformDouyin(session)) {
            return platformAuthService.authDouyin(param, session);
        }
        // 无会话且 authType=alipay: 小程序等直连场景兜底
        if (isAlipayAuth(param.getAuthType()) && session == null) {
            return platformAuthService.authAlipay(param, null);
        }
        return channelAuthService.auth(param, session);
    }

    /// 是否支付宝认证类型(平台级支付宝走 OAuth)
    private boolean isAlipayAuth(String authType) {
        return Objects.equals(authType, ChannelAuthTypeEnum.ALIPAY.getCode());
    }

    /// 是否支付宝平台级配置来源
    private boolean isPlatformAlipay(AuthSession session) {
        return session != null && AuthSession.SOURCE_PLATFORM_ALIPAY.equals(session.getSource());
    }

    /// 是否微信系统公众号配置来源(平台级)
    private boolean isPlatformMp(AuthSession session) {
        return session != null && AuthSession.SOURCE_PLATFORM_MP.equals(session.getSource());
    }

    /// 是否抖音 H5 应用配置来源(平台级)
    private boolean isPlatformDouyin(AuthSession session) {
        return session != null && AuthSession.SOURCE_PLATFORM_DOUYIN.equals(session.getSource());
    }
}
