package cn.daxpay.open.channel.hkrt.service.isv;

import cn.daxpay.open.channel.hkrt.client.credential.HkrtSdkCredential;
import cn.daxpay.open.channel.hkrt.dao.isv.HkrtIsvChannelMerchantManager;
import cn.daxpay.open.channel.hkrt.entity.isv.HkrtIsvChannelMerchant;
import cn.daxpay.open.channel.hkrt.entity.isv.HkrtIsvKeyConfig;
import cn.daxpay.open.channel.hkrt.strategy.product.HkrtProductStrategy;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 海科融通服务商通道凭证组装器
///
/// 从服务商密钥配置([HkrtIsvKeyConfig]) + 通道商户绑定([HkrtIsvChannelMerchant]) 组装通道调用凭证,
/// 下发给子应用 dax-pay-channel-two 发起海科融通 API 调用。
///
/// 沙箱标识直接读通道商户固化的 [HkrtIsvChannelMerchant#isSandbox]
/// (创建时按当时产品 activeEnv 写入, 不随产品切换改变), 据此选择对应环境的密钥与网关地址。
///
/// 字段映射(对齐海科融通接口):
/// - agent_no / access_id / access_key ← [HkrtIsvKeyConfig](服务商级, 全局唯一)
/// - merch_no ← [HkrtIsvChannelMerchant#merchNo](海科商户号)
/// - pn ← [HkrtIsvChannelMerchant#pn](SAAS 终端号)
///
/// 供支付策略([cn.daxpay.open.channel.hkrt.strategy.*])组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class HkrtIsvConfigAssembler {

    private final HkrtIsvChannelMerchantManager hkrtIsvChannelMerchantManager;
    private final HkrtIsvKeyConfigService hkrtIsvKeyConfigService;
    private final WxAppFacade wxAppFacade;
    private final HkrtProductStrategy productStrategy;

    /// 组装海科融通通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(保留参数对齐签名, 服务商密钥全局唯一不依赖此字段)
    /// @param channelMchNo 通道商户号(海科商户绑定主键)
    /// @param capability   支付能力编码(保留参数, 海科融通不按能力路由)
    /// @return 海科融通 SDK 凭证(含服务商密钥 + 商户号/终端号)
    public HkrtSdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 通道商户绑定(取 merchNo + pn + sandbox)
        HkrtIsvChannelMerchant channelMerchant = hkrtIsvChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // 海科融通: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        // 沙箱标识直接读通道商户固化的快照(创建时按当时产品 activeEnv 写入, 不随产品切换改变)
        boolean sandbox = channelMerchant.isSandbox();
        // 服务商密钥(按 sandbox 分环境, 含 agentNo/accessId/accessKey; 缺失或关键字段为空时 fail-fast)
        HkrtIsvKeyConfig keyConfig = hkrtIsvKeyConfigService.getByProductForPay(ProductEnum.HKRT_PAY.getCode(), sandbox);

        HkrtSdkCredential credential = new HkrtSdkCredential();
        // 服务商身份与密钥
        credential.setAgentNo(keyConfig.getAgentNo());
        credential.setAccessId(keyConfig.getAccessId());
        credential.setAccessKey(keyConfig.getAccessKey());
        credential.setSandbox(sandbox);
        // 子商户身份
        credential.setMerchNo(channelMerchant.getMerchNo());
        // SAAS 终端号(从通道商户配置取)
        String pn = channelMerchant.getPn();
        if (StrUtil.isBlank(pn)) {
            // 海科融通: 终端号未配置, 请在商户配置中填写
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.hkrt.termNoNotConfigured");
        }
        credential.setPn(pn);
        return credential;
    }

    /// 尽力解析微信应用并回填 channelAppId(微信 JSAPI/小程序支付上送 sub_appid)
    ///
    /// 解析顺序: 显式 channelAppId → 通道商户能力绑定(商户档优先, 平台档兜底) → 产品级平台默认绑,
    /// 与 [WxAppFacade#resolveOptional] 一致; 仅产品策略([HkrtProductStrategy#wxAppRequiredCapabilities])
    /// 声明需要应用的能力(JSAPI/小程序)解析, 其余能力(条码/支付宝/银联)不解析。
    /// 海科 sub_appid 可选(海科后台已绑定时可不传), 未命中不回填不阻断;
    /// 调用方显式传入的 channelAppId 未命中应用表时保留原值透传, 与既有行为兼容。
    ///
    /// @param payParam 支付参数(channelAppId 回填后供子应用上送与订单落库)
    public void resolveWxAppIfRequired(NormalPayParam payParam) {
        PayCapabilityEnum cap = PayCapabilityEnum.findByCode(payParam.getCapability());
        // 非微信应用所需能力(条码/支付宝/银联)不解析
        if (Objects.isNull(cap) || !productStrategy.wxAppRequiredCapabilities().contains(cap)) {
            return;
        }
        wxAppFacade.resolveOptional(payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability(),
                        payParam.getChannelAppId(), ProductEnum.HKRT_PAY.getCode())
                .ifPresent(app -> payParam.setChannelAppId(app.wxAppId()));
    }
}
