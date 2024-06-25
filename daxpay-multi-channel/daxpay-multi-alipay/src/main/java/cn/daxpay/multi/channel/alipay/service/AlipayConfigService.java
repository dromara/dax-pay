package cn.daxpay.multi.channel.alipay.service;

import cn.daxpay.multi.channel.alipay.entity.AlipayConfig;
import cn.daxpay.multi.core.exception.ConfigNotEnabledException;
import cn.daxpay.multi.service.dao.channel.ChannelConfigManager;
import cn.daxpay.multi.service.entity.channel.ChannelConfig;
import cn.daxpay.multi.service.entity.constant.ChannelConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private AlipayConfig findById(Long id) {
        ChannelConfig config = channelConfigManager.findById(id)
                .orElseThrow(() -> new ConfigNotEnabledException("支付包配置不存在"));


    }

    /**
     * 添加
     */

    /**
     * 更新
     */

}
