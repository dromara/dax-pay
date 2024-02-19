package cn.bootx.platform.daxpay.service.core.channel.wechat.service;

import cn.bootx.platform.daxpay.service.core.channel.wechat.dao.WeChatPayRecordManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author xxm
 * @since 2024/2/19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPayRecordService {
    private final WeChatPayRecordManager weChatPayRecordManager;
}
