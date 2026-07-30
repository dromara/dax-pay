package cn.daxpay.open.channel.wechat.service.isv;

import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvKeyConfigManager;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvKeyConfig;
import cn.daxpay.open.payment.common.check.checker.ProductBindingChecker;
import cn.daxpay.open.payment.common.check.model.ProductBindingCheckItem;
import cn.daxpay.open.payment.wx.service.platform.WxPlatformAppCapabilityService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/// # 微信服务商产品绑定检查器
///
/// 检查微信服务商产品(`wechat_isv`)的4项关键配置完整性:
/// 1. 服务商商户号(wxMchId)
/// 2. API V3密钥(apiKeyV3)
/// 3. API证书(privateKey + certSerialNo)
/// 4. 默认支付应用(产品级平台应用能力绑定, 至少一条)
///
/// 检查为只读操作, 不产生写入副作用。
@Slf4j
@Component
@RequiredArgsConstructor
public class WechatIsvProductBindingChecker implements ProductBindingChecker {

    private final WechatIsvKeyConfigManager wechatIsvKeyConfigManager;
    private final WxPlatformAppCapabilityService wxPlatformAppCapabilityService;

    @Override
    public String getProduct() {
        return ProductEnum.WECHAT_ISV.getCode();
    }

    @Override
    public List<ProductBindingCheckItem> check() {
        // 只读查询密钥配置, 不使用 findByProduct(有 upsert 副作用)
        Optional<WechatIsvKeyConfig> configOpt = wechatIsvKeyConfigManager.findByProduct(getProduct());
        WechatIsvKeyConfig config = configOpt.orElse(null);

        boolean mchIdConfigured = config != null && StrUtil.isNotBlank(config.getWxMchId());
        boolean apiKeyConfigured = config != null && StrUtil.isNotBlank(config.getApiKeyV3());
        boolean certConfigured = config != null
                && StrUtil.isNotBlank(config.getPrivateKey())
                && StrUtil.isNotBlank(config.getCertSerialNo());
        // 产品级默认应用: 至少绑定一个支付能力
        boolean defaultAppConfigured = !wxPlatformAppCapabilityService.listByProduct(getProduct()).isEmpty();

        return List.of(
                // 服务商商户号
                ProductBindingCheckItem.of(
                        "wechatIsv.mchId",
                        "productBindingCheck.wechatIsv.mchId.title",
                        "productBindingCheck.wechatIsv.mchId.description",
                        mchIdConfigured,
                        "openKeyConfig"
                ),
                // API V3密钥
                ProductBindingCheckItem.of(
                        "wechatIsv.apiKeyV3",
                        "productBindingCheck.wechatIsv.apiKeyV3.title",
                        "productBindingCheck.wechatIsv.apiKeyV3.description",
                        apiKeyConfigured,
                        "openKeyConfig"
                ),
                // API证书(私钥 + 证书序列号)
                ProductBindingCheckItem.of(
                        "wechatIsv.cert",
                        "productBindingCheck.wechatIsv.cert.title",
                        "productBindingCheck.wechatIsv.cert.description",
                        certConfigured,
                        "openKeyConfig"
                ),
                // 默认支付应用(产品级平台应用能力绑定)
                ProductBindingCheckItem.of(
                        "wechatIsv.defaultApp",
                        "productBindingCheck.wechatIsv.defaultApp.title",
                        "productBindingCheck.wechatIsv.defaultApp.description",
                        defaultAppConfigured,
                        "openPlatformCapability"
                )
        );
    }
}
