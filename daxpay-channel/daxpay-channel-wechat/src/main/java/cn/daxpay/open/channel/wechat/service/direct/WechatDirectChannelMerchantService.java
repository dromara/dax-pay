package cn.daxpay.open.channel.wechat.service.direct;

import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectAppAuthConfigManager;
import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectAppCapabilityManager;
import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectAppManager;
import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectChannelMerchantManager;
import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectKeyConfigManager;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectApp;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectAppAuthConfig;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectAppCapability;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectChannelMerchant;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectKeyConfig;
import cn.daxpay.open.channel.wechat.param.direct.WechatDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.wechat.result.direct.WechatDirectChannelMerchantResult;
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

/// # 微信直连通道商户管理
///
/// 一个微信商户号(wxMchId)对应一个 channelMchNo, 商户的多个应用共享此绑定。
///
/// 同时作为通道商户扩展数据清理 SPI 实现（[ChannelMerchantCleanupService]），
/// 在通道商户删除时清理微信直连相关的所有扩展表。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatDirectChannelMerchantService implements ChannelMerchantCleanupService {

    private final ChannelMerchantManager channelMerchantManager;
    private final PayProductConfigManager payProductConfigManager;
    private final WechatDirectChannelMerchantManager wechatDirectChannelMerchantManager;
    private final WechatDirectKeyConfigManager wechatDirectAppKeyConfigManager;
    private final WechatDirectAppCapabilityManager wechatDirectAppCapabilityManager;
    private final WechatDirectAppManager wechatDirectAppManager;
    private final WechatDirectAppAuthConfigManager wechatDirectAppAuthConfigManager;

    /// 通道编码（对应 [ChannelEnum#WECHAT]）
    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT.getCode();
    }

    /// 清理指定通道商户号下微信直连的所有扩展数据
    ///
    /// 包含：直连扩展表、应用、应用密钥、应用能力、应用授权配置
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        wechatDirectChannelMerchantManager.deleteByField(WechatDirectChannelMerchant::getChannelMchNo, channelMchNo);
        wechatDirectAppManager.deleteByField(WechatDirectApp::getChannelMchNo, channelMchNo);
        wechatDirectAppKeyConfigManager.deleteByField(WechatDirectKeyConfig::getChannelMchNo, channelMchNo);
        wechatDirectAppCapabilityManager.deleteByField(WechatDirectAppCapability::getChannelMchNo, channelMchNo);
        wechatDirectAppAuthConfigManager.deleteByField(WechatDirectAppAuthConfig::getChannelMchNo, channelMchNo);
    }

    /// 创建微信直连通道商户
    @Transactional(rollbackFor = Exception.class)
    public void create(WechatDirectChannelMerchantCreateParam param) {
        // 校验同一商户下微信商户号不重复
        if (wechatDirectChannelMerchantManager.existsByMchNoAndWxMchId(
                param.getMchNo(), param.getWxMchId())) {
            // 微信: 同一商户下该微信商户已存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.wechat.directMchDuplicate");
        }
        // 生成通道商户号: 通道前缀 + 雪花ID(无分隔符, 仅供排障辨识)
        String channelMchNo = ChannelMchNoGenerateUtil.generate("WECHAT");
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
        // 写直连绑定表(wxMchId 作为业务字段, 不参与关联)
        var entity = new WechatDirectChannelMerchant();
        entity.setMchNo(param.getMchNo());
        entity.setChannelMchNo(channelMchNo);
        entity.setProduct(param.getProduct());
        entity.setWxMchId(param.getWxMchId());
        wechatDirectChannelMerchantManager.save(entity);
    }

    /// 根据通道商户号查询直连通道商户配置
    public WechatDirectChannelMerchantResult findByChannelMchNo(String channelMchNo) {
        return wechatDirectChannelMerchantManager.lambdaQuery()
                .eq(WechatDirectChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .map(WechatDirectChannelMerchant::toResult)
                // 微信: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
    }
}
