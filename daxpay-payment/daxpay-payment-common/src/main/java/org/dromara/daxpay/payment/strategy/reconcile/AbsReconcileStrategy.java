package org.dromara.daxpay.payment.strategy.reconcile;

import org.dromara.daxpay.payment.common.strategy.PaymentStrategy;
import org.dromara.daxpay.payment.old.pay.bo.reconcile.ReconcileResolveResultBo;
import org.dromara.daxpay.payment.old.pay.entity.reconcile.ReconcileStatement;
import org.dromara.daxpay.platform.core.enums.pay.reconcile.ReconcileFileTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/// # 抽象对账策略
///
@Setter
@Getter
public abstract class AbsReconcileStrategy implements PaymentStrategy {

    /// 对账单
    private ReconcileStatement statement;

    /// 对账前处理, 主要是初始化支付SDK配置
    public void doBeforeHandler() {
    }

    /// 上传对账单解析并保存
    public abstract ReconcileResolveResultBo uploadAndResolve(MultipartFile file, ReconcileFileTypeEnum fileType);

    /// 下载对账单到本地进行保存
    public abstract ReconcileResolveResultBo downAndResolve();

}
