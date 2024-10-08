package cn.daxpay.multi.channel.union.service.config;

import cn.daxpay.multi.channel.union.UnionPayConfigResult;
import cn.daxpay.multi.channel.union.convert.UnionPayConfigConvert;
import cn.daxpay.multi.channel.union.entity.config.UnionPayConfig;
import cn.daxpay.multi.channel.union.param.config.UnionPayConfigParam;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.core.exception.ConfigNotEnableException;
import cn.daxpay.multi.core.exception.DataErrorException;
import cn.daxpay.multi.service.common.cache.ChannelConfigCacheService;
import cn.daxpay.multi.service.common.context.MchAppLocal;
import cn.daxpay.multi.service.common.local.MchContextLocal;
import cn.daxpay.multi.service.common.local.PaymentContextLocal;
import cn.daxpay.multi.service.dao.config.ChannelConfigManager;
import cn.daxpay.multi.service.entity.config.ChannelConfig;
import cn.daxpay.multi.service.service.config.PlatformConfigService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final PlatformConfigService platformConfigService;

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
        // 如果运营端使用, 商户号写入上下文中
        MchContextLocal.setMchNo(channelConfig.getMchNo());
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
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        ChannelConfig channelConfig = channelConfigCacheService.get(mchAppInfo.getAppId(), ChannelEnum.ALI.getCode());
        return UnionPayConfig.convertConfig(channelConfig);
    }

    /**
     * 获取支步通知地址
     */
    public String getNotifyUrl() {
        var mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        var platformInfo = platformConfigService.getConfig();
        return StrUtil.format("{}/unipay/callback/{}/{}/union",platformInfo.getGatewayServiceUrl(), mchAppInfo.getMchNo(),mchAppInfo.getAppId());
    }

    /**
     * 获取同步通知地址
     */
    public String getReturnUrl() {
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        var platformInfo = platformConfigService.getConfig();
        return StrUtil.format("{}/unipay/return/{}/{}/union",platformInfo.getGatewayServiceUrl(), mchAppInfo.getMchNo(),mchAppInfo.getAppId());
    }

}
