package cn.daxpay.open.channel.vbill.service.isv;

import cn.daxpay.open.channel.vbill.dao.isv.VbillIsvChannelMerchantManager;
import cn.daxpay.open.channel.vbill.entity.isv.VbillIsvChannelMerchant;
import cn.daxpay.open.channel.vbill.param.isv.VbillIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.vbill.result.isv.VbillIsvChannelMerchantResult;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
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

/// # 随行付通道商户管理
///
/// 随行付(天阙科技)服务商模式下, 子商户关联到服务商(密钥全局唯一)。
/// 一个商户号下同一天阙商户号(vbillMchNo/mno)只允许绑定一次。
@Slf4j
@Service
@RequiredArgsConstructor
public class VbillIsvChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final VbillIsvChannelMerchantManager vbillIsvChannelMerchantManager;

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
        channelMerchantManager.save(channelMerchant);
        // 写随行付绑定表
        var entity = new VbillIsvChannelMerchant();
        entity.setMchNo(param.getMchNo());
        entity.setChannelMchNo(channelMchNo);
        entity.setProduct(param.getProduct());
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
