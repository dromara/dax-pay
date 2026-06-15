package org.dromara.daxpay.channel.alipay.service.isv;

import org.dromara.daxpay.channel.alipay.dao.isv.AlipayIsvAppManager;
import org.dromara.daxpay.channel.alipay.dao.isv.AlipayIsvChannelMerchantManager;
import org.dromara.daxpay.channel.alipay.entity.isv.AlipayIsvApp;
import org.dromara.daxpay.channel.alipay.entity.isv.AlipayIsvChannelMerchant;
import org.dromara.daxpay.channel.alipay.param.isv.AlipayIsvChannelMerchantCreateParam;
import org.dromara.daxpay.channel.alipay.result.isv.AlipayIsvChannelMerchantResult;
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

/// # 支付宝服务商通道商户管理
///
/// 一条记录代表"子商户挂靠在某个服务商应用下"的授权关系。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayIsvChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final AlipayIsvChannelMerchantManager alipayIsvChannelMerchantManager;
    private final AlipayIsvAppManager alipayIsvAppManager;

    /// 创建支付宝服务商通道商户
    @Transactional(rollbackFor = Exception.class)
    public void create(AlipayIsvChannelMerchantCreateParam param) {
        // 校验服务商应用存在(用 isvAppId 系统主键查询)
        var isvApp = alipayIsvAppManager.findById(param.getIsvAppId())
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
        // 校验同一应用下子商户号不重复
        if (alipayIsvChannelMerchantManager.existsByIsvAppIdAndAlipayUserId(
                param.getIsvAppId(), param.getAlipayUserId())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.subMerchantDuplicate");
        }
        // 生成通道商户号：前缀 AISV + 雪花ID(无分隔符, 符合 TradeNoGenerateUtil 约定)
        String channelMchNo = "AISV" + IdUtil.getSnowflakeNextId();
        // 写通用通道商户主表
        var channelMerchant = new ChannelMerchant();
        channelMerchant.setMchNo(param.getMchNo());
        channelMerchant.setChannelMchNo(channelMchNo);
        channelMerchant.setChannelMerchantName(param.getChannelMerchantName());
        channelMerchant.setProduct(param.getProduct());
        channelMerchant.setSource(ChannelMerchantSourceEnum.MANUAL.getCode());
        channelMerchant.setEnable(true);
        channelMerchantManager.save(channelMerchant);
        // 写服务商绑定表(含挂靠关系专属字段)
        var entity = new AlipayIsvChannelMerchant();
        entity.setMchNo(param.getMchNo());
        entity.setChannelMchNo(channelMchNo);
        entity.setProduct(param.getProduct());
        entity.setIsvAppId(isvApp.getId());
        entity.setAlipayUserId(param.getAlipayUserId());
        entity.setAppAuthToken(param.getAppAuthToken());
        alipayIsvChannelMerchantManager.save(entity);
    }

    /// 根据通道商户号查询服务商通道商户配置
    public AlipayIsvChannelMerchantResult findByChannelMchNo(String channelMchNo) {
        return alipayIsvChannelMerchantManager.lambdaQuery()
                .eq(AlipayIsvChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .map(AlipayIsvChannelMerchant::toResult)
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
    }
}
