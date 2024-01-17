package cn.bootx.platform.daxpay.service.core.channel.wechat.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 微信支付配置
 *
 * @author xxm
 * @since 2021/3/19
 */
@Repository
@RequiredArgsConstructor
public class WeChatPayConfigManager extends BaseManager<WeChatPayConfigMapper, WeChatPayConfig> {

}
