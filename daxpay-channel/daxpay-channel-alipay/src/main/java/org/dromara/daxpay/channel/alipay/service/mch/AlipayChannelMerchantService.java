package org.dromara.daxpay.channel.alipay.service.mch;

import org.dromara.daxpay.channel.alipay.dao.app.AlipayIsvAppManager;
import org.dromara.daxpay.channel.alipay.dao.config.AlipayChannelMerchantManager;
import org.dromara.daxpay.channel.alipay.entity.config.AlipayChannelMerchant;
import org.dromara.daxpay.channel.alipay.param.mch.AlipayDirectChannelMerchantCreateParam;
import org.dromara.daxpay.channel.alipay.param.mch.AlipayIsvChannelMerchantCreateParam;
import org.dromara.daxpay.channel.alipay.result.config.AlipayChannelMerchantResult;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final AlipayChannelMerchantManager alipayChannelMerchantManager;
    private final AlipayIsvAppManager alipayIsvAppManager;

    /// 创建支付宝服务商通道商户
    @Transactional(rollbackFor = Exception.class)
    public void create(AlipayIsvChannelMerchantCreateParam param) {
        var isvApp = alipayIsvAppManager.findByAliAppId(param.getIsvAppId())
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
        // 校验同一应用下子商户号不重复
        if (alipayChannelMerchantManager.existsByIsvAppIdAndAlipayUserId(
                isvApp.getAliAppId(), param.getAlipayUserId())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.subMerchantDuplicate");
        }
        // 生成通道商户号：前缀 AISV + 雪花ID（无分隔符，符合 TradeNoGenerateUtil 约定）
        String channelMchNo = "AISV" + IdUtil.getSnowflakeNextId();
        // 写通用通道商户主表
        ChannelMerchant channelMerchant = new ChannelMerchant();
        channelMerchant.setId(IdUtil.getSnowflakeNextId());
        channelMerchant.setMchNo(param.getMchNo());
        channelMerchant.setChannelMchNo(channelMchNo);
        channelMerchant.setChannelMerchantName(param.getChannelMerchantName());
        channelMerchant.setProduct(param.getProduct());
        channelMerchant.setSource(ChannelMerchantSourceEnum.MANUAL.getCode());
        channelMerchant.setEnable(true);
        channelMerchantManager.save(channelMerchant);
        // 写支付宝扩展表（含服务商专属字段）
        AlipayChannelMerchant alipayChannelMerchant = new AlipayChannelMerchant();
        alipayChannelMerchant.setId(IdUtil.getSnowflakeNextId());
        alipayChannelMerchant.setMchNo(param.getMchNo());
        alipayChannelMerchant.setChannelMchNo(channelMchNo);
        alipayChannelMerchant.setProduct(param.getProduct());
        alipayChannelMerchant.setIsvAppId(isvApp.getAliAppId());
        alipayChannelMerchant.setAlipayUserId(param.getAlipayUserId());
        alipayChannelMerchant.setAppAuthToken(param.getAppAuthToken());
        alipayChannelMerchantManager.save(alipayChannelMerchant);
    }

    /// 创建支付宝直连通道商户
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

