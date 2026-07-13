package cn.daxpay.open.channel.ums.service.direct;

import cn.daxpay.open.channel.ums.dao.direct.UmsDirectKeyConfigManager;
import cn.daxpay.open.channel.ums.entity.direct.UmsDirectKeyConfig;
import cn.daxpay.open.channel.ums.param.direct.UmsDirectChannelMerchantCreateParam;
import cn.daxpay.open.payment.channel.dao.ChannelMerchantManager;
import cn.daxpay.open.payment.channel.entity.ChannelMerchant;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.platform.core.util.ChannelMchNoGenerateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 银联商务直连通道商户管理
///
/// 管理通道商户绑定的创建/删除。
/// 商户身份(mid)在创建时录入直连配置表,
/// 应用ID(umsAppId)/终端号(tid)/应用密钥(appKey)/通讯密钥(secretKey)由密钥配置单独维护,
/// 沙箱环境运行时读取支付产品配置(pay_md_product_config.activeEnv)。
@Slf4j
@Service
@RequiredArgsConstructor
public class UmsDirectChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final UmsDirectKeyConfigManager umsDirectKeyConfigManager;

    /// 创建通道商户绑定
    ///
    /// 同时写入通用通道商户主表和银联商务直连配置表(含商户号 mid)。
    /// umsAppId/terminalNo/appKey/secretKey 由密钥配置单独维护。
    @Transactional(rollbackFor = Exception.class)
    public void create(UmsDirectChannelMerchantCreateParam param) {
        // 生成通道商户号: 通道前缀 + 雪花ID(无分隔符, 仅供排障辨识)
        String channelMchNo = ChannelMchNoGenerateUtil.generate("UMSPAY");
        // 写通用通道商户主表
        ChannelMerchant channelMerchant = new ChannelMerchant();
        channelMerchant.setMchNo(param.getMchNo());
        channelMerchant.setChannelMchNo(channelMchNo);
        channelMerchant.setChannelMerchantName(param.getChannelMerchantName());
        channelMerchant.setProduct(param.getProduct());
        channelMerchant.setSource(ChannelMerchantSourceEnum.MANUAL.getCode());
        channelMerchant.setEnable(true);
        channelMerchantManager.save(channelMerchant);
        // 写银联商务直连配置(仅商户身份 mid, umsAppId/terminalNo/appKey/secretKey 由密钥配置单独维护)
        var keyConfig = new UmsDirectKeyConfig()
                .setChannelMchNo(channelMchNo)
                .setMerchantNo(param.getMerchantNo());
        // mchNo 继承自 MchBaseEntity, 链式返回父类型, 单独赋值
        keyConfig.setMchNo(param.getMchNo());
        umsDirectKeyConfigManager.save(keyConfig);
    }

    /// 根据通道商户号删除(级联删除直连配置)
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        umsDirectKeyConfigManager.deleteByChannelMchNo(channelMchNo);
    }
}
