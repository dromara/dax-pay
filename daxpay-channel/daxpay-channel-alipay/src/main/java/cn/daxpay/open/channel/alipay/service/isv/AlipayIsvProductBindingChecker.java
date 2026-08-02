package cn.daxpay.open.channel.alipay.service.isv;

import cn.daxpay.open.channel.alipay.code.AlipayCode;
import cn.daxpay.open.channel.alipay.dao.isv.AlipayIsvAppAuthConfigManager;
import cn.daxpay.open.channel.alipay.dao.isv.AlipayIsvAppKeyConfigManager;
import cn.daxpay.open.channel.alipay.dao.isv.AlipayIsvAppManager;
import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvApp;
import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvAppAuthConfig;
import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvAppKeyConfig;
import cn.daxpay.open.payment.common.check.checker.ProductBindingChecker;
import cn.daxpay.open.payment.common.check.model.ProductBindingCheckItem;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/// # 支付宝服务商产品绑定检查器
///
/// 检查支付宝服务商产品(`alipay_isv`)的3项关键配置完整性:
/// 1. 服务商应用(至少配置一个 [AlipayIsvApp])
/// 2. 应用密钥配置(至少一个应用密钥完整, 按认证类型区分公钥/证书模式)
/// 3. 应用授权认证(至少一个应用配置了用户标识类型 [AlipayIsvAppAuthConfig])
///
/// 与微信服务商的差异:
/// - 微信是单一密钥行(product 维度), 支付宝是多应用结构, 密钥挂在每个 [AlipayIsvApp] 下
/// - 微信有"产品级默认应用能力"检查, 支付宝 ISV 无此机制(子商户靠 appAuthToken 授权, 不需要)
///
/// 检查为只读操作, 不产生写入副作用。
@Slf4j
@Component
@RequiredArgsConstructor
public class AlipayIsvProductBindingChecker implements ProductBindingChecker {

    private final AlipayIsvAppManager alipayIsvAppManager;
    private final AlipayIsvAppKeyConfigManager alipayIsvAppKeyConfigManager;
    private final AlipayIsvAppAuthConfigManager alipayIsvAppAuthConfigManager;

    @Override
    public String getProduct() {
        return ProductEnum.ALIPAY_ISV.getCode();
    }

    @Override
    public List<ProductBindingCheckItem> check() {
        List<AlipayIsvApp> apps = alipayIsvAppManager.listAll();

        // 服务商应用: 至少配置一个
        boolean appConfigured = !apps.isEmpty();

        // 应用密钥: 至少一个应用密钥配置完整(按认证类型分支)
        boolean appKeyConfigured = apps.stream().anyMatch(app ->
                alipayIsvAppKeyConfigManager.findByAlipayIsvAppId(app.getId())
                        .map(AlipayIsvProductBindingChecker::isKeyComplete)
                        .orElse(false));

        // 应用授权认证: 至少一个应用配置了用户标识类型
        boolean appAuthConfigured = apps.stream().anyMatch(app ->
                alipayIsvAppAuthConfigManager.findByAlipayIsvAppId(app.getId())
                        .map(AlipayIsvProductBindingChecker::isAuthComplete)
                        .orElse(false));

        return List.of(
                // 服务商应用
                ProductBindingCheckItem.of(
                        "alipayIsv.app",
                        "productBindingCheck.alipayIsv.app.title",
                        "productBindingCheck.alipayIsv.app.description",
                        appConfigured,
                        "openAppManage"
                ),
                // 应用密钥配置(公钥模式或证书模式)
                ProductBindingCheckItem.of(
                        "alipayIsv.appKey",
                        "productBindingCheck.alipayIsv.appKey.title",
                        "productBindingCheck.alipayIsv.appKey.description",
                        appKeyConfigured,
                        "openAppManage"
                ),
                // 应用授权认证
                ProductBindingCheckItem.of(
                        "alipayIsv.appAuth",
                        "productBindingCheck.alipayIsv.appAuth.title",
                        "productBindingCheck.alipayIsv.appAuth.description",
                        appAuthConfigured,
                        "openAppManage"
                )
        );
    }

    /// 密钥配置完整性判断(按认证类型分支):
    /// - 公钥模式([AlipayCode.AuthType#AUTH_TYPE_KEY]): 需支付宝公钥 + 应用私钥
    /// - 证书模式([AlipayCode.AuthType#AUTH_TYPE_CERT]): 需应用私钥 + 应用公钥证书 + 支付宝公钥证书 + 支付宝根证书
    private static boolean isKeyComplete(AlipayIsvAppKeyConfig config) {
        // 公钥模式: 支付宝公钥 + 应用私钥
        if (AlipayCode.AuthType.AUTH_TYPE_KEY.equals(config.getAuthType())) {
            return StrUtil.isAllNotBlank(config.getAlipayPublicKey(), config.getPrivateKey());
        }
        // 证书模式: 应用私钥 + 应用公钥证书 + 支付宝公钥证书 + 支付宝根证书
        if (AlipayCode.AuthType.AUTH_TYPE_CERT.equals(config.getAuthType())) {
            return StrUtil.isAllNotBlank(
                    config.getPrivateKey(),
                    config.getAppCert(),
                    config.getAlipayCert(),
                    config.getAlipayRootCert()
            );
        }
        // 未知认证类型视为未配置
        return false;
    }

    /// 授权认证配置完整性判断: 用户标识类型非空
    private static boolean isAuthComplete(AlipayIsvAppAuthConfig config) {
        return StrUtil.isNotBlank(config.getUserIdType());
    }
}
