package cn.daxpay.open.channel.douyin.service.direct;

import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectChannelMerchantManager;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectChannelMerchant;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectKeyConfig;
import cn.daxpay.open.payment.auth.core.AppScopeEnum;
import cn.daxpay.open.payment.douyin.facade.DouyinAppFacade;
import cn.daxpay.open.payment.douyin.facade.DyAppView;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 抖音直连通道凭证组装器
///
/// 从进件对象读取 appId / mchId / 私钥 / 证书序列号 / 加密密钥, 组装为下发给子应用的通道调用凭证 [DouyinSdkCredential]。
///
/// 应用解析(获取 douyinAppId)委托 [DouyinAppFacade#resolve]:显式 channelAppId → 通道能力绑 →(直连)商户档 appType 推导,
/// 应用主数据已上移至商户/平台级(dy_mch_app / dy_platform_app), 通道商户下不再持有抖音应用。
/// 密钥/证书(douyin_direct_key_config)与通道商户绑定(dyMchId)仍保留在通道商户维度。
///
/// 供抖音直连策略([cn.daxpay.open.channel.douyin.strategy.DouyinDirectPayStrategy] 等)组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinDirectConfigAssembler {

    private final DouyinAppFacade douyinAppFacade;
    private final DouyinDirectKeyConfigService keyConfigService;
    private final DouyinDirectChannelMerchantManager channelMerchantManager;

    /// 组装直连商户的通道调用凭证(下发给子应用)
    ///
    /// @param mchNo         商户号(应用解析的商户档隔离条件)
    /// @param channelMchNo  通道商户号(定位密钥/商户绑定/能力绑)
    /// @param capability    支付能力编码(用于选择匹配的应用)
    /// @param channelAppId  通道应用 AppId(订单快照或调用方显式指定; 非空时优先于能力绑解析,
    ///                      保证关单/退款/同步/分账与下单使用同一应用, 不随能力绑定配置变更漂移)
    /// @return 抖音 SDK 凭证, 字段对齐子应用 DouyinSdkCredential
    public DouyinSdkCredential buildConfig(String mchNo, String channelMchNo, String capability, String channelAppId) {
        // 1. 解析支付使用的应用(传直连产品标识, 激活 resolve 内商户档隔离: 仅商户表/能力绑 merchant 档/商户档 appType 推导)
        DyAppView app = douyinAppFacade.resolve(mchNo, channelMchNo, capability, channelAppId,
                ProductEnum.DOUYIN_PAY.getCode());

        // 2-4. 组装凭证
        return assembleCredential(app, channelMchNo);
    }

    /// 分账接收方绑定专用凭证组装(不经 capability, 显式指定应用)
    ///
    /// 接收方绑定无支付能力维度, 抖音 addSplitReceiver 要求 app_id,
    /// 且 openid 类型账号为 app_id 维度, 故由绑定记录显式指定商户档应用 appid。
    ///
    /// @param mchNo        商户号(应用解析的商户档隔离条件)
    /// @param channelMchNo 通道商户号(定位密钥/商户绑定)
    /// @param channelAppId 绑定所用商户档抖音应用 appid
    /// @return 抖音 SDK 凭证, douyinAppId 来自显式指定的商户档应用
    public DouyinSdkCredential buildAllocReceiverConfig(String mchNo, String channelMchNo, String channelAppId) {
        // 显式 appid 解析商户档应用(直连产品语义: 仅商户表, 优先于能力绑)
        DyAppView app = douyinAppFacade.resolve(mchNo, channelMchNo, null, channelAppId,
                ProductEnum.DOUYIN_PAY.getCode());
        // 2-4. 组装凭证
        return assembleCredential(app, channelMchNo);
    }

    /// 组装转账使用的通道调用凭证(下发给子应用)
    ///
    /// 转账发起应用由「抖音转账配置」显式指定(网站应用, 支持手机H5获取OpenId), 不走支付能力绑定解析,
    /// 与支付链路([#buildConfig])的应用解析相互独立。
    ///
    /// @param mchNo            商户号(应用归属校验)
    /// @param channelMchNo     通道商户号(定位密钥/商户绑定)
    /// @param transferAppRefId 转账发起应用引用(dy_mch_app 主键)
    /// @return 抖音 SDK 凭证, 字段对齐子应用 DouyinSdkCredential
    public DouyinSdkCredential buildTransferConfig(String mchNo, String channelMchNo, Long transferAppRefId) {
        // 1. 按引用加载转账发起应用(仅商户档, 直连商户不使用平台应用)
        DyAppView app = douyinAppFacade.getById(AppScopeEnum.MERCHANT, transferAppRefId);
        // 2-4. 组装凭证
        return assembleCredential(app, channelMchNo);
    }

    /// 读取通道商户绑定与密钥配置, 组装凭证(第 2-4 步公共部分)
    private DouyinSdkCredential assembleCredential(DyAppView app, String channelMchNo) {
        // 2. 读取通道商户绑定(获取抖音商户号 dyMchId 作为 mchId)
        DouyinDirectChannelMerchant merchant = channelMerchantManager.lambdaQuery()
                .eq(DouyinDirectChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));

        // 3. 读取密钥配置(私钥 / 证书序列号 / 加密密钥)
        DouyinDirectKeyConfig keyConfig = keyConfigService.findByChannelMchNo(channelMchNo);

        // 4. 组装凭证
        var credential = new DouyinSdkCredential();
        credential.setDouyinAppId(app.douyinAppId());
        credential.setMchId(merchant.getDyMchId());
        credential.setMerchantSerialNumber(keyConfig.getMerchantSerialNumber());
        credential.setMerchantPrivateKey(keyConfig.getMerchantPrivateKey());
        credential.setEncryptKey(keyConfig.getEncryptKey());
        return credential;
    }
}
