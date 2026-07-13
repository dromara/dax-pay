package cn.daxpay.open.channel.lakala.service.isv;

import cn.daxpay.open.channel.lakala.dao.isv.LakalaIsvChannelMerchantManager;
import cn.daxpay.open.channel.lakala.entity.isv.LakalaIsvChannelMerchant;
import cn.daxpay.open.channel.lakala.param.isv.LakalaIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.lakala.result.isv.LakalaIsvChannelMerchantResult;
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

/// # 拉卡拉通道商户管理
///
/// 拉卡拉服务商模式下, 子商户关联到服务商本身(密钥全局唯一), 不挂靠具体应用。
/// 一个商户下同一拉卡拉商户号(lakalaMchNo)只允许绑定一次。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaIsvChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final LakalaIsvChannelMerchantManager lakalaIsvChannelMerchantManager;

    /// 创建拉卡拉通道商户
    @Transactional(rollbackFor = Exception.class)
    public void create(LakalaIsvChannelMerchantCreateParam param) {
        // 校验同一商户下拉卡拉商户号不重复
        if (lakalaIsvChannelMerchantManager.existsByMchNoAndLakalaMchNo(
                param.getMchNo(), param.getLakalaMchNo())) {
            // 拉卡拉: 该商户下已存在此拉卡拉商户号
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.lakala.merchantDuplicate");
        }
        // 生成通道商户号: 通道前缀 + 雪花ID(无分隔符, 仅供排障辨识)
        String channelMchNo = ChannelMchNoGenerateUtil.generate("LAKALA");
        // 写通用通道商户主表
        ChannelMerchant channelMerchant = new ChannelMerchant();
        channelMerchant.setMchNo(param.getMchNo());
        channelMerchant.setChannelMchNo(channelMchNo);
        channelMerchant.setChannelMerchantName(param.getChannelMerchantName());
        channelMerchant.setProduct(param.getProduct());
        channelMerchant.setSource(ChannelMerchantSourceEnum.MANUAL.getCode());
        channelMerchant.setEnable(true);
        channelMerchantManager.save(channelMerchant);
        // 写拉卡拉绑定表
        var entity = new LakalaIsvChannelMerchant();
        entity.setMchNo(param.getMchNo());
        entity.setChannelMchNo(channelMchNo);
        entity.setProduct(param.getProduct());
        entity.setLakalaMchNo(param.getLakalaMchNo());
        lakalaIsvChannelMerchantManager.save(entity);
    }

    /// 根据通道商户号查询拉卡拉通道商户配置
    public LakalaIsvChannelMerchantResult findByChannelMchNo(String channelMchNo) {
        return lakalaIsvChannelMerchantManager.lambdaQuery()
                .eq(LakalaIsvChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .map(LakalaIsvChannelMerchant::toResult)
                // 拉卡拉: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
    }

    /// 更新终端号
    @Transactional(rollbackFor = Exception.class)
    public void updateTermNo(String channelMchNo, String termNo) {
        var entity = lakalaIsvChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // 拉卡拉: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        entity.setTermNo(termNo);
        lakalaIsvChannelMerchantManager.updateById(entity);
    }
}
