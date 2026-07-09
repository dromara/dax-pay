package cn.daxpay.open.channel.alipay.service.isv;

import cn.daxpay.open.channel.alipay.client.AlipayChannelClient;
import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.client.req.AlipayAppAuthTokenReq;
import cn.daxpay.open.channel.alipay.client.resp.AlipayAppAuthTokenResp;
import cn.daxpay.open.channel.alipay.dao.isv.AlipayIsvAppManager;
import cn.daxpay.open.channel.alipay.dao.isv.AlipayIsvChannelMerchantManager;
import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvApp;
import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvAppKeyConfig;
import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvChannelMerchant;
import cn.daxpay.open.channel.alipay.param.isv.AlipayIsvAuthParam;
import cn.daxpay.open.channel.alipay.result.isv.AlipayIsvAuthUrlResult;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.masterdata.constants.product.dao.PayProductConfigManager;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.config.PayEnvEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.system.service.config.PlatformUrlConfigService;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/// # 支付宝服务商代运营授权
///
/// 通道商户维度: 生成 INTERFACE_AUTH 授权深链, H5 回调用授权码换 token,
/// 并校验返回的 userId 与通道商户已绑定的 alipayUserId 一致后落库。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayIsvAuthService {

    /// 支付宝开放平台「第三方应用授权」小程序 appId(官方固定)
    private static final String AUTH_MINI_APP_ID = "2021003130652097";

    /// 授权应用类型列表(JSON 数组字面量, 对齐支付宝 agentOpParam.appTypes)
    private static final String APP_TYPES_JSON = "[\"MOBILEAPP\",\"WEBAPP\",\"PUBLICAPP\",\"TINYAPP\"]";

    private final AlipayIsvChannelMerchantManager alipayIsvChannelMerchantManager;
    private final AlipayIsvAppManager alipayIsvAppManager;
    private final AlipayIsvAppKeyConfigService alipayIsvAppKeyConfigService;
    private final PayProductConfigManager payProductConfigManager;
    private final PlatformUrlConfigService platformUrlConfigService;
    private final AlipayChannelClient alipayChannelClient;

    /// 生成代运营授权链接(支付宝深链, 可出二维码)
    public AlipayIsvAuthUrlResult genAuthUrl(AlipayIsvAuthParam param) {
        AlipayIsvChannelMerchant entity = this.loadChannelMerchant(param.getChannelMchNo());
        // 必须先绑定子商户号, 授权后才能做 userId 一致性校验
        if (StrUtil.isBlank(entity.getAlipayUserId())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.alipay.subMerchantNoRequired");
        }
        AlipayIsvApp isvApp = alipayIsvAppManager.findById(entity.getIsvAppId())
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
        String gatewayBase = platformUrlConfigService.getUrlConfig().getPaymentGatewayBaseUrl();
        if (StrUtil.isBlank(gatewayBase)) {
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL,
                    "error.channel.alipay.paymentGatewayUrlRequired");
        }
        String redirectUri = gatewayBase + "/isv-auth/alipay";
        // state 绑定通道商户号, 回调时定位落库目标
        String state = Base64.encode(entity.getChannelMchNo());
        // 对齐商业版 INTERFACE_AUTH 深链; StrUtil.format 将 {} 视为占位符, appTypes 的空对象也需传入
        String authUrl = StrUtil.format(
                "alipays://platformapi/startapp?appId={}&page=pages/authorize/index?bizData="
                        + "{\"platformCode\":\"O\",\"taskType\":\"INTERFACE_AUTH\","
                        + "\"agentOpParam\":{\"redirectUri\":\"{}\",\"appTypes\":{},\"isvAppId\":\"{}\",\"state\":\"{}\"}}",
                AUTH_MINI_APP_ID,
                redirectUri,
                APP_TYPES_JSON,
                isvApp.getAliAppId(),
                state);
        return new AlipayIsvAuthUrlResult().setAuthUrl(authUrl);
    }

    /// H5 回调: 授权码换 token + 绑定校验 + 写回通道商户
    @Transactional(rollbackFor = Exception.class)
    public void auth(AlipayIsvAuthParam param) {
        if (StrUtil.isBlank(param.getCode())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.alipay.authCodeInvalid");
        }
        AlipayIsvChannelMerchant entity = this.loadChannelMerchant(param.getChannelMchNo());
        if (StrUtil.isBlank(entity.getAlipayUserId())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.alipay.subMerchantNoRequired");
        }
        // 组装服务商应用凭证(不带子商户 token)
        AlipaySdkCredential credential = this.buildIsvCredential(entity.getIsvAppId());
        AlipayAppAuthTokenReq req = new AlipayAppAuthTokenReq();
        req.setAuthCode(param.getCode());
        req.setCredential(credential);
        DaxResult<AlipayAppAuthTokenResp> result = alipayChannelClient.exchangeAppAuthToken(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL,
                    "error.channel.alipay.authTokenExchangeFailed", result.getMsg());
        }
        AlipayAppAuthTokenResp resp = result.getData();
        if (resp == null || !Objects.equals("10000", resp.getCode())) {
            String detail = resp != null ? StrUtil.blankToDefault(resp.getSubMsg(), resp.getCode()) : "empty";
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL,
                    "error.channel.alipay.authTokenExchangeFailed", detail);
        }
        // 核心限定: 授权返回的商户号必须与通道商户绑定的子商户号一致
        if (!Objects.equals(resp.getUserId(), entity.getAlipayUserId())) {
            log.warn("代运营授权商户号不匹配: channelMchNo={}, expected={}, actual={}",
                    entity.getChannelMchNo(), entity.getAlipayUserId(), resp.getUserId());
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL,
                    "error.channel.alipay.authUserIdMismatch");
        }
        if (StrUtil.isBlank(resp.getAppAuthToken())) {
            throw new BizInfoException(DaxPayErrorCode.OPERATION_FAIL,
                    "error.channel.alipay.authTokenExchangeFailed", "app_auth_token empty");
        }
        entity.setAppAuthToken(resp.getAppAuthToken());
        alipayIsvChannelMerchantManager.updateById(entity);
    }

    /// 按通道商户号加载绑定记录
    private AlipayIsvChannelMerchant loadChannelMerchant(String channelMchNo) {
        return alipayIsvChannelMerchantManager.lambdaQuery()
                .eq(AlipayIsvChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
    }

    /// 组装服务商应用凭证(密钥取自 IsvAppKeyConfig, 不含 appAuthToken)
    private AlipaySdkCredential buildIsvCredential(Long isvAppId) {
        AlipayIsvApp isvApp = alipayIsvAppManager.findById(isvAppId)
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
        AlipayIsvAppKeyConfig keyConfig = alipayIsvAppKeyConfigService.findByAlipayIsvAppId(isvAppId);
        var credential = new AlipaySdkCredential();
        credential.setAliAppId(isvApp.getAliAppId());
        credential.setPrivateKey(keyConfig.getPrivateKey());
        credential.setAlipayPublicKey(keyConfig.getAlipayPublicKey());
        credential.setAuthType(keyConfig.getAuthType());
        credential.setAppCert(keyConfig.getAppCert());
        credential.setAlipayCert(keyConfig.getAlipayCert());
        credential.setAlipayRootCert(keyConfig.getAlipayRootCert());
        boolean sandbox = payProductConfigManager.findByProduct(ProductEnum.ALIPAY_ISV.getCode())
                .map(c -> PayEnvEnum.SANDBOX.getCode().equals(c.getActiveEnv()))
                .orElse(false);
        credential.setSandbox(sandbox);
        return credential;
    }
}
