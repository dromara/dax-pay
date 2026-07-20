package cn.daxpay.open.channel.hmpay.service.isv;

import cn.daxpay.open.channel.hmpay.dao.isv.HmpayIsvChannelMerchantManager;
import cn.daxpay.open.channel.hmpay.entity.isv.HmpayIsvChannelMerchant;
import cn.daxpay.open.channel.hmpay.param.isv.HmpayIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.hmpay.param.isv.HmpayIsvChannelMerchantUpdateParam;
import cn.daxpay.open.channel.hmpay.result.isv.HmpayIsvChannelMerchantResult;
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

/// # 河马付通道商户管理
///
/// 河马付服务商模式下, 子商户关联到服务商本身(密钥全局唯一), 不挂靠具体应用。
/// 一个商户下同一杉德商户号只允许绑定一次。
///
/// 同时作为通道商户扩展数据清理 SPI 实现（[ChannelMerchantCleanupService]）。
///
/// 注意：河马付底层走杉德通道，[getChannel] 返回 [ChannelEnum#SAND_PAY]。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class HmpayIsvChannelMerchantService implements ChannelMerchantCleanupService {

    private final ChannelMerchantManager channelMerchantManager;
    private final PayProductConfigManager payProductConfigManager;
    private final HmpayIsvChannelMerchantManager hmpayIsvChannelMerchantManager;

    /// 通道编码（对应 [ChannelEnum#SAND_PAY]，河马付底层走杉德通道）
    @Override
    public String getChannel() {
        return ChannelEnum.SAND_PAY.getCode();
    }

    /// 清理指定通道商户号下河马付的所有扩展数据
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        hmpayIsvChannelMerchantManager.deleteByField(HmpayIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }

    /// 创建河马付通道商户
    @Transactional(rollbackFor = Exception.class)
    public void create(HmpayIsvChannelMerchantCreateParam param) {
        // 校验同一商户下杉德商户号不重复
        if (hmpayIsvChannelMerchantManager.existsByMchNoAndMerchantNo(
                param.getMchNo(), param.getMerchantNo())) {
            // 河马付: 该商户下已存在此杉德商户号
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.hmpay.merchantDuplicate");
        }
        // 生成通道商户号: 通道前缀 + 雪花ID
        String channelMchNo = ChannelMchNoGenerateUtil.generate("HMPAY");
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
        // 写河马付绑定表(门店号为可选配置, 创建后另行编辑)
        var entity = new HmpayIsvChannelMerchant();
        entity.setMchNo(param.getMchNo());
        entity.setChannelMchNo(channelMchNo);
        entity.setProduct(param.getProduct());
        entity.setSandbox(sandbox);
        entity.setMerchantNo(param.getMerchantNo());
        hmpayIsvChannelMerchantManager.save(entity);
    }

    /// 更新河马付通道商户可选配置(门店号)
    ///
    /// 杉德商户号为核心识别字段, 创建后不可修改。
    @Transactional(rollbackFor = Exception.class)
    public void updateConfig(HmpayIsvChannelMerchantUpdateParam param) {
        var entity = hmpayIsvChannelMerchantManager.findByChannelMchNo(param.getChannelMchNo())
                // 河马付: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        entity.setStoreId(param.getStoreId());
        hmpayIsvChannelMerchantManager.updateById(entity);
    }

    /// 根据通道商户号查询河马付通道商户配置
    public HmpayIsvChannelMerchantResult findByChannelMchNo(String channelMchNo) {
        return hmpayIsvChannelMerchantManager.lambdaQuery()
                .eq(HmpayIsvChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .map(HmpayIsvChannelMerchant::toResult)
                // 河马付: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
    }
}
