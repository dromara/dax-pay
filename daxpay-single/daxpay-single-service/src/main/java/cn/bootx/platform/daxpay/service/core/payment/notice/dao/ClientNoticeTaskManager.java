package cn.bootx.platform.daxpay.service.core.payment.notice.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.service.core.payment.notice.entity.ClientNoticeTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author xxm
 * @since 2024/2/20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientNoticeTaskManager extends BaseManager<ClientNoticeTaskMapper, ClientNoticeTask> {
}
