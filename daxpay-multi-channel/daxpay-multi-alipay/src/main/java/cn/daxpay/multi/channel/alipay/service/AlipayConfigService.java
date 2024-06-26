package cn.daxpay.multi.channel.alipay.service;

import cn.daxpay.multi.channel.alipay.convert.AlipayConfigConvert;
import cn.daxpay.multi.channel.alipay.entity.AlipayConfig;
import cn.daxpay.multi.channel.alipay.param.AlipayConfigParam;
import cn.daxpay.multi.channel.alipay.result.AlipayConfigResult;
import cn.daxpay.multi.core.exception.ConfigNotEnableException;
import cn.daxpay.multi.core.exception.DataErrorException;
import cn.daxpay.multi.service.dao.channel.ChannelConfigManager;
import cn.daxpay.multi.service.entity.channel.ChannelConfig;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 支付宝配置
 * @author xxm
 * @since 2024/6/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayConfigService {
    private final ChannelConfigManager channelConfigManager;

    /**
     * 查询
     */
    public AlipayConfigResult findById(Long id) {
        return channelConfigManager.findById(id)
                .map(AlipayConfig::convertConfig)
                .map(AlipayConfig::toResult)
                .orElseThrow(() -> new ConfigNotEnableException("支付包配置不存在"));

    }

    /**
     * 新增或更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(AlipayConfigParam param){
        if (param.getId() == null){
            this.save(param);
        } else {
            this.update(param);
        }
    }

    /**
     * 添加
     */
    public void save(AlipayConfigParam param) {
        AlipayConfig entity = AlipayConfigConvert.CONVERT.toEntity(param);
        ChannelConfig channelConfig = entity.toChannelConfig();
        // TODO 如果运营端, 商户号写入上下文中

        // 判断商户和应用下是否存在该配置
        if (channelConfigManager.existsByAppIdAndChannel(channelConfig.getAppId(), channelConfig.getChannel())){
            throw new DataErrorException("该应用下已存在支付宝配置, 请勿重新添加");
        }
        channelConfigManager.save(channelConfig);
    }

    /**
     * 更新
     */
    public void update(AlipayConfigParam param){
        ChannelConfig channelConfig = channelConfigManager.findById(param.getId())
                .orElseThrow(() -> new ConfigNotEnableException("支付宝配置不存在"));
        // 通道配置 --转换--> 支付宝配置  ----> 从更新参数赋值  --转换-->  通道配置 ----> 保存更新
        AlipayConfig alipayConfig = AlipayConfig.convertConfig(channelConfig);
        BeanUtil.copyProperties(param, alipayConfig);
        ChannelConfig channelConfigParam = alipayConfig.toChannelConfig();
        BeanUtil.copyProperties(channelConfigParam, channelConfig, CopyOptions.create().ignoreNullValue());
        channelConfigManager.updateById(channelConfig);
    }

}
