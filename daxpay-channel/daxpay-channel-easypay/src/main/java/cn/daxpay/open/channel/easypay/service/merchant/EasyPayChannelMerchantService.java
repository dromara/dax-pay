package cn.daxpay.open.channel.easypay.service.merchant;

import cn.daxpay.open.channel.easypay.dao.EasyPayKeyConfigManager;
import cn.daxpay.open.channel.easypay.entity.EasyPayKeyConfig;
import cn.daxpay.open.channel.easypay.param.EasyPayChannelMerchantCreateParam;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.masterdata.dao.product.PayProductConfigManager;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.util.ChannelMchNoGenerateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 易支付通道商户管理
///
/// 创建时写通用通道商户主表 + 预建 easy_pay_key_config 身份行(平台地址/商户ID随创建录入, 密钥后置配置)。
/// 同一商户下易支付商户ID(pid)唯一, 重复创建直接拒绝。
/// 易支付无集测环境, 沙箱标记仍按支付产品生效环境读取(实际恒为 false)。
///
/// 通道商户删除时的扩展数据清理由独立的策略类
/// [cn.daxpay.open.channel.easypay.strategy.merchant.EasyPayChannelMerchantCleanupStrategy] 承担。
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;

    private final PayProductConfigManager payProductConfigManager;

    private final EasyPayKeyConfigManager easyPayKeyConfigManager;

    /// 创建易支付通道商户
    ///
    /// 写表顺序: 通用通道商户主表(mch_channel_merchant) → 易支付密钥配置身份行(easy_pay_key_config, 密钥字段留空)。
    @Transactional(rollbackFor = Exception.class)
    public void create(EasyPayChannelMerchantCreateParam param) {
        // 可选字段空串归一为 NULL(前端未填时可能上送空串), 保证唯一性判断与落库语义一致
        String partnerId = StrUtil.trimToNull(param.getPartnerId());
        // 唯一性校验: 同一商户下易支付商户ID不重复
        var query = easyPayKeyConfigManager.lambdaQuery()
                .eq(EasyPayKeyConfig::getMchNo, param.getMchNo())
                .eq(EasyPayKeyConfig::getPartnerId, partnerId);
        if (query.exists()) {
            // 易支付: 该商户下已存在此易支付商户ID
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.easypay.mchDuplicate");
        }
        // 生成通道商户号: 通道前缀 + 雪花ID
        String channelMchNo = ChannelMchNoGenerateUtil.generate("EASY");
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
        // 预建易支付密钥配置身份行, 平台地址与商户ID随创建录入, 密钥字段留空由密钥配置后置维护
        var keyConfig = new EasyPayKeyConfig()
                .setChannelMchNo(channelMchNo)
                .setServerUrl(StrUtil.removeSuffix(param.getServerUrl(), "/"))
                .setPartnerId(partnerId);
        // 运营端写入必须显式 setMchNo(父类 setter 返回父类型, 不链式)
        keyConfig.setMchNo(param.getMchNo());
        easyPayKeyConfigManager.save(keyConfig);
    }
}
