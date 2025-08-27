package org.dromara.daxpay.channel.union.service.config;

import org.dromara.daxpay.channel.union.result.UnionPayConfigResult;
import org.dromara.daxpay.channel.union.convert.UnionPayConfigConvert;
import org.dromara.daxpay.channel.union.entity.config.UnionPayConfig;
import org.dromara.daxpay.channel.union.param.config.UnionPayConfigParam;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.exception.ConfigNotEnableException;
import org.dromara.daxpay.core.exception.DataErrorException;
import org.dromara.daxpay.core.context.PaymentReqInfoLocal;
import org.dromara.daxpay.service.merchant.cache.ChannelConfigCacheService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.merchant.dao.config.ChannelConfigManager;
import org.dromara.daxpay.service.pay.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.pay.entity.config.ChannelConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 银联支付
 * @author xxm
 * @since 2024/9/6
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPayConfigService {

    private final ChannelConfigManager channelConfigManager;
    private final ChannelConfigCacheService channelConfigCacheService;

    /**
     * 查询
     */
    public UnionPayConfigResult findById(Long id) {
        return channelConfigManager.findById(id)
                .map(UnionPayConfig::convertConfig)
                .map(UnionPayConfig::toResult)
                .orElseThrow(() -> new ConfigNotEnableException("支付宝配置不存在"));
    }

    /**
     * 新增或更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(UnionPayConfigParam param){
        if (param.getId() == null){
            this.save(param);
        } else {
            this.update(param);
        }
    }

    /**
     * 添加
     */
    public void save(UnionPayConfigParam param) {
        UnionPayConfig entity = UnionPayConfigConvert.CONVERT.toEntity(param);
        ChannelConfig channelConfig = entity.toChannelConfig();
        // 判断商户和应用下是否存在该配置
        if (channelConfigManager.existsByAppIdAndChannel(channelConfig.getAppId(), channelConfig.getChannel())){
            throw new DataErrorException("该应用下已存在云闪付配置, 请勿重新添加");
        }
        channelConfigManager.save(channelConfig);
    }

    /**
     * 更新
     */
    public void update(UnionPayConfigParam param){
        ChannelConfig channelConfig = channelConfigManager.findById(param.getId())
                .orElseThrow(() -> new ConfigNotEnableException("支付宝配置不存在"));
        // 通道配置 --转换--> 支付宝配置  ----> 从更新参数赋值  --转换-->  通道配置 ----> 保存更新
        UnionPayConfig unionPayConfig = UnionPayConfig.convertConfig(channelConfig);
        BeanUtil.copyProperties(param, unionPayConfig, CopyOptions.create().ignoreNullValue());
        ChannelConfig channelConfigParam = unionPayConfig.toChannelConfig();
        // 手动清空一下默认的数据版本号
        channelConfigParam.setVersion(null);
        BeanUtil.copyProperties(channelConfigParam, channelConfig, CopyOptions.create().ignoreNullValue());
        channelConfigManager.updateById(channelConfig);
    }

    /**
     * 获取支付宝支付配置
     */
    public UnionPayConfig getUnionPayConfig(){
        PaymentReqInfoLocal reqInfo = PaymentContextLocal.get().getReqInfo();
        ChannelConfig channelConfig = channelConfigCacheService.getMchChannelConfig(reqInfo.getAppId(), ChannelEnum.ALIPAY.getCode());
        return UnionPayConfig.convertConfig(channelConfig);
    }

    /**
     * 获取支步通知地址
     */
    public String getNotifyUrl() {
        var reqInfo = PaymentContextLocal.get().getReqInfo();
        return StrUtil.format("{}/unipay/callback/{}/union",reqInfo.getGatewayServiceUrl(), reqInfo.getAppId());
    }

    /**
     * 获取同步通知地址
     */
    public String getReturnUrl() {
        PaymentReqInfoLocal reqInfo = PaymentContextLocal.get().getReqInfo();
        return StrUtil.format("{}/unipay/return/{}/union",reqInfo.getGatewayServiceUrl(),reqInfo.getAppId());
    }

}
