package cn.daxpay.open.channel.fuyou.service.isv;

import cn.daxpay.open.channel.fuyou.dao.isv.FuyouIsvChannelMerchantManager;
import cn.daxpay.open.channel.fuyou.entity.isv.FuyouIsvChannelMerchant;
import cn.daxpay.open.channel.fuyou.param.isv.FuyouIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.fuyou.result.isv.FuyouIsvChannelMerchantResult;
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

/// # 富友通道商户管理
///
/// 富友服务商模式下, 子商户关联到服务商(密钥全局唯一)。
/// 一个商户号下同一富友商户号(fuyouMchNo)只允许绑定一次。
///
/// 同时作为通道商户扩展数据清理 SPI 实现（[ChannelMerchantCleanupService]）。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouIsvChannelMerchantService implements ChannelMerchantCleanupService {

    private final ChannelMerchantManager channelMerchantManager;
    private final PayProductConfigManager payProductConfigManager;
    private final FuyouIsvChannelMerchantManager fuyouIsvChannelMerchantManager;

    /// 通道编码（对应 [ChannelEnum#FUYOU_PAY]）
    @Override
    public String getChannel() {
        return ChannelEnum.FUYOU_PAY.getCode();
    }

    /// 清理指定通道商户号下富友的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        fuyouIsvChannelMerchantManager.deleteByField(FuyouIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }

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
        // 沙箱标记从支付产品生效环境同步写入, 禁止商户/表单设置
        boolean sandbox = payProductConfigManager.isSandboxActive(param.getProduct());
        channelMerchant.setSandbox(sandbox);
        channelMerchantManager.save(channelMerchant);
        // 写富友绑定表
        var entity = new FuyouIsvChannelMerchant();
        entity.setMchNo(param.getMchNo());
        entity.setChannelMchNo(channelMchNo);
        entity.setProduct(param.getProduct());
        entity.setSandbox(sandbox);
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
