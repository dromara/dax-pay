package cn.daxpay.open.channel.douyin.service.direct;

import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectAppAuthConfigManager;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectAppCapabilityManager;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectAppManager;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectChannelMerchantManager;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectKeyConfigManager;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectApp;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectAppAuthConfig;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectAppCapability;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectChannelMerchant;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectKeyConfig;
import cn.daxpay.open.channel.douyin.param.direct.DouyinDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.douyin.result.direct.DouyinDirectChannelMerchantResult;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.service.channel.ChannelMerchantCleanupService;
import cn.daxpay.open.payment.masterdata.dao.product.PayProductConfigManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.util.ChannelMchNoGenerateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 抖音直连通道商户管理
///
/// 一个抖音商户号(dyMchId)对应一个 channelMchNo, 商户的多个应用共享此绑定。
///
/// 同时作为通道商户扩展数据清理 SPI 实现（[ChannelMerchantCleanupService]），
/// 在通道商户删除时清理抖音直连相关的所有扩展表。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinDirectChannelMerchantService implements ChannelMerchantCleanupService {

    private final ChannelMerchantManager channelMerchantManager;
    private final PayProductConfigManager payProductConfigManager;
    private final DouyinDirectChannelMerchantManager douyinDirectChannelMerchantManager;
    private final DouyinDirectKeyConfigManager douyinDirectAppKeyConfigManager;
    private final DouyinDirectAppCapabilityManager douyinDirectAppCapabilityManager;
    private final DouyinDirectAppManager douyinDirectAppManager;
    private final DouyinDirectAppAuthConfigManager douyinDirectAppAuthConfigManager;

    /// 通道编码（对应 [ChannelEnum#DOUYIN]）
    @Override
    public String getChannel() {
        return ChannelEnum.DOUYIN.getCode();
    }

    /// 清理指定通道商户号下抖音直连的所有扩展数据
    ///
    /// 包含：直连扩展表、应用、应用密钥、应用能力、应用授权配置
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        douyinDirectChannelMerchantManager.deleteByField(DouyinDirectChannelMerchant::getChannelMchNo, channelMchNo);
        douyinDirectAppManager.deleteByField(DouyinDirectApp::getChannelMchNo, channelMchNo);
        douyinDirectAppKeyConfigManager.deleteByField(DouyinDirectKeyConfig::getChannelMchNo, channelMchNo);
        douyinDirectAppCapabilityManager.deleteByField(DouyinDirectAppCapability::getChannelMchNo, channelMchNo);
        douyinDirectAppAuthConfigManager.deleteByField(DouyinDirectAppAuthConfig::getChannelMchNo, channelMchNo);
    }

    /// 创建抖音直连通道商户
    @Transactional(rollbackFor = Exception.class)
    public void create(DouyinDirectChannelMerchantCreateParam param) {
        // 校验同一商户下抖音商户号不重复
        if (douyinDirectChannelMerchantManager.existsByMchNoAndDyMchId(
                param.getMchNo(), param.getDyMchId())) {
            // 抖音: 同一商户下该抖音商户已存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.douyin.directMchDuplicate");
        }
        // 生成通道商户号: 通道前缀 + 雪花ID(无分隔符, 仅供排障辨识)
        String channelMchNo = ChannelMchNoGenerateUtil.generate("DOUYIN");
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
        // 写直连绑定表(dyMchId 作为业务字段)
        var entity = new DouyinDirectChannelMerchant();
        entity.setMchNo(param.getMchNo());
        entity.setChannelMchNo(channelMchNo);
        entity.setProduct(param.getProduct());
        entity.setDyMchId(param.getDyMchId());
        douyinDirectChannelMerchantManager.save(entity);
    }

    /// 根据通道商户号查询直连通道商户配置
    public DouyinDirectChannelMerchantResult findByChannelMchNo(String channelMchNo) {
        return douyinDirectChannelMerchantManager.lambdaQuery()
                .eq(DouyinDirectChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .map(DouyinDirectChannelMerchant::toResult)
                // 通道: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
    }
}
