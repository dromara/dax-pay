package cn.daxpay.open.channel.fuyou.service.isv;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouSdkCredential;
import cn.daxpay.open.channel.fuyou.dao.isv.FuyouIsvChannelMerchantManager;
import cn.daxpay.open.channel.fuyou.entity.isv.FuyouIsvChannelMerchant;
import cn.daxpay.open.channel.fuyou.entity.isv.FuyouIsvKeyConfig;
import cn.daxpay.open.channel.fuyou.strategy.product.FuyouProductStrategy;
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

/// # 富友服务商通道凭证组装器
///
/// 从服务商密钥配置([FuyouIsvKeyConfig]) + 通道商户绑定([FuyouIsvChannelMerchant]) 组装通道调用凭证,
/// 下发给子应用 dax-pay-channel-two 发起富友 API 调用。
///
/// 沙箱标识直接读通道商户固化的 [FuyouIsvChannelMerchant#isSandbox]
/// (创建时按当时产品 activeEnv 写入, 不随产品切换改变), 据此选择对应环境的密钥与网关地址。
///
/// 字段映射(对齐富友开放平台接口):
/// - fyAppId/orderPrefix/privateKey/publicKey ← [FuyouIsvKeyConfig] (服务商级, 全局唯一)
/// - merchantNo/termNo ← [FuyouIsvChannelMerchant] (富友商户号 + 终端号, 子商户级)
/// - sandbox ← [FuyouIsvChannelMerchant#isSandbox] (沙箱/生产, 创建时固化)
///
/// 供支付策略([cn.daxpay.open.channel.fuyou.strategy.*])组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouIsvConfigAssembler {

    private final FuyouIsvChannelMerchantManager fuyouIsvChannelMerchantManager;
    private final FuyouIsvKeyConfigService fuyouIsvKeyConfigService;
    private final WxAppFacade wxAppFacade;
    private final FuyouProductStrategy productStrategy;

    /// 组装富友通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(保留参数对齐签名, 服务商密钥全局唯一不依赖此字段)
    /// @param channelMchNo 通道商户号(富友商户绑定主键)
    /// @param capability   支付能力编码(保留参数, 富友不按能力路由)
    /// @return 富友 SDK 凭证(含服务商密钥 + 富友商户号 + 终端号)
    public FuyouSdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 通道商户绑定(取富友商户号 + 终端号 + sandbox)
        FuyouIsvChannelMerchant channelMerchant = fuyouIsvChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // 富友: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        // 沙箱标识直接读通道商户固化的快照(创建时按当时产品 activeEnv 写入, 不随产品切换改变)
        boolean sandbox = channelMerchant.isSandbox();
        // 服务商密钥(按 sandbox 分环境取对应密钥, 含机构号 + 私钥/公钥 + 订单前缀; 缺失或关键字段为空时 fail-fast)
        FuyouIsvKeyConfig keyConfig = fuyouIsvKeyConfigService.getByProductForPay(ProductEnum.FUYOU_PAY.getCode(), sandbox);

        FuyouSdkCredential credential = new FuyouSdkCredential();
        // 服务商身份与密钥
        credential.setFyAppId(keyConfig.getFyAppId());
        credential.setOrderPrefix(keyConfig.getOrderPrefix());
        credential.setPrivateKey(keyConfig.getPrivateKey());
        credential.setPublicKey(keyConfig.getPublicKey());
        credential.setSandbox(sandbox);
        // 子商户身份(富友商户号 + 终端号)
        String fuyouMchNo = channelMerchant.getFuyouMchNo();
        if (StrUtil.isBlank(fuyouMchNo)) {
            // 富友: 富友商户号未配置
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.fuyou.mchNoNotConfigured");
        }
        credential.setMerchantNo(fuyouMchNo);
        credential.setTermNo(channelMerchant.getTermNo());
        return credential;
    }

    /// 尽力解析微信应用并回填 channelAppId(微信 JSAPI/小程序支付随单上送 sub_appid)
    ///
    /// 解析顺序: 显式 channelAppId → 通道商户能力绑定(商户档优先, 平台档兜底) → 产品级平台默认绑,
    /// 与 [WxAppFacade#resolveOptional] 一致; 仅产品策略([FuyouProductStrategy#wxAppRequiredCapabilities])
    /// 声明需要应用的能力(JSAPI/小程序)解析, 其余能力(扫码/付款码/支付宝/银联)不解析。
    /// 富友 sub_appid 可选(富友后台已绑定时可不传), 未命中不回填不阻断;
    /// 调用方显式传入的 channelAppId 未命中应用表时保留原值透传, 与既有行为兼容。
    ///
    /// @param payParam 支付参数(channelAppId 回填后供子应用上送与订单落库)
    public void resolveWxAppIfRequired(NormalPayParam payParam) {
        PayCapabilityEnum cap = PayCapabilityEnum.findByCode(payParam.getCapability());
        // 非微信应用所需能力(扫码/付款码/支付宝/银联)不解析
        if (Objects.isNull(cap) || !productStrategy.wxAppRequiredCapabilities().contains(cap)) {
            return;
        }
        wxAppFacade.resolveOptional(payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability(),
                        payParam.getChannelAppId(), ProductEnum.FUYOU_PAY.getCode())
                .ifPresent(app -> payParam.setChannelAppId(app.wxAppId()));
    }
}
