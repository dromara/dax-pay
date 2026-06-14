package org.dromara.daxpay.channel.alipay.service.mch;

import org.dromara.daxpay.channel.alipay.dao.config.AlipayChannelMerchantManager;
import org.dromara.daxpay.channel.alipay.entity.config.AlipayChannelMerchant;
import org.dromara.daxpay.channel.alipay.param.mch.AlipayDirectChannelMerchantCreateParam;
import org.dromara.daxpay.channel.alipay.result.config.AlipayChannelMerchantResult;
import org.dromara.daxpay.payment.channel.dao.mch.ChannelMerchantManager;
import org.dromara.daxpay.payment.channel.entity.mch.ChannelMerchant;
import org.dromara.daxpay.platform.core.enums.channel.ChannelMerchantSourceEnum;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final AlipayChannelMerchantManager alipayChannelMerchantManager;

    @Transactional(rollbackFor = Exception.class)
    public Long createDirect(AlipayDirectChannelMerchantCreateParam param) {
        String channelMchNo = param.getAlipayUserId();

        ChannelMerchant channelMerchant = new ChannelMerchant();
        long channelMerchantId = IdUtil.getSnowflakeNextId();
        channelMerchant.setId(channelMerchantId);
        channelMerchant.setMchNo(param.getMchNo());
        channelMerchant.setChannelMchNo(channelMchNo);
        channelMerchant.setChannelMerchantName(param.getChannelMerchantName());
        channelMerchant.setProduct(param.getProduct());
        channelMerchant.setSource(ChannelMerchantSourceEnum.MANUAL.getCode());
        channelMerchant.setEnable(true);
        channelMerchantManager.save(channelMerchant);

        AlipayChannelMerchant alipayChannelMerchant = new AlipayChannelMerchant();
        alipayChannelMerchant.setId(IdUtil.getSnowflakeNextId());
        alipayChannelMerchant.setMchNo(param.getMchNo());
        alipayChannelMerchant.setChannelMchNo(channelMchNo);
        alipayChannelMerchant.setProduct(param.getProduct());
        alipayChannelMerchant.setAlipayUserId(param.getAlipayUserId());
        alipayChannelMerchantManager.save(alipayChannelMerchant);
        return channelMerchantId;
    }

    public AlipayChannelMerchantResult findByChannelMchNo(String channelMchNo) {
        return alipayChannelMerchantManager.lambdaQuery()
                .eq(AlipayChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .map(AlipayChannelMerchant::toResult)
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
    }
}
