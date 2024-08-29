package cn.daxpay.multi.gateway.task;

import cn.bootx.platform.core.annotation.IgnoreTenant;
import cn.daxpay.multi.service.service.reconcile.ReconcileStatementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 对账任务下载和保存定时任务
 * @author xxm
 * @since 2024/1/20
 */
@IgnoreTenant
@Slf4j
@Component
@RequiredArgsConstructor
public class ReconcileTask  {
    private final ReconcileStatementService reconcileService;

    /**
     * 任务实现
     */
    public void execute() {
    }



}
