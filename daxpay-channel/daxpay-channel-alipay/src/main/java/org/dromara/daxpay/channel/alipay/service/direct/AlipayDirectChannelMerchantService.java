package org.dromara.daxpay.channel.alipay.service.direct;

import org.dromara.daxpay.channel.alipay.dao.direct.AlipayDirectChannelMerchantManager;
import org.dromara.daxpay.channel.alipay.entity.direct.AlipayDirectChannelMerchant;
import org.dromara.daxpay.channel.alipay.param.direct.AlipayDirectChannelMerchantCreateParam;
import org.dromara.daxpay.channel.alipay.result.direct.AlipayDirectChannelMerchantResult;
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

/// # 支付宝直连通道商户管理
///
/// 一个商户PID对应一个channelMchNo, 商户的多个应用共享此绑定。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayDirectChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final AlipayDirectChannelMerchantManager alipayDirectChannelMerchantManager;

    /// 创建支付宝直连通道商户
    @Transactional(rollbackFor = Exception.class)
    public void create(AlipayDirectChannelMerchantCreateParam param) {
        // 校验同一商户下 PID 不重复
        if (alipayDirectChannelMerchantManager.existsByMchNoAndAlipayUserId(
                param.getMchNo(), param.getAlipayUserId())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.directMchDuplicate");
        }
        // 生成通道商户号(雪花号, 不再使用 alipayUserId)
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
        // 写直连绑定表(alipayUserId 作为业务字段, 不参与关联)
        AlipayDirectChannelMerchant entity = new AlipayDirectChannelMerchant();
        entity.setMchNo(param.getMchNo());
        entity.setChannelMchNo(channelMchNo);
        entity.setProduct(param.getProduct());
        entity.setAlipayUserId(param.getAlipayUserId());
        alipayDirectChannelMerchantManager.save(entity);
    }

    /// 根据通道商户号查询直连通道商户配置
    public AlipayDirectChannelMerchantResult findByChannelMchNo(String channelMchNo) {
        return alipayDirectChannelMerchantManager.lambdaQuery()
                .eq(AlipayDirectChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .map(AlipayDirectChannelMerchant::toResult)
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
    }
}
