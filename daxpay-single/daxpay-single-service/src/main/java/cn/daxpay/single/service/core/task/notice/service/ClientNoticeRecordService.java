package cn.daxpay.single.service.core.task.notice.service;

import cn.daxpay.single.service.core.task.notice.dao.ClientNoticeRecordManager;
import cn.daxpay.single.service.core.task.notice.entity.ClientNoticeRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 消息通知发送记录
 * @author xxm
 * @since 2024/2/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientNoticeRecordService {
    private final ClientNoticeRecordManager recordManager;

    /**
     * 保存
     */
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    public void save(ClientNoticeRecord record){
        recordManager.save(record);
    }
}
