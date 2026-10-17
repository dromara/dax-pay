package cn.daxpay.open.channel.lakala.service.isv;

import cn.daxpay.open.channel.lakala.client.credential.MossSdkCredential;
import cn.daxpay.open.channel.lakala.dao.isv.LakalaIsvChannelMerchantManager;
import cn.daxpay.open.channel.lakala.dao.isv.LakalaIsvKeyConfigManager;
import cn.daxpay.open.channel.lakala.entity.isv.LakalaIsvChannelMerchant;
import cn.daxpay.open.channel.lakala.entity.isv.LakalaIsvKeyConfig;
import cn.daxpay.open.channel.lakala.strategy.product.LakalaMossProductStrategy;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 拉卡拉 MOSS 通道凭证组装器
///
/// 复用拉卡拉现有两表(零表结构变更, 以 product=lakala_moss 区分):
/// - 密钥配置 `lakala_isv_key_config`(全局一份): lklAppId→MOSS 应用编号(appid/businessChannel),
///   privateKey→商户 MOSS RSA 私钥, publicKey→平台公钥(可选, 为空时子应用回退内置默认值)
/// - 通道商户绑定 `lakala_isv_channel_merchant`(按商户): lakalaMchNo→MOSS 商户号(mer_no),
///   termNo 对 MOSS 无意义不校验
///
/// 供 MOSS 产品策略族([cn.daxpay.open.channel.lakala.strategy.*])组装通道调用凭证。
///
/// 注意: MOSS 沙箱/生产共用同一网关(无公开沙箱地址), sandbox 仅参与密钥选取。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaMossConfigAssembler {

    private final LakalaIsvChannelMerchantManager lakalaIsvChannelMerchantManager;
    private final LakalaIsvKeyConfigManager lakalaIsvKeyConfigManager;
    private final WxAppFacade wxAppFacade;
    private final LakalaMossProductStrategy productStrategy;

    /// 组装 MOSS 通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(保留参数对齐签名, 密钥全局唯一不依赖此字段)
    /// @param channelMchNo 通道商户号(拉卡拉商户绑定主键)
    /// @param capability   支付能力编码(保留参数, MOSS 不按能力路由)
    /// @return MOSS SDK 凭证(appid + 商户私钥 + 平台公钥 + 商户号)
    public MossSdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 通道商户绑定(取 mer_no + sandbox 快照)
        LakalaIsvChannelMerchant channelMerchant = lakalaIsvChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // 拉卡拉: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        boolean sandbox = channelMerchant.isSandbox();
        // 密钥配置(appid + 商户私钥必填, 平台公钥可选; 缺失时 fail-fast)
        LakalaIsvKeyConfig keyConfig = lakalaIsvKeyConfigManager.findByProductAndSandbox(
                        ProductEnum.LAKALA_MOSS.getCode(), sandbox)
                // 拉卡拉: 服务商密钥未配置
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.lakala.isvKeyNotConfigured"));
        if (StrUtil.hasBlank(keyConfig.getLklAppId(), keyConfig.getPrivateKey())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.lakala.isvKeyNotConfigured");
        }

        MossSdkCredential credential = new MossSdkCredential();
        credential.setAppId(keyConfig.getLklAppId());
        credential.setMchPrivateKey(keyConfig.getPrivateKey());
        // 平台公钥可选: 未配置时子应用回退内置默认平台公钥
        credential.setPlatformPublicKey(keyConfig.getPublicKey());
        credential.setMerNo(channelMerchant.getLakalaMchNo());
        return credential;
    }

    /// 尽力解析微信应用并回填 channelAppId(微信 JSAPI 支付上送 sub_appid)
    ///
    /// 解析逻辑与 [LakalaIsvConfigAssembler#resolveWxAppIfRequired] 一致:
    /// 仅产品策略([LakalaMossProductStrategy#wxAppRequiredCapabilities])声明需要应用的能力
    /// (JSAPI)解析, 其余能力(扫码/收银台/支付宝)不解析; 未命中不回填不阻断。
    public void resolveWxAppIfRequired(NormalPayParam payParam) {
        PayCapabilityEnum cap = PayCapabilityEnum.findByCode(payParam.getCapability());
        // 非微信应用所需能力(扫码/支付宝)不解析
        if (Objects.isNull(cap) || !productStrategy.wxAppRequiredCapabilities().contains(cap)) {
            return;
        }
        wxAppFacade.resolveOptional(payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability(),
                        payParam.getChannelAppId(), ProductEnum.LAKALA_MOSS.getCode())
                .ifPresent(app -> payParam.setChannelAppId(app.wxAppId()));
    }
}
