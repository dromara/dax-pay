package cn.daxpay.open.payment.auth.merchant;

import cn.daxpay.open.payment.auth.core.AuthScene;
import cn.daxpay.open.payment.auth.core.AuthSession;
import cn.daxpay.open.payment.auth.core.AuthSessionStore;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthStatusEnum;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import cn.daxpay.open.payment.auth.platform.PlatformAuthProvider;

/// # 支付产品认证服务(商户级)
///
/// 负责按支付产品路由认证策略(继承 [AbsProductAuthStrategy]), 依赖商户上下文定位通道应用,
/// 获取支付所需的用户标识(微信 openId / 支付宝 userId)。H5 授权重定向场景生成 authToken 保存会话。
///
/// **职责边界**: 本服务仅处理商户级通道认证; 平台级认证(平台支付宝配置 / 系统公众号配置)
/// 由各 [PlatformAuthProvider] 承担, 会话与结果缓存由 [AuthSessionStore] 统一管理。
/// 平台级 vs 通道级 的来源分发由 [ChannelAuthService] 完成, 请勿在 Controller 再写分流。
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductAuthService {

    private final AuthSessionStore authSessionStore;
    private final MerchantContextLoader merchantContextLoader;
    private final ChannelMerchantManager channelMerchantManager;

    /// 获取通道授权链接
    ///
    /// 生成 authToken 并委托产品策略([AbsProductAuthStrategy])生成授权 URL, 会话随 authToken 保存,
    /// 授权回调后凭此恢复上下文。同时生成 queryCode 供调试轮询(微信等 OAuth 重定向通道回调 URL
    /// 不含 queryCode, 需随会话保存)。
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param) {
        initMchContext(param.getAppId(), param.getMchNo(), null);
        // 支付方式缺失时按认证类型推导默认值(OAuth 重定向天然只适用于公众号场景)
        param.setMethod(resolveDefaultMethod(param.getAuthType(), param.getMethod()));
        // 支付产品: 显式传入优先, 否则从通道商户号反查(调试工具/直接指定通道商户场景)
        String product = resolveProduct(param);
        assertNotAlipayProduct(product);
        var strategy = PaymentStrategyFactory.createByProduct(product, AbsProductAuthStrategy.class);
        // 生成认证会话码并保存上下文, 授权回调后凭此恢复
        String authToken = IdUtil.fastSimpleUUID();
        // 生成 queryCode 供调试轮询(微信等 OAuth 重定向通道回调 URL 不含 queryCode, 需随会话保存)
        String queryCode = RandomUtil.randomString(10);
        AuthSession session = new AuthSession()
                .setProduct(product)
                .setChannelMchNo(param.getChannelMchNo())
                .setMethod(param.getMethod())
                .setChannelAppId(param.getChannelAppId())
                .setReturnPath(param.getReturnPath())
                .setQueryCode(queryCode)
                .setScene(AuthScene.PAYMENT.getCode());
        authSessionStore.saveSession(authToken, session);
        AuthUrlResult authUrlResult = strategy.generateAuthUrl(param, authToken);
        // 回填 queryCode 并写入 WAITING 状态供前端轮询
        authUrlResult.setQueryCode(queryCode);
        authUrlResult.setAuthToken(authToken);
        authSessionStore.saveWaitingResult(queryCode);
        return authUrlResult;
    }

    /// 通过AuthCode获取通道认证结果
    ///
    /// @param session 认证会话上下文(H5场景从 authToken 恢复; 小程序直连场景可为空, 此时从 param 取上下文)。
    ///                由认证分发层在调用前通过 [AuthSessionStore#loadSession] 加载后注入。
    public AuthResult auth(AuthCodeParam param, AuthSession session) {
        initMchContext(param.getAppId(), param.getMchNo(), session);
        // 支付方式缺失时按认证类型推导(session 恢复场景已有 method, 小程序直连场景需补)
        param.setMethod(resolveDefaultMethod(param.getAuthType(), param.getMethod()));
        // product 优先从会话恢复, 其次取参数(小程序直连场景)
        String product = (session != null && StrUtil.isNotBlank(session.getProduct()))
                ? session.getProduct() : param.getProduct();
        assertNotAlipayProduct(product);
        var strategy = PaymentStrategyFactory.createByProduct(product, AbsProductAuthStrategy.class);
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

    /// 商户上下文初始化: appId 优先(渠道配置/小程序直连), appId 为空则用 mchNo(调试/直接指定通道商户场景),
    /// 两者均空时从 session.channelMchNo 反查(OAuth 重定向回调场景, param 仅含 authToken)。
    /// 反查后调 initMch 把 mchNo 装载到 PaymentContext: 下游对 MchBaseEntity 表(如 wx_channel_app_capability)
    /// 的查询依赖线程级 mchNo, 否则 MchNoTenantLineHandler fail-closed 抛 mchContextMissing。
    private void initMchContext(String appId, String mchNo, AuthSession session) {
        if (StrUtil.isNotBlank(appId)) {
            merchantContextLoader.initMchByApp(appId);
        } else if (StrUtil.isNotBlank(mchNo)) {
            merchantContextLoader.initMch(mchNo);
        } else if (session != null && StrUtil.isNotBlank(session.getChannelMchNo())) {
            String resolvedMchNo = channelMerchantManager
                    .findByChannelMchNoNotTenant(session.getChannelMchNo())
                    .map(ChannelMerchant::getMchNo)
                    // 认证: 通道商户不存在
                    .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                            "pay.error.assist.channelMchNotFound", session.getChannelMchNo()));
            merchantContextLoader.initMch(resolvedMchNo);
        }
    }

    /// 解析支付产品: 显式传入优先, 否则从通道商户号(channelMchNo)反查所属产品
    private String resolveProduct(GenerateAuthUrlParam param) {
        if (StrUtil.isNotBlank(param.getProduct())) {
            return param.getProduct();
        }
        // 缺失 product 时必须提供 channelMchNo 才能反查
        if (StrUtil.isBlank(param.getChannelMchNo())) {
            // 认证: 支付产品与通道商户号至少传其一
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.assist.productOrChannelMchRequired");
        }
        return channelMerchantManager.findByChannelMchNo(param.getChannelMchNo())
                .map(ChannelMerchant::getProduct)
                // 认证: 通道商户不存在
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.assist.channelMchNotFound", param.getChannelMchNo()));
    }

    /// 支付宝认证统一走平台级 PlatformAuthProvider, 不应进入产品策略; 触发即编程错误。
    private void assertNotAlipayProduct(String product) {
        if (ProductEnum.ALIPAY.getCode().equals(product) || ProductEnum.ALIPAY_ISV.getCode().equals(product)) {
            throw new IllegalStateException("支付宝认证应走平台级 Provider, 不应进入产品策略: " + product);
        }
    }

    /// 支付方式缺失时按认证类型推导默认值
    ///
    /// OAuth 重定向认证天然只适用于公众号(H5网页授权)场景, 微信默认补 jsapi, 抖音默认补 jsapi。
    /// 已显式传入 method 的场景(网关/码牌)不受影响。
    private static String resolveDefaultMethod(String authType, String method) {
        if (StrUtil.isNotBlank(method)) {
            return method;
        }
        if (ChannelAuthTypeEnum.WECHAT.getCode().equals(authType)) {
            return PayMethodEnum.WECHAT_JSAPI.getCode();
        }
        if (ChannelAuthTypeEnum.DOUYIN.getCode().equals(authType)) {
            return PayMethodEnum.DOUYIN_JSAPI.getCode();
        }
        return method;
    }
}
