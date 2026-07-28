package cn.daxpay.open.payment.auth;

import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthStatusEnum;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import cn.daxpay.open.payment.auth.core.AuthSession;
import cn.daxpay.open.payment.auth.core.AuthSessionStore;
import cn.daxpay.open.payment.auth.core.AuthSourceEnum;
import cn.daxpay.open.payment.auth.channel.MerchantChannelAuthService;
import cn.daxpay.open.payment.auth.platform.PlatformAuthProvider;

/// # 统一认证服务(分发器)
///
/// 统一对外的「生成授权链接 / 授权码换用户标识」入口, 按会话来源([AuthSession#getSource])与
/// authType 分流到平台级 [PlatformAuthProvider](按 source 注册)或商户级服务([MerchantChannelAuthService])。
/// Controller / 调试入口只做协议适配, 不在 Web 层写业务分支。
///
/// ## 分发机制
/// 平台级认证 Provider 按 [PlatformAuthProvider#sourceCode] 注册到 `providers` Map, 查找 O(1)。
/// 新增平台级通道只需新增 [PlatformAuthProvider] 实现, 无需改本类。
///
/// ## 分发优先级(auth)
/// 1. `session.source` 命中 `providers` → 平台级 Provider 对应方法
/// 2. 无 session 且 `authType` 映射到平台级 source(支付宝小程序直连兜底)→ Provider
/// 3. 其余 → 商户级服务
///
/// ## 分发优先级(generateAuthUrl)
/// - `authType=alipay` → 平台级支付宝 Provider(无商户上下文)
/// - 其余 → 商户级服务(微信/抖音需商户上下文定位应用)
@Slf4j
@Service
public class UnifiedAuthService {

    private final AuthSessionStore authSessionStore;
    private final MerchantChannelAuthService merchantChannelAuthService;
    /// 平台级认证 Provider 按 [PlatformAuthProvider#sourceCode] 索引
    private final Map<String, PlatformAuthProvider> providers;

    public UnifiedAuthService(AuthSessionStore authSessionStore,
                              MerchantChannelAuthService merchantChannelAuthService,
                              List<PlatformAuthProvider> providerList) {
        this.authSessionStore = authSessionStore;
        this.merchantChannelAuthService = merchantChannelAuthService;
        // 按 sourceCode 索引, doAuth 据会话来源 O(1) 查找
        this.providers = providerList.stream()
                .collect(Collectors.toMap(p -> p.sourceCode().getCode(), Function.identity()));
    }

    /// 生成授权链接: 支付宝走平台级 Provider, 其余走商户级通道策略
    ///
    /// 微信应用档位标识(wxAppScope/wxAppRefId)随 param 透传, 由商户级策略消费; 支付宝分支不使用。
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param) {
        // 获取认证来源: 目前只有支付宝直接获取来源
        AuthSourceEnum source = mapAuthTypeToSource(param.getAuthType());
        if (source != null) {
            // 通常会是支付宝, 支付宝走平台级获取userId
            return providers.get(source.getCode()).generateAuthUrl(param.getReturnPath());
        }
        // 获取通道授权链接
        return merchantChannelAuthService.generateAuthUrl(param);
    }

    /// 通过 AuthCode 换取认证结果, 成功后销毁会话(一次使用)
    public AuthResult auth(AuthCodeParam param) {
        AuthSession session = authSessionStore.loadSession(param.getAuthToken());
        // 会话已失效(且非平台级认证的无 session 兜底场景): 提示重新生成, 避免下游抛"不支持的能力: null"
        // 平台级 authType(如 alipay)不依赖 session 字段, 由 doAuth 内的兜底分支处理
        if (session == null && mapAuthTypeToSource(param.getAuthType()) == null) {
            // 认证: 授权链接已失效请重新生成
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL,
                    "pay.error.assist.authSessionExpired");
        }
        AuthResult result;
        try {
            result = doAuth(param, session);
        } catch (RuntimeException e) {
            // 认证失败: 写回 FAIL 状态供前端轮询及时退出(避免死等 TTL), 再抛原异常由调用方处理
            authSessionStore.writeResultByQueryCode(param.getQueryCode(), session,
                    new AuthResult().setStatus(ChannelAuthStatusEnum.FAIL.getCode()));
            throw e;
        }
        // 平台级 auth 方法未回填 returnPath 时, 从会话补齐
        if (session != null && StrUtil.isNotBlank(session.getReturnPath())
                && StrUtil.isBlank(result.getReturnPath())) {
            result.setReturnPath(session.getReturnPath());
        }
        // 成功后失效 authToken, 避免 TTL 内重复消费会话上下文
        authSessionStore.deleteSession(param.getAuthToken());
        return result;
    }

    /// 按会话来源把授权码回调分到平台 Provider 或商户级服务处理
    private AuthResult doAuth(AuthCodeParam param, AuthSession session) {
        // session.source 标记的平台级认证, 按 source 查 Provider
        if (session != null && StrUtil.isNotBlank(session.getSource())) {
            PlatformAuthProvider provider = providers.get(session.getSource());
            if (provider != null) {
                return provider.auth(param, session);
            }
        }
        // 无 session 且 authType 对应平台级 Provider(如支付宝小程序直连): 兜底走 Provider
        if (session == null) {
            AuthSourceEnum source = mapAuthTypeToSource(param.getAuthType());
            if (source != null) {
                return providers.get(source.getCode()).auth(param, null);
            }
        }
        // 其余: 商户级服务
        return merchantChannelAuthService.auth(param, session);
    }

    /// authType → 平台级 Provider source 映射
    ///
    /// 仅支付宝走平台级 generate/auth(不依赖商户上下文); 微信/抖音 generate/auth 需商户上下文定位应用,
    /// 走 [MerchantChannelAuthService]。
    private static AuthSourceEnum mapAuthTypeToSource(String authType) {
        if (ChannelAuthTypeEnum.ALIPAY.getCode().equals(authType)) {
            return AuthSourceEnum.PLATFORM_ALIPAY;
        }
        return null;
    }
}
