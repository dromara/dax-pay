package cn.bootx.platform.daxpay.service.core.system.config.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.service.core.system.config.entity.WechatNoticeConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 微信消息通知相关配置
 * @author xxm
 * @since 2023/12/24
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class WechatNoticeConfigManager extends BaseManager<WechatNoticeConfigMapper, WechatNoticeConfig> {

}
