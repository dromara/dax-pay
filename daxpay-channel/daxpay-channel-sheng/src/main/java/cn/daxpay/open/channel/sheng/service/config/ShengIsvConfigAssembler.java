package cn.daxpay.open.channel.sheng.service.config;

import cn.daxpay.open.channel.sheng.client.credential.ShengSdkCredential;
import cn.daxpay.open.channel.sheng.dao.ShengIsvChannelMerchantManager;
import cn.daxpay.open.channel.sheng.entity.ShengIsvChannelMerchant;
import cn.daxpay.open.channel.sheng.entity.ShengIsvKeyConfig;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 盛付通服务商(ISV)通道凭证组装器
///
/// 从服务商密钥配置([ShengIsvKeyConfig], 按产品编码定位, 产品级全局一份) +
/// 子商户绑定行([ShengIsvChannelMerchant], 按通道商户号定位)组装通道调用凭证 [ShengSdkCredential],
/// 下发给子应用 dax-pay-channel-two 以服务商身份代子商户发起盛付通 API 调用。
///
/// 字段映射(对齐盛付通聚合 API 服务商模式):
/// - mchId ← [ShengIsvKeyConfig#getShengMchId] (服务商盛付通商户号, 全局唯一)
/// - subMchId ← [ShengIsvChannelMerchant#getSubMchId] (子商户号, 按绑定行)
/// - sdpAppId ← [ShengIsvChannelMerchant#getSdpAppId] (子商户应用ID, 按绑定行)
/// - 私钥/公钥 ← [ShengIsvKeyConfig] (服务商级, 全局唯一)
///
/// 聚合 API 商户/服务商两模式同网关同端点同签名, 子应用零改动。
/// 盛付通不提供集测接口, 无沙箱双环境(官方不提供集测接口), 凭证不携带沙箱标记。
/// 盛付通为聚合支付, 不区分应用能力(capability 参数保留但不使用)。
///
/// 供支付策略([cn.daxpay.open.channel.sheng.strategy.*])组装通道调用凭证。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengIsvConfigAssembler {

    private final ShengIsvChannelMerchantManager shengIsvChannelMerchantManager;
    private final ShengIsvKeyConfigService shengIsvKeyConfigService;

    /// 组装盛付通服务商通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(保留参数对齐签名, 凭证按通道商户号 + 产品级密钥定位)
    /// @param channelMchNo 通道商户号(定位子商户绑定行)
    /// @param capability   支付能力编码(盛付通不按能力路由, 保留对齐签名)
    /// @return 盛付通 SDK 凭证(服务商身份与密钥 + 子商户身份)
    public ShengSdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 子商户绑定行(取 subMchId/sdpAppId; 绑定行不存在 fail-fast)
        ShengIsvChannelMerchant channelMerchant = shengIsvChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // 通道商户信息不存在(复用支付域通用文案)
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        // 服务商密钥(产品级全局一份, 关键字段缺失 fail-fast)
        ShengIsvKeyConfig keyConfig = shengIsvKeyConfigService.getByProductForPay(ProductEnum.SHENG_ISV.getCode());

        // 组装凭证: 服务商身份与密钥 + 子商户身份
        ShengSdkCredential credential = new ShengSdkCredential();
        credential.setMchId(keyConfig.getShengMchId());
        credential.setSubMchId(channelMerchant.getSubMchId());
        credential.setSdpAppId(channelMerchant.getSdpAppId());
        credential.setMerchantPrivateKey(keyConfig.getMerchantPrivateKey());
        credential.setShengpayPublicKey(keyConfig.getShengpayPublicKey());
        return credential;
    }
}
