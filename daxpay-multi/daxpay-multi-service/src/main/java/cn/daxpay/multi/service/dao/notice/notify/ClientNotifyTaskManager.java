package cn.daxpay.multi.service.dao.notice.notify;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.multi.service.entity.notice.notify.ClientNotifyTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/7/30
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ClientNotifyTaskManager extends BaseManager<ClientNotifyTaskMapper, ClientNotifyTask> {
}
