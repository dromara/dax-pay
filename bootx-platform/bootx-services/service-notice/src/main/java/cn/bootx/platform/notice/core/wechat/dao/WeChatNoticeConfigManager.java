package cn.bootx.platform.notice.core.wechat.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.notice.core.wechat.entity.WeChatNoticeConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 微信消息配置
 *
 * @author xxm
 * @since 2021/8/10
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class WeChatNoticeConfigManager extends BaseManager<WeChatNoticeConfigMapper, WeChatNoticeConfig> {

}
