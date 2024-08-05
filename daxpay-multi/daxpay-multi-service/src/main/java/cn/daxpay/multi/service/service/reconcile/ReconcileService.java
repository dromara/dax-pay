package cn.daxpay.multi.service.service.reconcile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 对账服务类
 * @author xxm
 * @since 2024/8/5
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReconcileService {

    /**
     * 手动创建对账任务
     */
    public void createReconcileTask() {
        log.info("手动创建对账任务");
    }
}
