package cn.daxpay.open.channel.lakala.service.isv;

import cn.daxpay.open.channel.lakala.client.credential.LakalaSdkCredential;
import cn.daxpay.open.channel.lakala.dao.isv.LakalaIsvChannelMerchantManager;
import cn.daxpay.open.channel.lakala.entity.isv.LakalaIsvChannelMerchant;
import cn.daxpay.open.channel.lakala.entity.isv.LakalaIsvKeyConfig;
import cn.daxpay.open.channel.lakala.strategy.product.LakalaProductStrategy;
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

/// # 拉卡拉服务商通道凭证组装器
///
/// 从服务商密钥配置([LakalaIsvKeyConfig]) + 通道商户绑定([LakalaIsvChannelMerchant]) 组装通道调用凭证,
/// 下发给子应用 dax-pay-channel-two 发起拉卡拉 API 调用。
///
/// 沙箱标识直接读通道商户固化的 [LakalaIsvChannelMerchant#isSandbox]
/// (创建时按当时产品 activeEnv 写入, 不随产品切换改变), 据此选择对应环境的密钥与网关地址。
///
/// 字段映射(对齐拉卡拉 V3 接口):
/// - lkl_app_id ← [LakalaIsvKeyConfig#lklAppId] (拉卡拉应用编号, 全局唯一)
/// - merchant_no ← [LakalaIsvChannelMerchant#lakalaMchNo] (拉卡拉商户号)
/// - term_no ← [LakalaIsvChannelMerchant#termNo] (终端号)
/// - 私钥/公钥/证书序列号 ← [LakalaIsvKeyConfig] (服务商级, 全局唯一)
///
/// 供支付策略([cn.daxpay.open.channel.lakala.strategy.*])组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaIsvConfigAssembler {

    private final LakalaIsvChannelMerchantManager lakalaIsvChannelMerchantManager;
    private final LakalaIsvKeyConfigService lakalaIsvKeyConfigService;
    private final WxAppFacade wxAppFacade;
    private final LakalaProductStrategy productStrategy;

    /// 组装拉卡拉通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(保留参数对齐签名, 服务商密钥全局唯一不依赖此字段)
    /// @param channelMchNo 通道商户号(拉卡拉商户绑定主键)
    /// @param capability   支付能力编码(保留参数, 拉卡拉不按能力路由)
    /// @return 拉卡拉 SDK 凭证(含服务商密钥 + 商户号/终端号)
    public LakalaSdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 通道商户绑定(取 merchantNo + termNo + sandbox)
        LakalaIsvChannelMerchant channelMerchant = lakalaIsvChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // 拉卡拉: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        // 沙箱标识直接读通道商户固化的快照(创建时按当时产品 activeEnv 写入, 不随产品切换改变)
        boolean sandbox = channelMerchant.isSandbox();
        // 服务商密钥(按 sandbox 分环境取对应密钥, 含 lkl_app_id + 私钥/公钥; 缺失或关键字段为空时 fail-fast)
        LakalaIsvKeyConfig keyConfig = lakalaIsvKeyConfigService.getByProductForPay(ProductEnum.LAKALA_PAY.getCode(), sandbox);

        LakalaSdkCredential credential = new LakalaSdkCredential();
        // 服务商身份与密钥
        credential.setLklAppId(keyConfig.getLklAppId());
        credential.setMchSerialNo(keyConfig.getMchSerialNo());
        credential.setPrivateKey(keyConfig.getPrivateKey());
        credential.setPublicKey(keyConfig.getPublicKey());
        credential.setSandbox(sandbox);
        // 子商户身份
        credential.setLakalaMchNo(channelMerchant.getLakalaMchNo());
        // 终端号(从通道商户配置取)
        String termNo = channelMerchant.getTermNo();
        if (StrUtil.isBlank(termNo)) {
            // 拉卡拉: 终端号未配置, 请在商户配置中填写
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.lakala.termNoNotConfigured");
        }
        credential.setTermNo(termNo);
        // 商户门店编号(拉卡拉 V3 接口"支付宝收单上送", 条件必填 C)
        // TODO 门店对接后从门店配置读取, 当前暂写死占位值
        credential.setStoreId("1");
        return credential;
    }

    /// 尽力解析微信应用并回填 channelAppId(微信 JSAPI/小程序支付上送 sub_appid)
    ///
    /// 解析顺序: 显式 channelAppId → 通道商户能力绑定(商户档优先, 平台档兜底) → 产品级平台默认绑,
    /// 与 [WxAppFacade#resolveOptional] 一致; 仅产品策略([LakalaProductStrategy#wxAppRequiredCapabilities])
    /// 声明需要应用的能力(JSAPI/小程序)解析, 其余能力(条码/APP/支付宝/银联)不解析。
    /// 拉卡拉 sub_appid 可选(拉卡拉后台已绑定时可不传), 未命中不回填不阻断;
    /// 调用方显式传入的 channelAppId 未命中应用表时保留原值透传, 与既有行为兼容。
    ///
    /// @param payParam 支付参数(channelAppId 回填后供子应用上送与订单落库)
    public void resolveWxAppIfRequired(NormalPayParam payParam) {
        PayCapabilityEnum cap = PayCapabilityEnum.findByCode(payParam.getCapability());
        // 非微信应用所需能力(条码/APP/支付宝/银联)不解析
        if (Objects.isNull(cap) || !productStrategy.wxAppRequiredCapabilities().contains(cap)) {
            return;
        }
        wxAppFacade.resolveOptional(payParam.getMchNo(), payParam.getChannelMchNo(), payParam.getCapability(),
                        payParam.getChannelAppId(), ProductEnum.LAKALA_PAY.getCode())
                .ifPresent(app -> payParam.setChannelAppId(app.wxAppId()));
    }
}
