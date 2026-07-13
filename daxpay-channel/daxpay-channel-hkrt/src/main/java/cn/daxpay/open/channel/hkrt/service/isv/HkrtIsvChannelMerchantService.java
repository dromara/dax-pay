package cn.daxpay.open.channel.hkrt.service.isv;

import cn.daxpay.open.channel.hkrt.dao.isv.HkrtIsvChannelMerchantManager;
import cn.daxpay.open.channel.hkrt.entity.isv.HkrtIsvChannelMerchant;
import cn.daxpay.open.channel.hkrt.param.isv.HkrtIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.hkrt.result.isv.HkrtIsvChannelMerchantResult;
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

/// # 海科融通通道商户管理
///
/// 海科融通服务商模式下, 子商户关联到服务商本身(密钥全局唯一), 不挂靠具体应用。
/// 一个商户下同一海科商户号(merchNo)只允许绑定一次。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class HkrtIsvChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final HkrtIsvChannelMerchantManager hkrtIsvChannelMerchantManager;

    /// 创建海科融通通道商户
    @Transactional(rollbackFor = Exception.class)
    public void create(HkrtIsvChannelMerchantCreateParam param) {
        // 校验同一商户下海科商户号不重复
        if (hkrtIsvChannelMerchantManager.existsByMchNoAndMerchNo(
                param.getMchNo(), param.getMerchNo())) {
            // 海科融通: 该商户下已存在此海科商户号
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "channel.error.hkrtMerchantDuplicate");
        }
        // 生成通道商户号: 通道前缀 + 雪花ID(无分隔符, 仅供排障辨识)
        String channelMchNo = ChannelMchNoGenerateUtil.generate("HKRT");
        // 写通用通道商户主表
        ChannelMerchant channelMerchant = new ChannelMerchant();
        channelMerchant.setMchNo(param.getMchNo());
        channelMerchant.setChannelMchNo(channelMchNo);
        channelMerchant.setChannelMerchantName(param.getChannelMerchantName());
        channelMerchant.setProduct(param.getProduct());
        channelMerchant.setSource(ChannelMerchantSourceEnum.MANUAL.getCode());
        channelMerchant.setEnable(true);
        channelMerchantManager.save(channelMerchant);
        // 写海科融通绑定表
        var entity = new HkrtIsvChannelMerchant();
        entity.setMchNo(param.getMchNo());
        entity.setChannelMchNo(channelMchNo);
        entity.setProduct(param.getProduct());
        entity.setMerchNo(param.getMerchNo());
        hkrtIsvChannelMerchantManager.save(entity);
    }

    /// 根据通道商户号查询海科融通通道商户配置
    public HkrtIsvChannelMerchantResult findByChannelMchNo(String channelMchNo) {
        return hkrtIsvChannelMerchantManager.lambdaQuery()
                .eq(HkrtIsvChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .map(HkrtIsvChannelMerchant::toResult)
                // 海科融通: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
    }

    /// 更新 SAAS 终端号(pn)
    @Transactional(rollbackFor = Exception.class)
    public void updatePn(String channelMchNo, String pn) {
        var entity = hkrtIsvChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // 海科融通: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        entity.setPn(pn);
        hkrtIsvChannelMerchantManager.updateById(entity);
    }
}
