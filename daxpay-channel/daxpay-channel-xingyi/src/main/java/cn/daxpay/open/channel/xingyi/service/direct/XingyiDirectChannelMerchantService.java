package cn.daxpay.open.channel.xingyi.service.direct;

import cn.daxpay.open.channel.xingyi.code.XingyiCode;
import cn.daxpay.open.channel.xingyi.dao.direct.XingyiDirectKeyConfigManager;
import cn.daxpay.open.channel.xingyi.entity.direct.XingyiDirectKeyConfig;
import cn.daxpay.open.channel.xingyi.param.direct.XingyiDirectChannelMerchantCreateParam;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.masterdata.dao.product.PayProductConfigManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.platform.core.util.ChannelMchNoGenerateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 星驿付直连通道商户管理
///
/// 管理通道商户绑定的创建。商户身份(机构号/星驿付商户号)在创建时录入直连配置表,
/// 商户 RSA 公钥由密钥配置单独维护,
/// 沙箱环境运行时读取支付产品配置(pay_md_product_config.activeEnv)。
///
/// 通道商户删除时的扩展数据清理由独立的策略类
/// [cn.daxpay.open.channel.xingyi.strategy.merchant.XingyiDirectChannelMerchantCleanupStrategy] 承担。
@Slf4j
@Service
@RequiredArgsConstructor
public class XingyiDirectChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;

    private final PayProductConfigManager payProductConfigManager;

    private final XingyiDirectKeyConfigManager xingyiDirectKeyConfigManager;

    /// 创建通道商户绑定
    ///
    /// 同时写入通用通道商户主表和星驿付直连配置表(含机构号/星驿付商户号)。
    /// 商户公钥由密钥配置单独维护。
    @Transactional(rollbackFor = Exception.class)
    public void create(XingyiDirectChannelMerchantCreateParam param) {
        // 生成通道商户号: 通道前缀 + 雪花ID
        String channelMchNo = ChannelMchNoGenerateUtil.generate(XingyiCode.CHANNEL_MCH_NO_PREFIX);
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
        // 写星驿付直连配置(仅商户身份, 公钥由密钥配置单独维护), 两环境各预建一条
        this.saveKeyConfig(channelMchNo, param.getMchNo(), param.getAgetId(), param.getCustId(), true);
        this.saveKeyConfig(channelMchNo, param.getMchNo(), param.getAgetId(), param.getCustId(), false);
    }

    /// 写入直连配置记录(身份字段)
    private void saveKeyConfig(String channelMchNo, String mchNo, String agetId, String custId, boolean sandbox) {
        var keyConfig = new XingyiDirectKeyConfig()
                .setChannelMchNo(channelMchNo)
                .setAgetId(agetId)
                .setCustId(custId)
                .setSandbox(sandbox);
        // mchNo 继承自 MchBaseEntity, 链式返回父类型, 单独赋值
        keyConfig.setMchNo(mchNo);
        xingyiDirectKeyConfigManager.save(keyConfig);
    }
}
