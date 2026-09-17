package cn.daxpay.open.channel.sheng.service.merchant;

import cn.daxpay.open.channel.sheng.dao.ShengKeyConfigManager;
import cn.daxpay.open.channel.sheng.entity.ShengKeyConfig;
import cn.daxpay.open.channel.sheng.param.ShengChannelMerchantCreateParam;
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

/// # 盛付通通道商户管理
///
/// 创建时写通用通道商户主表 + 预建 sheng_key_config 身份行(密钥后置配置)。
/// 同一商户下盛付通商户号唯一, 重复创建直接拒绝。
/// 盛付通无沙箱环境, 沙箱标记仍按支付产品生效环境读取(实际恒为 false, 与 adapay/dougong/douyin 同款写法)。
///
/// 通道商户删除时的扩展数据清理由独立的策略类
/// [cn.daxpay.open.channel.sheng.strategy.merchant.ShengChannelMerchantCleanupStrategy] 承担。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final PayProductConfigManager payProductConfigManager;
    private final ShengKeyConfigManager shengKeyConfigManager;

    /// 创建盛付通通道商户
    ///
    /// 写表顺序: 通用通道商户主表(mch_channel_merchant) → 盛付通密钥配置身份行(sheng_key_config, 密钥字段留空)。
    @Transactional(rollbackFor = Exception.class)
    public void create(ShengChannelMerchantCreateParam param) {
        // 可选字段空串归一为 NULL(前端未填时可能上送空串), 保证唯一性判断与落库语义一致
        String shengMchId = StrUtil.trimToNull(param.getShengMchId());
        String sdpAppId = StrUtil.trimToNull(param.getSdpAppId());
        // 唯一性校验: 同一商户下盛付通商户号不重复
        var query = shengKeyConfigManager.lambdaQuery()
                .eq(ShengKeyConfig::getMchNo, param.getMchNo())
                .eq(ShengKeyConfig::getShengMchId, shengMchId);
        if (query.exists()) {
            // 盛付通: 该商户下已存在此盛付通商户号
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.sheng.mchDuplicate");
        }
        // 生成通道商户号: 通道前缀 + 雪花ID
        String channelMchNo = ChannelMchNoGenerateUtil.generate("SHENG");
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
        // 预建盛付通密钥配置身份行, 商户身份随创建录入, 密钥字段留空由密钥配置后置维护
        var keyConfig = new ShengKeyConfig()
                .setChannelMchNo(channelMchNo)
                .setShengMchId(shengMchId)
                .setSdpAppId(sdpAppId);
        // 运营端写入必须显式 setMchNo(父类 setter 返回父类型, 不链式)
        keyConfig.setMchNo(param.getMchNo());
        shengKeyConfigManager.save(keyConfig);
    }
}
