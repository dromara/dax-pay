package org.dromara.daxpay.channel.douyin.service.direct;

import org.dromara.daxpay.channel.douyin.dao.direct.DouyinDirectChannelMerchantManager;
import org.dromara.daxpay.channel.douyin.entity.direct.DouyinDirectChannelMerchant;
import org.dromara.daxpay.channel.douyin.param.direct.DouyinDirectChannelMerchantCreateParam;
import org.dromara.daxpay.channel.douyin.result.direct.DouyinDirectChannelMerchantResult;
import org.dromara.daxpay.payment.channel.dao.mch.ChannelMerchantManager;
import org.dromara.daxpay.payment.channel.entity.mch.ChannelMerchant;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.enums.channel.ChannelMerchantSourceEnum;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 抖音直连通道商户管理
///
/// 一个抖音商户号(dyMchId)对应一个 channelMchNo, 商户的多个应用共享此绑定。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinDirectChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final DouyinDirectChannelMerchantManager douyinDirectChannelMerchantManager;

    /// 创建抖音直连通道商户
    @Transactional(rollbackFor = Exception.class)
    public void create(DouyinDirectChannelMerchantCreateParam param) {
        // 校验同一商户下抖音商户号不重复
        if (douyinDirectChannelMerchantManager.existsByMchNoAndDyMchId(
                param.getMchNo(), param.getDyMchId())) {
            // 抖音: 同一商户下该抖音商户已存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.douyin.directMchDuplicate");
        }
        // 生成通道商户号(雪花号)
        String channelMchNo = String.valueOf(IdUtil.getSnowflakeNextId());
        // 写通用通道商户主表
        ChannelMerchant channelMerchant = new ChannelMerchant();
        channelMerchant.setMchNo(param.getMchNo());
        channelMerchant.setChannelMchNo(channelMchNo);
        channelMerchant.setChannelMerchantName(param.getChannelMerchantName());
        channelMerchant.setProduct(param.getProduct());
        channelMerchant.setSource(ChannelMerchantSourceEnum.MANUAL.getCode());
        channelMerchant.setEnable(true);
        channelMerchantManager.save(channelMerchant);
        // 写直连绑定表(dyMchId 作为业务字段)
        DouyinDirectChannelMerchant entity = new DouyinDirectChannelMerchant();
        entity.setMchNo(param.getMchNo());
        entity.setChannelMchNo(channelMchNo);
        entity.setProduct(param.getProduct());
        entity.setDyMchId(param.getDyMchId());
        douyinDirectChannelMerchantManager.save(entity);
    }

    /// 根据通道商户号查询直连通道商户配置
    public DouyinDirectChannelMerchantResult findByChannelMchNo(String channelMchNo) {
        return douyinDirectChannelMerchantManager.lambdaQuery()
                .eq(DouyinDirectChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .map(DouyinDirectChannelMerchant::toResult)
                // 通道: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
    }
}
