package cn.bootx.platform.daxpay.service.func;

import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileDetail;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileOrder;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.GeneralReconcileRecord;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * 支付对账策略
 * @author xxm
 * @since 2024/1/18
 */
@Setter
@Getter
public abstract class AbsReconcileStrategy implements PayStrategy {

    /** 对账订单 */
    private ReconcileOrder recordOrder;

    /** 对账订单明细 */
    private List<ReconcileDetail> reconcileDetails;


    /**
     * 生成对账序列号
     */
    public abstract String generateSequence(LocalDate date);

    /**
     * 对账前处理, 主要是初始化支付SDK配置
     */
    public void doBeforeHandler() {
    }

    /**
     * 下载对账单到本地进行保存
     */
    public abstract void downAndSave();

    /**
     * 获取通用对账对象, 将流水记录转换为对账对象
     */
    public abstract List<GeneralReconcileRecord> getGeneralReconcileRecord();

}
