package cn.daxpay.open.channel.hmpay.service.isv;

import cn.daxpay.open.channel.hmpay.client.credential.HmpaySdkCredential;
import cn.daxpay.open.channel.hmpay.dao.isv.HmpayIsvChannelMerchantManager;
import cn.daxpay.open.channel.hmpay.entity.isv.HmpayIsvChannelMerchant;
import cn.daxpay.open.channel.hmpay.entity.isv.HmpayIsvKeyConfig;
import cn.daxpay.open.channel.hmpay.strategy.product.HmpayProductStrategy;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 河马付服务商通道凭证组装器
///
/// 从服务商密钥配置([HmpayIsvKeyConfig]) + 通道商户绑定([HmpayIsvChannelMerchant]) 组装通道调用凭证,
/// 下发给子应用 dax-pay-channel-two 发起河马付(杉德) API 调用。
///
/// 沙箱标识直接读通道商户固化的 [HmpayIsvChannelMerchant#isSandbox]
/// (创建时按当时产品 activeEnv 写入, 不随产品切换改变), 据此选择对应环境的密钥与网关地址。
///
/// 字段映射:
/// - sandAppId/privateKey/publicKey ← [HmpayIsvKeyConfig](服务商级, 全局唯一)
/// - merchantNo/storeId ← [HmpayIsvChannelMerchant](商户级)
///
/// 供支付策略([cn.daxpay.open.channel.hmpay.strategy.*])组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class HmpayIsvConfigAssembler {

    private final HmpayIsvChannelMerchantManager hmpayIsvChannelMerchantManager;
    private final HmpayIsvKeyConfigService hmpayIsvKeyConfigService;
    private final WxAppFacade wxAppFacade;
    private final HmpayProductStrategy productStrategy;

    /// 组装河马付通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(保留参数, 服务商密钥全局唯一不依赖此字段)
    /// @param channelMchNo 通道商户号(河马付商户绑定主键)
    /// @param capability   支付能力编码(保留参数, 河马付不按能力路由)
    /// @return 河马付 SDK 凭证
    public HmpaySdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 通道商户绑定(取 merchantNo/storeId + sandbox)
        HmpayIsvChannelMerchant channelMerchant = hmpayIsvChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // 河马付: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        // 沙箱标识直接读通道商户固化的快照(创建时按当时产品 activeEnv 写入, 不随产品切换改变)
        boolean sandbox = channelMerchant.isSandbox();
        // 服务商密钥(按 sandbox 分环境, 含 sandAppId/密钥; 缺失或关键字段为空时 fail-fast)
        HmpayIsvKeyConfig keyConfig = hmpayIsvKeyConfigService.getByProductForPay(ProductEnum.HM_PAY.getCode(), sandbox);

        HmpaySdkCredential credential = new HmpaySdkCredential();
        // 服务商身份与密钥
        credential.setSandAppId(keyConfig.getSandAppId());
        credential.setPrivateKey(keyConfig.getPrivateKey());
        credential.setPublicKey(keyConfig.getPublicKey());
        credential.setSandbox(sandbox);
        // 子商户身份
        credential.setMerchantNo(channelMerchant.getMerchantNo());
        credential.setStoreId(channelMerchant.getStoreId());
        return credential;
    }

    /// 尽力解析微信应用并回填 channelAppId(微信 JSAPI/小程序支付随单上送 mer_app_id)
    ///
    /// 解析顺序: 显式 channelAppId → 通道商户能力绑定(商户档优先, 平台档兜底) → 产品级平台默认绑,
    /// 与 [WxAppFacade#resolveOptional] 一致; 仅产品策略([HmpayProductStrategy#wxAppRequiredCapabilities])
    /// 声明需要应用的能力(JSAPI/小程序)解析, 其余能力(扫码/付款码/支付宝/聚合扫码)不解析。
    /// 河马付 mer_app_id 随单可选上送, 未命中不回填不阻断;
    /// 调用方显式传入的 channelAppId 未命中应用表时保留原值透传, 与既有行为兼容。
    ///
    /// @param payParam 支付参数(channelAppId 回填后供子应用上送与订单落库)
    public void resolveWxAppIfRequired(NormalPayParam payParam) {
        PayCapabilityEnum cap = PayCapabilityEnum.findByCode(payParam.getCapability());
        // 非微信应用所需能力(扫码/付款码/支付宝/聚合扫码)不解析
        if (Objects.isNull(cap) || !productStrategy.wxAppRequiredCapabilities().contains(cap)) {
            return;
        }
        wxAppFacade.resolveOptional(payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability(),
                        payParam.getChannelAppId(), ProductEnum.HM_PAY.getCode())
                .ifPresent(app -> payParam.setChannelAppId(app.wxAppId()));
    }
}
