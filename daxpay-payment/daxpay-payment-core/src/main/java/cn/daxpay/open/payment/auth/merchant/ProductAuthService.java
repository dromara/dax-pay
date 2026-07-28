package cn.daxpay.open.payment.auth.merchant;

import cn.daxpay.open.payment.auth.core.AuthScene;
import cn.daxpay.open.payment.auth.core.AuthSession;
import cn.daxpay.open.payment.auth.core.AuthSessionStore;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthStatusEnum;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 通道认证服务(商户级)
///
/// 按 authType 路由认证策略([ChannelAuthStrategy])。应用解析的职责归入口层与策略层:
/// 入口层 resolve 选定微信应用后, 通过 GenerateAuthUrlParam.wxAppScope/wxAppRefId 标识;
/// 策略层(微信)据此查密钥, 抖音策略则自行解析。本服务不感知任何微信应用细节,
/// 仅负责会话生命周期与策略分发。
///
/// **职责边界**: 本服务仅处理商户级通道认证; 平台级认证(平台支付宝配置 / 系统公众号配置)
/// 由各 PlatformAuthProvider 承担, 会话与结果缓存由 [AuthSessionStore] 统一管理。
/// 平台级 vs 通道级 的来源分发由 [ChannelAuthService] 完成, 请勿在 Controller 再写分流。
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductAuthService {

    private final AuthSessionStore authSessionStore;
    private final MerchantContextLoader merchantContextLoader;
    private final ChannelAuthStrategyRegistry channelAuthStrategyRegistry;

    /// 获取通道授权链接
    ///
    /// 生成 authToken 并委托认证策略生成授权 URL; 策略负责写入回调恢复所需的应用引用到 session,
    /// 会话在策略执行后统一持久化(一次保存)。授权回调后凭 authToken 恢复上下文。
    /// 同时生成 queryCode 供调试轮询。
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param) {
        // 商户上下文初始化(mchNo 必填, appId 非必填传了则校验归属)
        initMchContext(param.getAppId(), param.getMchNo());
        // 按 authType 路由策略
        ChannelAuthStrategy strategy = channelAuthStrategyRegistry.findByAuthType(param.getAuthType());
        // 生成认证会话码, 授权回调后凭此恢复
        String authToken = IdUtil.fastSimpleUUID();
        // 生成 queryCode 供调试轮询(微信等 OAuth 重定向通道回调 URL 不含 queryCode, 需随会话保存)
        String queryCode = RandomUtil.randomString(10);
        AuthSession session = buildSession(param, queryCode);
        // 策略往 session 写入回调恢复所需的应用引用(微信写 wxAppScope/wxAppRefId), 执行后统一持久化
        AuthUrlResult authUrlResult = strategy.generateAuthUrl(param, authToken, session);
        authSessionStore.saveSession(authToken, session);
        // 回填 queryCode 并写入 WAITING 状态供前端轮询
        authUrlResult.setQueryCode(queryCode);
        authUrlResult.setAuthToken(authToken);
        authSessionStore.saveWaitingResult(queryCode);
        return authUrlResult;
    }

    /// 通过AuthCode获取通道认证结果
    ///
    /// 策略自行从 session 恢复应用凭证: 微信读 wxAppScope/wxAppRefId 调 getById 查密钥,
    /// 抖音读 channelMchNo 自行解析。
    ///
    /// @param session 认证会话上下文(H5场景从 authToken 恢复; 小程序直连场景可为空, 此时从 param 取上下文)。
    ///                由认证分发层在调用前通过 [AuthSessionStore#loadSession] 加载后注入。
    public AuthResult auth(AuthCodeParam param, AuthSession session) {
        // 商户上下文恢复: session.mchNo 优先, 否则用 param
        if (session != null && StrUtil.isNotBlank(session.getMchNo())) {
            merchantContextLoader.initMch(session.getMchNo());
        } else {
            initMchContext(param.getAppId(), param.getMchNo());
        }
        // authType 优先从会话恢复, 其次取参数(小程序直连场景)
        String authType = (session != null && StrUtil.isNotBlank(session.getAuthType()))
                ? session.getAuthType() : param.getAuthType();
        ChannelAuthStrategy strategy = channelAuthStrategyRegistry.findByAuthType(authType);
        // 策略自行从 session 恢复应用凭证(微信读 wxAppScope/wxAppRefId 查密钥; 抖音读 channelMchNo)
        AuthResult authResult = strategy.doAuth(param, session);
        authResult.setStatus(ChannelAuthStatusEnum.SUCCESS.getCode());
        // 会话恢复场景: 回填来源回跳路径, 供前端跳回业务页面
        if (session != null) {
            authResult.setReturnPath(session.getReturnPath());
        }
        // 写回轮询结果(微信等 OAuth 重定向通道从 session 恢复 queryCode)
        authSessionStore.writeResultByQueryCode(param.getQueryCode(), session, authResult);
        return authResult;
    }

    /// 从 param 构建通用会话(只通用字段; 微信应用引用由 WechatAuthStrategy 在 generateAuthUrl 时写入)
    private AuthSession buildSession(GenerateAuthUrlParam param, String queryCode) {
        return new AuthSession()
                .setMchNo(param.getMchNo())
                .setAuthType(param.getAuthType())
                .setChannelMchNo(param.getChannelMchNo())
                .setReturnPath(param.getReturnPath())
                .setQueryCode(queryCode)
                .setScene(AuthScene.PAYMENT.getCode());
    }

    /// 商户上下文初始化: mchNo 必填先初始化, appId 非必填传了则校验归属
    private void initMchContext(String appId, String mchNo) {
        // mchNo 必填, 先初始化商户上下文(含启用校验)
        merchantContextLoader.initMch(mchNo);
        // appId 非必填, 传了就解析并校验归属(resolveApp 内部校验 mchApp.mchNo == mchNo)
        if (StrUtil.isNotBlank(appId)) {
            merchantContextLoader.resolveApp(mchNo, appId);
        }
    }
}
