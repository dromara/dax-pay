package cn.daxpay.open.channel.lakala.service.isv;

import cn.daxpay.open.channel.lakala.client.credential.LakalaSdkCredential;
import cn.daxpay.open.channel.lakala.dao.isv.LakalaIsvChannelMerchantManager;
import cn.daxpay.open.channel.lakala.entity.isv.LakalaIsvChannelMerchant;
import cn.daxpay.open.channel.lakala.entity.isv.LakalaIsvKeyConfig;
import cn.daxpay.open.payment.masterdata.constants.product.dao.PayProductConfigManager;
import cn.daxpay.open.payment.masterdata.constants.product.entity.PayProductConfig;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.config.PayEnvEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 拉卡拉服务商通道凭证组装器
///
/// 从服务商密钥配置([LakalaIsvKeyConfig]) + 通道商户绑定([LakalaIsvChannelMerchant]) 组装通道调用凭证,
/// 下发给子应用 dax-pay-channel-two 发起拉卡拉 API 调用。
///
/// 字段映射(对齐拉卡拉 V3 接口):
/// - lkl_app_id ← [LakalaIsvKeyConfig.lklAppId] (拉卡拉应用编号, 全局唯一)
/// - merchant_no ← [LakalaIsvChannelMerchant.lakalaMchNo] (拉卡拉商户号)
/// - term_no ← [LakalaIsvChannelMerchant.termNo] (终端号)
/// - 私钥/公钥/证书序列号 ← [LakalaIsvKeyConfig] (服务商级, 全局唯一)
///
/// 供支付策略([cn.daxpay.open.channel.lakala.strategy.*])组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaIsvConfigAssembler {

    private final LakalaIsvChannelMerchantManager lakalaIsvChannelMerchantManager;
    private final LakalaIsvKeyConfigService lakalaIsvKeyConfigService;
    private final PayProductConfigManager payProductConfigManager;

    /// 组装拉卡拉通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(保留参数对齐签名, 服务商密钥全局唯一不依赖此字段)
    /// @param channelMchNo 通道商户号(拉卡拉商户绑定主键)
    /// @param capability   支付能力编码(保留参数, 拉卡拉不按能力路由)
    /// @return 拉卡拉 SDK 凭证(含服务商密钥 + 商户号/终端号)
    public LakalaSdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 沙箱状态读取支付产品配置的生效环境
        boolean sandbox = payProductConfigManager.findByProduct(ProductEnum.LAKALA_PAY.getCode())
                .map(PayProductConfig::getActiveEnv)
                .map(PayEnvEnum.SANDBOX.getCode()::equals)
                .orElse(false);
        // 服务商密钥(按生效环境取对应环境密钥, 含 lkl_app_id + 私钥/公钥; 缺失或关键字段为空时 fail-fast)
        LakalaIsvKeyConfig keyConfig = lakalaIsvKeyConfigService.getByProductForPay(ProductEnum.LAKALA_PAY.getCode(), sandbox);
        // 通道商户绑定(取 merchantNo + termNo)
        LakalaIsvChannelMerchant channelMerchant = lakalaIsvChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // 拉卡拉: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));

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
}
