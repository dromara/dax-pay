package cn.daxpay.open.channel.fubei.service.direct;

import cn.daxpay.open.channel.fubei.code.FubeiCode;
import cn.daxpay.open.channel.fubei.dao.direct.FubeiDirectKeyConfigManager;
import cn.daxpay.open.channel.fubei.entity.direct.FubeiDirectKeyConfig;
import cn.daxpay.open.channel.fubei.param.direct.FubeiDirectChannelMerchantCreateParam;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.masterdata.dao.product.PayProductConfigManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.platform.core.util.ChannelMchNoGenerateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 付呗直连通道商户管理
///
/// 管理通道商户绑定的创建/删除。
/// 商户身份(开放平台应用ID/商户ID/门店ID)在创建时录入直连配置表,
/// 接口密钥(app_secret)由密钥配置单独维护, 沙箱环境运行时读取支付产品配置(pay_md_product_config.activeEnv)。
///
/// 通道商户删除时的扩展数据清理由独立的策略类
/// [cn.daxpay.open.channel.fubei.strategy.merchant.FubeiDirectChannelMerchantCleanupStrategy] 承担。
@Slf4j
@Service
@RequiredArgsConstructor
public class FubeiDirectChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final PayProductConfigManager payProductConfigManager;
    private final FubeiDirectKeyConfigManager fubeiDirectKeyConfigManager;

    /// 创建通道商户绑定
    ///
    /// 同时写入通用通道商户主表和付呗直连配置表(含开放平台应用ID/商户ID/门店ID)。
    /// 接口密钥由密钥配置单独维护。
    @Transactional(rollbackFor = Exception.class)
    public void create(FubeiDirectChannelMerchantCreateParam param) {
        // 生成通道商户号: 通道前缀 + 雪花ID
        String channelMchNo = ChannelMchNoGenerateUtil.generate(FubeiCode.CHANNEL_MCH_NO_PREFIX);
        // 写通用通道商户主表
        ChannelMerchant channelMerchant = new ChannelMerchant();
        channelMerchant.setMchNo(param.getMchNo());
        channelMerchant.setChannelMchNo(channelMchNo);
        channelMerchant.setChannelMerchantName(param.getChannelMerchantName());
        channelMerchant.setProduct(param.getProduct());
        channelMerchant.setSource(ChannelMerchantSourceEnum.MANUAL.getCode());
        channelMerchant.setEnable(true);
        // 沙箱标记从支付产品生效环境同步写入, 禁止商户/表单设置
        boolean sandbox = payProductConfigManager.isSandboxActive(param.getProduct());
        channelMerchant.setSandbox(sandbox);
        channelMerchantManager.save(channelMerchant);
        // 写付呗直连配置(仅商户身份, 接口密钥由密钥配置单独维护)
        var keyConfig = new FubeiDirectKeyConfig()
                .setChannelMchNo(channelMchNo)
                .setAppId(param.getAppId())
                .setMerchantId(param.getMerchantId())
                .setStoreId(param.getStoreId());
        // mchNo 继承自 MchBaseEntity, 链式返回父类型, 单独赋值
        keyConfig.setMchNo(param.getMchNo());
        fubeiDirectKeyConfigManager.save(keyConfig);
    }
}
