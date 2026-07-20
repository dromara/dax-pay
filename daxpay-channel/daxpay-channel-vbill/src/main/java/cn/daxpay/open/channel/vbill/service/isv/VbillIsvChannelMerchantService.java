package cn.daxpay.open.channel.vbill.service.isv;

import cn.daxpay.open.channel.vbill.dao.isv.VbillIsvChannelMerchantManager;
import cn.daxpay.open.channel.vbill.entity.isv.VbillIsvChannelMerchant;
import cn.daxpay.open.channel.vbill.param.isv.VbillIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.vbill.result.isv.VbillIsvChannelMerchantResult;
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

/// # 随行付通道商户管理
///
/// 随行付(天阙科技)服务商模式下, 子商户关联到服务商(密钥全局唯一)。
/// 一个商户号下同一天阙商户号(vbillMchNo/mno)只允许绑定一次。
///
/// 同时作为通道商户扩展数据清理 SPI 实现（[ChannelMerchantCleanupService]）。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class VbillIsvChannelMerchantService implements ChannelMerchantCleanupService {

    private final ChannelMerchantManager channelMerchantManager;
    private final PayProductConfigManager payProductConfigManager;
    private final VbillIsvChannelMerchantManager vbillIsvChannelMerchantManager;

    /// 通道编码（对应 [ChannelEnum#VBILL_PAY]）
    @Override
    public String getChannel() {
        return ChannelEnum.VBILL_PAY.getCode();
    }

    /// 清理指定通道商户号下随行付的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        vbillIsvChannelMerchantManager.deleteByField(VbillIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }

    /// 创建随行付通道商户
    @Transactional(rollbackFor = Exception.class)
    public void create(VbillIsvChannelMerchantCreateParam param) {
        // 校验同一商户号下天阙商户号不重复
        if (vbillIsvChannelMerchantManager.existsByMchNoAndVbillMchNo(
                param.getMchNo(), param.getVbillMchNo())) {
            // 随行付: 该商户下已存在此天阙商户号
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.vbill.merchantDuplicate");
        }
        // 生成通道商户号: 通道前缀 + 雪花ID
        String channelMchNo = ChannelMchNoGenerateUtil.generate("VBILL");
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
        // 写随行付绑定表
        var entity = new VbillIsvChannelMerchant();
        entity.setMchNo(param.getMchNo());
        entity.setChannelMchNo(channelMchNo);
        entity.setProduct(param.getProduct());
        entity.setSandbox(sandbox);
        entity.setVbillMchNo(param.getVbillMchNo());
        vbillIsvChannelMerchantManager.save(entity);
    }

    /// 根据通道商户号查询随行付通道商户配置
    public VbillIsvChannelMerchantResult findByChannelMchNo(String channelMchNo) {
        return vbillIsvChannelMerchantManager.lambdaQuery()
                .eq(VbillIsvChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .map(VbillIsvChannelMerchant::toResult)
                // 随行付: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
    }
}
