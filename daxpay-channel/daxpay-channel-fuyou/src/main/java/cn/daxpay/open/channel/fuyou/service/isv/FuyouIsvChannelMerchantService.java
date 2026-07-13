package cn.daxpay.open.channel.fuyou.service.isv;

import cn.daxpay.open.channel.fuyou.dao.isv.FuyouIsvChannelMerchantManager;
import cn.daxpay.open.channel.fuyou.entity.isv.FuyouIsvChannelMerchant;
import cn.daxpay.open.channel.fuyou.param.isv.FuyouIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.fuyou.result.isv.FuyouIsvChannelMerchantResult;
import cn.daxpay.open.payment.channel.dao.ChannelMerchantManager;
import cn.daxpay.open.payment.channel.entity.ChannelMerchant;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.util.ChannelMchNoGenerateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 富友通道商户管理
///
/// 富友服务商模式下, 子商户关联到服务商(密钥全局唯一)。
/// 一个商户号下同一富友商户号(fuyouMchNo)只允许绑定一次。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouIsvChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final FuyouIsvChannelMerchantManager fuyouIsvChannelMerchantManager;

    /// 创建富友通道商户
    @Transactional(rollbackFor = Exception.class)
    public void create(FuyouIsvChannelMerchantCreateParam param) {
        // 校验同一商户号下富友商户号不重复
        if (fuyouIsvChannelMerchantManager.existsByMchNoAndFuyouMchNo(
                param.getMchNo(), param.getFuyouMchNo())) {
            // 富友: 该商户下已存在此富友商户号
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.fuyou.merchantDuplicate");
        }
        // 生成通道商户号: 通道前缀 + 雪花ID
        String channelMchNo = ChannelMchNoGenerateUtil.generate("FUYOU");
        // 写通用通道商户主表
        ChannelMerchant channelMerchant = new ChannelMerchant();
        channelMerchant.setMchNo(param.getMchNo());
        channelMerchant.setChannelMchNo(channelMchNo);
        channelMerchant.setChannelMerchantName(param.getChannelMerchantName());
        channelMerchant.setProduct(param.getProduct());
        channelMerchant.setSource(ChannelMerchantSourceEnum.MANUAL.getCode());
        channelMerchant.setEnable(true);
        channelMerchantManager.save(channelMerchant);
        // 写富友绑定表
        var entity = new FuyouIsvChannelMerchant();
        entity.setMchNo(param.getMchNo());
        entity.setChannelMchNo(channelMchNo);
        entity.setProduct(param.getProduct());
        entity.setFuyouMchNo(param.getFuyouMchNo());
        entity.setTermNo(param.getTermNo());
        fuyouIsvChannelMerchantManager.save(entity);
    }

    /// 根据通道商户号查询富友通道商户配置
    public FuyouIsvChannelMerchantResult findByChannelMchNo(String channelMchNo) {
        return fuyouIsvChannelMerchantManager.lambdaQuery()
                .eq(FuyouIsvChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .map(FuyouIsvChannelMerchant::toResult)
                // 富友: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
    }
}
