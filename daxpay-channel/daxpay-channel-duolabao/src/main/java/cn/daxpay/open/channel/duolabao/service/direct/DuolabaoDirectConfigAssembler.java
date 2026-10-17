package cn.daxpay.open.channel.duolabao.service.direct;

import cn.daxpay.open.channel.duolabao.client.credential.DuolabaoSdkCredential;
import cn.daxpay.open.channel.duolabao.entity.direct.DuolabaoDirectKeyConfig;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 哆啦宝直连通道凭证组装器
///
/// 从通用通道商户主表([ChannelMerchant])读取 product 与固化 sandbox 标识,
/// 从直连配置([DuolabaoDirectKeyConfig])读取商户编号/店铺编号/代理商编号与 accessKey/secretKey 密钥对,
/// 组装为下发给子应用的通道调用凭证 [DuolabaoSdkCredential]。
///
/// 沙箱标识直接读通道商户固化的 [ChannelMerchant#isSandbox]
/// (创建时按当时产品 activeEnv 写入, 不随产品切换改变)。
@Slf4j
@Service
@RequiredArgsConstructor
public class DuolabaoDirectConfigAssembler {

    private final DuolabaoDirectKeyConfigService keyConfigService;

    private final ChannelMerchantManager channelMerchantManager;

    /// 组装直连商户的通道调用凭证(下发给子应用)
    ///
    /// @param mchNo        商户号(定位通用通道商户主表)
    /// @param channelMchNo 通道商户号(定位直连配置)
    /// @param capability   支付能力编码(哆啦宝不使用, 保留对齐签名)
    /// @return 哆啦宝 SDK 凭证
    public DuolabaoSdkCredential buildConfig(String mchNo, String channelMchNo, String capability) {
        // 1. 通用通道商户主表(取 sandbox 固化标识)
        ChannelMerchant channelMerchant = channelMerchantManager.findByMchNoAndChannelMchNo(mchNo, channelMchNo)
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        // 2. 沙箱标识直接读通道商户固化的快照(创建时按当时产品 activeEnv 写入, 不随产品切换改变)
        boolean sandbox = channelMerchant.isSandbox();
        // 3. 直连配置(商户身份 + 密钥对, 按 sandbox 取对应环境密钥)
        DuolabaoDirectKeyConfig keyConfig = keyConfigService.findByChannelMchNo(channelMchNo, sandbox);
        if (keyConfig.getAccessKey() == null || keyConfig.getAccessKey().isBlank()
                || keyConfig.getSecretKey() == null || keyConfig.getSecretKey().isBlank()) {
            // 哆啦宝: 通道密钥未配置
            throw new BizInfoException("error.channel.duolabao.keyNotConfigured");
        }

        // 4. 组装凭证
        var credential = new DuolabaoSdkCredential();
        credential.setCustomerNum(keyConfig.getCustomerNum());
        credential.setShopNum(keyConfig.getShopNum());
        credential.setAgentNum(keyConfig.getAgentNum());
        credential.setAccessKey(keyConfig.getAccessKey());
        credential.setSecretKey(keyConfig.getSecretKey());
        credential.setSandbox(sandbox);
        return credential;
    }
}
