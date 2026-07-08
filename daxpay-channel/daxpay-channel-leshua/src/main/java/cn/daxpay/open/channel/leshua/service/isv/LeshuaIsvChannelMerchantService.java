package cn.daxpay.open.channel.leshua.service.isv;

import cn.daxpay.open.channel.leshua.dao.isv.LeshuaIsvChannelMerchantManager;
import cn.daxpay.open.channel.leshua.entity.isv.LeshuaIsvChannelMerchant;
import cn.daxpay.open.channel.leshua.param.isv.LeshuaIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.leshua.result.isv.LeshuaIsvChannelMerchantResult;
import cn.daxpay.open.payment.channel.dao.mch.ChannelMerchantManager;
import cn.daxpay.open.payment.channel.entity.mch.ChannelMerchant;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.util.ChannelMchNoGenerateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 乐刷通道商户管理
///
/// 乐刷服务商模式下, 子商户关联到服务商本身(密钥全局唯一), 不挂靠具体应用。
/// 一个商户下同一乐刷商户号(lsMchNo)只允许绑定一次。
@Slf4j
@Service
@RequiredArgsConstructor
public class LeshuaIsvChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final LeshuaIsvChannelMerchantManager leshuaIsvChannelMerchantManager;

    /// 创建乐刷通道商户
    @Transactional(rollbackFor = Exception.class)
    public void create(LeshuaIsvChannelMerchantCreateParam param) {
        // 校验同一商户下乐刷商户号不重复
        if (leshuaIsvChannelMerchantManager.existsByMchNoAndLsMchNo(
                param.getMchNo(), param.getLsMchNo())) {
            // 乐刷: 该商户下已存在此乐刷商户号
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.leshua.merchantDuplicate");
        }
        // 生成通道商户号
        String channelMchNo = ChannelMchNoGenerateUtil.generate("LESHUA");
        // 写通用通道商户主表
        ChannelMerchant channelMerchant = new ChannelMerchant();
        channelMerchant.setMchNo(param.getMchNo());
        channelMerchant.setChannelMchNo(channelMchNo);
        channelMerchant.setChannelMerchantName(param.getChannelMerchantName());
        channelMerchant.setProduct(param.getProduct());
        channelMerchant.setSource(ChannelMerchantSourceEnum.MANUAL.getCode());
        channelMerchant.setEnable(true);
        channelMerchantManager.save(channelMerchant);
        // 写乐刷绑定表
        var entity = new LeshuaIsvChannelMerchant();
        entity.setMchNo(param.getMchNo());
        entity.setChannelMchNo(channelMchNo);
        entity.setProduct(param.getProduct());
        entity.setLsMchNo(param.getLsMchNo());
        leshuaIsvChannelMerchantManager.save(entity);
    }

    /// 根据通道商户号查询乐刷通道商户配置
    public LeshuaIsvChannelMerchantResult findByChannelMchNo(String channelMchNo) {
        return leshuaIsvChannelMerchantManager.lambdaQuery()
                .eq(LeshuaIsvChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .map(LeshuaIsvChannelMerchant::toResult)
                // 乐刷: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
    }
}
