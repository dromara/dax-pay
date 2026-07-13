package cn.daxpay.open.payment.auth;

import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.common.assist.MerchantContextLoader;
import cn.daxpay.open.payment.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.strategy.auth.AbsChannelAuthStrategy;
import cn.daxpay.open.payment.unipay.param.assist.AuthCodeParam;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthStatusEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 通道认证服务(商户级)
///
/// 负责按支付产品路由认证策略(继承 [AbsChannelAuthStrategy]), 依赖商户上下文定位通道应用,
/// 获取支付所需的用户标识(微信 openId / 支付宝 userId)。H5 授权重定向场景生成 authToken 保存会话。
///
/// **职责边界**: 本服务仅处理商户级通道认证; 平台级认证(平台支付宝配置 / 系统公众号配置)
/// 由 [PlatformAuthService] 承担, 会话与结果缓存由 [AuthSessionStore] 统一管理。
/// 平台级 vs 通道级 的来源分发由 [ChannelAuthFacade] 完成, 请勿在 Controller 再写分流。
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelAuthService {

    private final AuthSessionStore authSessionStore;
    private final MerchantContextLoader merchantContextLoader;
    private final ChannelMerchantManager channelMerchantManager;

    /// 获取通道授权链接
    ///
    /// 生成 authToken 并委托产品策略([AbsChannelAuthStrategy])生成授权 URL, 会话随 authToken 保存,
    /// 授权回调后凭此恢复上下文。同时生成 queryCode 供调试轮询(微信等 OAuth 重定向通道回调 URL
    /// 不含 queryCode, 需随会话保存)。
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param) {
        initMchContext(param.getAppId(), param.getMchNo());
        // 支付产品: 显式传入优先, 否则从通道商户号反查(调试工具/直传商户号场景)
        String product = resolveProduct(param);
        var strategy = PaymentStrategyFactory.createByProduct(product, AbsChannelAuthStrategy.class);
        // 生成认证会话码并保存上下文, 授权回调后凭此恢复
        String authToken = IdUtil.fastSimpleUUID();
        // 生成 queryCode 供调试轮询(微信等 OAuth 重定向通道回调 URL 不含 queryCode, 需随会话保存)
        String queryCode = RandomUtil.randomString(10);
        AuthSession session = new AuthSession()
                .setProduct(product)
                .setChannelMchNo(param.getChannelMchNo())
                .setCapability(param.getCapability())
                .setOpAppId(param.getOpAppId())
                .setReturnPath(param.getReturnPath())
                .setQueryCode(queryCode);
        authSessionStore.saveSession(authToken, session);
        AuthUrlResult authUrlResult = strategy.generateAuthUrl(param, authToken);
        // 回填 queryCode 并写入 WAITING 状态供前端轮询
        authUrlResult.setQueryCode(queryCode);
        authSessionStore.saveWaitingResult(queryCode);
        return authUrlResult;
    }

    /// 通过AuthCode获取通道认证结果
    ///
    /// @param session 认证会话上下文(H5场景从 authToken 恢复; 小程序直连场景可为空, 此时从 param 取上下文)。
    ///                由认证分发层在调用前通过 [AuthSessionStore.loadSession] 加载后注入。
    public AuthResult auth(AuthCodeParam param, AuthSession session) {
        initMchContext(param.getAppId(), param.getMchNo());
        // product 优先从会话恢复, 其次取参数(小程序直连场景)
        String product = (session != null && StrUtil.isNotBlank(session.getProduct()))
                ? session.getProduct() : param.getProduct();
        var strategy = PaymentStrategyFactory.createByProduct(product, AbsChannelAuthStrategy.class);
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

    /// 商户上下文初始化: appId 优先(渠道配置/小程序直连), appId 为空则用 mchNo(调试/直传商户号场景),
    /// 两者均空时跳过(微信 OAuth 重定向回调仅含 authToken, 商户上下文由 session.channelMchNo 维度定位, 无需线程级 mchNo)。
    private void initMchContext(String appId, String mchNo) {
        if (StrUtil.isNotBlank(appId)) {
            merchantContextLoader.initMchByApp(appId);
        } else if (StrUtil.isNotBlank(mchNo)) {
            merchantContextLoader.initMch(mchNo);
        }
    }

    /// 解析支付产品: 显式传入优先, 否则从通道商户号(channelMchNo)反查所属产品
    private String resolveProduct(GenerateAuthUrlParam param) {
        if (StrUtil.isNotBlank(param.getProduct())) {
            return param.getProduct();
        }
        // 缺失 product 时必须提供 channelMchNo 才能反查
        if (StrUtil.isBlank(param.getChannelMchNo())) {
            // 支付产品与通道商户号至少传其一
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.assist.productOrChannelMchRequired");
        }
        return channelMerchantManager.findByChannelMchNo(param.getChannelMchNo())
                .map(ChannelMerchant::getProduct)
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.assist.channelMchNotFound", param.getChannelMchNo()));
    }
}
