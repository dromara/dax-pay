package cn.daxpay.open.channel.epaylinks.service.direct;

import cn.daxpay.open.channel.epaylinks.code.EpaylinksCode;
import cn.daxpay.open.channel.epaylinks.dao.direct.EpaylinksDirectKeyConfigManager;
import cn.daxpay.open.channel.epaylinks.entity.direct.EpaylinksDirectKeyConfig;
import cn.daxpay.open.channel.epaylinks.param.direct.EpaylinksDirectChannelMerchantCreateParam;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.masterdata.dao.product.PayProductConfigManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.platform.core.util.ChannelMchNoGenerateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 易票联直连通道商户管理
///
/// 管理通道商户绑定的创建/删除。
/// 商户身份(易票联商户编号/上游商户号)在创建时录入直连配置表,
/// 密钥三元组(证书序列号/商户私钥/平台公钥证书)由密钥配置单独维护,
/// 沙箱环境运行时读取支付产品配置(pay_md_product_config.activeEnv)。
///
/// 通道商户删除时的扩展数据清理由独立的策略类
/// [cn.daxpay.open.channel.epaylinks.strategy.merchant.EpaylinksDirectChannelMerchantCleanupStrategy] 承担。
@Slf4j
@Service
@RequiredArgsConstructor
public class EpaylinksDirectChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final PayProductConfigManager payProductConfigManager;
    private final EpaylinksDirectKeyConfigManager epaylinksDirectKeyConfigManager;

    /// 创建通道商户绑定
    ///
    /// 同时写入通用通道商户主表和易票联直连配置表(含商户编号/上游商户号)。
    /// 密钥三元组由密钥配置单独维护。
    @Transactional(rollbackFor = Exception.class)
    public void create(EpaylinksDirectChannelMerchantCreateParam param) {
        // 生成通道商户号: 通道前缀 + 雪花ID
        String channelMchNo = ChannelMchNoGenerateUtil.generate(EpaylinksCode.CHANNEL_MCH_NO_PREFIX);
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
        // 写易票联直连配置(仅商户身份, 密钥三元组由密钥配置单独维护)
        var keyConfig = new EpaylinksDirectKeyConfig()
                .setChannelMchNo(channelMchNo)
                .setCustomerCode(param.getCustomerCode())
                .setChannelMchtNo(param.getChannelMchtNo());
        // mchNo 继承自 MchBaseEntity, 链式返回父类型, 单独赋值
        keyConfig.setMchNo(param.getMchNo());
        epaylinksDirectKeyConfigManager.save(keyConfig);
    }
}
