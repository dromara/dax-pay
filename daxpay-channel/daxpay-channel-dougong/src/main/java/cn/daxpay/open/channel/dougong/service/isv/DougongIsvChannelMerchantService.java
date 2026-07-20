package cn.daxpay.open.channel.dougong.service.isv;

import cn.daxpay.open.channel.dougong.dao.isv.DougongIsvChannelMerchantManager;
import cn.daxpay.open.channel.dougong.entity.isv.DougongIsvChannelMerchant;
import cn.daxpay.open.channel.dougong.param.isv.DougongIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.dougong.result.isv.DougongIsvChannelMerchantResult;
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

/// # 斗拱通道商户管理
///
/// 斗拱服务商模式下, 子商户关联到服务商本身(密钥全局唯一), 不挂靠具体应用。
/// 一个商户下同一汇付商户号(merchantNo)只允许绑定一次。
///
/// 同时作为通道商户扩展数据清理 SPI 实现（[ChannelMerchantCleanupService]）。
///
/// 注意：斗拱与 Adapay 共享同一通道编码 [ChannelEnum#HUIFU]，
/// 由 [ChannelMerchantCleanupSupport] 按 channel 分组遍历调用各自实现，互不影响。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class DougongIsvChannelMerchantService implements ChannelMerchantCleanupService {

    private final ChannelMerchantManager channelMerchantManager;
    private final PayProductConfigManager payProductConfigManager;
    private final DougongIsvChannelMerchantManager dougongIsvChannelMerchantManager;

    /// 通道编码（对应 [ChannelEnum#HUIFU]，与 Adapay 共享）
    @Override
    public String getChannel() {
        return ChannelEnum.HUIFU.getCode();
    }

    /// 清理指定通道商户号下斗拱的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        dougongIsvChannelMerchantManager.deleteByField(DougongIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }

    /// 创建斗拱通道商户
    @Transactional(rollbackFor = Exception.class)
    public void create(DougongIsvChannelMerchantCreateParam param) {
        // 校验同一商户下汇付商户号不重复
        if (dougongIsvChannelMerchantManager.existsByMchNoAndMerchantNo(
                param.getMchNo(), param.getMerchantNo())) {
            // 斗拱: 该商户下已存在此汇付商户号
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.dougong.merchantDuplicate");
        }
        // 生成通道商户号: 通道前缀 + 雪花ID
        String channelMchNo = ChannelMchNoGenerateUtil.generate("DOUGONG");
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
        // 写斗拱绑定表
        var entity = new DougongIsvChannelMerchant();
        entity.setMchNo(param.getMchNo());
        entity.setChannelMchNo(channelMchNo);
        entity.setProduct(param.getProduct());
        entity.setMerchantNo(param.getMerchantNo());
        entity.setAppId(param.getAppId());
        dougongIsvChannelMerchantManager.save(entity);
    }

    /// 根据通道商户号查询斗拱通道商户配置
    public DougongIsvChannelMerchantResult findByChannelMchNo(String channelMchNo) {
        return dougongIsvChannelMerchantManager.lambdaQuery()
                .eq(DougongIsvChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .map(DougongIsvChannelMerchant::toResult)
                // 斗拱: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
    }

    /// 更新商户 appId
    @Transactional(rollbackFor = Exception.class)
    public void updateAppId(String channelMchNo, String appId) {
        var entity = dougongIsvChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // 斗拱: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        entity.setAppId(appId);
        dougongIsvChannelMerchantManager.updateById(entity);
    }
}
