package cn.daxpay.open.channel.alipay.service.direct;

import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectAppAuthConfigManager;
import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectAppCapabilityManager;
import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectAppKeyConfigManager;
import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectAppManager;
import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectChannelMerchantManager;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectApp;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectAppAuthConfig;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectAppCapability;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectAppKeyConfig;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectChannelMerchant;
import cn.daxpay.open.channel.alipay.param.direct.AlipayDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectChannelMerchantResult;
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

/// # 支付宝直连通道商户管理
///
/// 一个商户PID对应一个channelMchNo, 商户的多个应用共享此绑定。
///
/// 同时作为通道商户扩展数据清理 SPI 实现（[ChannelMerchantCleanupService]），
/// 在通道商户删除时清理支付宝直连相关的所有扩展表。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayDirectChannelMerchantService implements ChannelMerchantCleanupService {

    private final ChannelMerchantManager channelMerchantManager;
    private final PayProductConfigManager payProductConfigManager;
    private final AlipayDirectChannelMerchantManager alipayDirectChannelMerchantManager;
    private final AlipayDirectAppKeyConfigManager alipayDirectAppKeyConfigManager;
    private final AlipayDirectAppCapabilityManager alipayDirectAppCapabilityManager;
    private final AlipayDirectAppManager alipayDirectAppManager;
    private final AlipayDirectAppAuthConfigManager alipayDirectAppAuthConfigManager;

    /// 通道编码（对应 [ChannelEnum#ALIPAY]）
    @Override
    public String getChannel() {
        return ChannelEnum.ALIPAY.getCode();
    }

    /// 清理指定通道商户号下支付宝直连的所有扩展数据
    ///
    /// 包含：直连扩展表、应用、应用密钥、应用能力、应用授权配置
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        alipayDirectChannelMerchantManager.deleteByField(AlipayDirectChannelMerchant::getChannelMchNo, channelMchNo);
        alipayDirectAppManager.deleteByField(AlipayDirectApp::getChannelMchNo, channelMchNo);
        alipayDirectAppKeyConfigManager.deleteByField(AlipayDirectAppKeyConfig::getChannelMchNo, channelMchNo);
        alipayDirectAppCapabilityManager.deleteByField(AlipayDirectAppCapability::getChannelMchNo, channelMchNo);
        alipayDirectAppAuthConfigManager.deleteByField(AlipayDirectAppAuthConfig::getChannelMchNo, channelMchNo);
    }

    /// 创建支付宝直连通道商户
    @Transactional(rollbackFor = Exception.class)
    public void create(AlipayDirectChannelMerchantCreateParam param) {
        // 校验同一商户下 PID 不重复
        if (alipayDirectChannelMerchantManager.existsByMchNoAndAlipayUserId(
                param.getMchNo(), param.getAlipayUserId())) {
            // 支付宝: 同一商户下该支付宝商户已存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.directMchDuplicate");
        }
        // 生成通道商户号: 通道前缀 + 雪花ID(无分隔符, 仅供排障辨识)
        String channelMchNo = ChannelMchNoGenerateUtil.generate("ALIPAY");
        // 写通用通道商户主表
        var channelMerchant = new ChannelMerchant();
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
        // 写直连绑定表(alipayUserId 作为业务字段, 不参与关联)
        var entity = new AlipayDirectChannelMerchant();
        entity.setMchNo(param.getMchNo());
        entity.setChannelMchNo(channelMchNo);
        entity.setProduct(param.getProduct());
        entity.setSandbox(sandbox);
        entity.setAlipayUserId(param.getAlipayUserId());
        alipayDirectChannelMerchantManager.save(entity);
    }

    /// 根据通道商户号查询直连通道商户配置
    public AlipayDirectChannelMerchantResult findByChannelMchNo(String channelMchNo) {
        return alipayDirectChannelMerchantManager.lambdaQuery()
                .eq(AlipayDirectChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .map(AlipayDirectChannelMerchant::toResult)
                // 支付宝: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
    }
}
