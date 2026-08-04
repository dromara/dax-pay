package cn.daxpay.open.channel.stripe.service;

import cn.daxpay.open.channel.stripe.dao.StripeChannelMerchantManager;
import cn.daxpay.open.channel.stripe.entity.StripeChannelMerchant;
import cn.daxpay.open.channel.stripe.param.StripeChannelMerchantCreateParam;
import cn.daxpay.open.channel.stripe.result.StripeChannelMerchantResult;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.masterdata.dao.product.PayProductConfigManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.util.ChannelMchNoGenerateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # Stripe 通道商户管理
///
/// 一个 Stripe 账户(accountId)对应一个 channelMchNo, 商户的多个应用共享此绑定。
///
/// 通道商户删除时的扩展数据清理由独立的策略类
/// [cn.daxpay.open.channel.stripe.strategy.merchant.StripeChannelMerchantCleanupStrategy] 承担。
@Slf4j
@Service
@RequiredArgsConstructor
public class StripeChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final PayProductConfigManager payProductConfigManager;
    private final StripeChannelMerchantManager stripeChannelMerchantManager;

    /// 创建 Stripe 通道商户
    @Transactional(rollbackFor = Exception.class)
    public void create(StripeChannelMerchantCreateParam param) {
        // 校验同一商户下 Stripe 账户不重复
        if (stripeChannelMerchantManager.existsByMchNoAndAccountId(
                param.getMchNo(), param.getAccountId())) {
            // Stripe: 同一商户下该 Stripe 账户已存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.stripe.mchDuplicate");
        }
        // 生成通道商户号: 通道前缀 + 雪花ID(无分隔符, 仅供排障辨识)
        String channelMchNo = ChannelMchNoGenerateUtil.generate("STRIPE");
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
        // 写绑定表(accountId 作为业务字段)
        var entity = new StripeChannelMerchant();
        entity.setMchNo(param.getMchNo());
        entity.setChannelMchNo(channelMchNo);
        entity.setProduct(param.getProduct());
        entity.setAccountId(param.getAccountId());
        stripeChannelMerchantManager.save(entity);
    }

    /// 根据通道商户号查询通道商户配置
    public StripeChannelMerchantResult findByChannelMchNo(String channelMchNo) {
        return stripeChannelMerchantManager.lambdaQuery()
                .eq(StripeChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .map(StripeChannelMerchant::toResult)
                // 通道: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
    }
}
