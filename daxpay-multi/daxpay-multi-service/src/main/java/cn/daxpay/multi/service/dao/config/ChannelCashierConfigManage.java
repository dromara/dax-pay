package cn.daxpay.multi.service.dao.config;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.multi.service.entity.config.ChannelCashierConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 通道收银台配置
 * @author xxm
 * @since 2024/9/28
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ChannelCashierConfigManage extends BaseManager<ChannelCashierConfigMapper, ChannelCashierConfig> {
}
