package cn.bootx.platform.daxpay.service.task;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.service.core.payment.notice.service.ClientNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 消息通知任务
 * @author xxm
 * @since 2024/2/22
 */
@Slf4j
@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
@RequiredArgsConstructor
public class ClientNoticeTask implements Job {

    private final ClientNoticeService clientNoticeService;


    /**
     * 通知任务执行
     */
    @Override
    public void execute(JobExecutionContext context){
        // 获取当前时间, 然后查询当前时间及以前需要进行通知的消息
        LocalDateTime now = LocalDateTime.now();
        clientNoticeService.taskStart(0, LocalDateTimeUtil.timestamp(now));
    }
}
