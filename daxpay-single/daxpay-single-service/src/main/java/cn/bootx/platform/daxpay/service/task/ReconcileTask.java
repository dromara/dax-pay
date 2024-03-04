package cn.bootx.platform.daxpay.service.task;

import cn.bootx.platform.daxpay.service.task.service.ReconcileTaskService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

/**
 * 对账任务下载和保存定时任务
 * @author xxm
 * @since 2024/1/20
 */
@Slf4j
@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
@RequiredArgsConstructor
public class ReconcileTask implements Job {
    private final ReconcileTaskService reconcileTaskService;

    /**
     * 任务参数, 格式
     * {"channel":"ali_pay","n":1}
     */
    @Setter
    private String parameter;

    /**
     * 任务实现
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        if (StrUtil.isBlank(parameter)){
            log.warn("传输参数为空");
            return;
        }
        Parameter bean = JSONUtil.toBean(parameter, Parameter.class);
        // 日期, 如果未配置T+N规则, 默认为T+1
        LocalDate date = LocalDate.now();
        if (Objects.nonNull(bean.n)){
            date = date.minusDays(bean.n);
        } else {
            date = date.minusDays(1);
        }
        reconcileTaskService.reconcileTask(date,bean.channel);
    }

    /**
     * 接收参数
     */
    @Getter
    @Setter
    public static class Parameter {
        /**
         * 要同步的通道
         */
        private String channel;

        /**
         * 同步账单规则是 T+N 天, 例如T+1就是同步昨天的账单, T+2就是同步前天的账单
         */
        private Integer n;
    }
}
