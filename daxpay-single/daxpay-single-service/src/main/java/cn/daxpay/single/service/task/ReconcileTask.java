package cn.daxpay.single.service.task;

import cn.daxpay.single.service.core.order.reconcile.entity.ReconcileOrder;
import cn.daxpay.single.service.core.payment.reconcile.service.ReconcileService;
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
    private final ReconcileService reconcileService;

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
    public void execute(JobExecutionContext context) {
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
        this.reconcileTask(date,bean.channel);
    }


    /**
     * 执行任务
     */
    public void reconcileTask(LocalDate date, String channel){

        // 1. 查询需要定时对账的通道, 创建出来对账订单
        ReconcileOrder reconcileOrder = reconcileService.create(date, channel);

        // 2. 执行对账任务, 下载对账单并解析, 分别存储为原始数据和通用对账数据
        reconcileService.downAndSave(reconcileOrder);

        // 3. 执行账单明细比对, 生成差异单
        reconcileService.compare(reconcileOrder);
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
