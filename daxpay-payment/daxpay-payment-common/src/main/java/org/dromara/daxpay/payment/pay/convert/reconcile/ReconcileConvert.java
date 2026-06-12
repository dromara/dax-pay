package org.dromara.daxpay.payment.pay.convert.reconcile;

import org.dromara.daxpay.payment.pay.bo.reconcile.ChannelReconcileTradeBo;
import org.dromara.daxpay.payment.pay.entity.reconcile.ChannelReconcileTrade;
import org.dromara.daxpay.payment.pay.entity.reconcile.ReconcileDiscrepancy;
import org.dromara.daxpay.payment.pay.entity.reconcile.ReconcileStatement;
import org.dromara.daxpay.payment.pay.result.reconcile.ReconcileDiscrepancyResult;
import org.dromara.daxpay.payment.pay.result.reconcile.ReconcileStatementResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/// # 对账转换类
///
@Mapper
public interface ReconcileConvert {
    ReconcileConvert CONVERT = Mappers.getMapper(ReconcileConvert.class);

    ReconcileStatementResult toResult(ReconcileStatement in);

    ReconcileDiscrepancyResult toResult(ReconcileDiscrepancy in);

    ChannelReconcileTrade toEntity(ChannelReconcileTradeBo in);

    List<ChannelReconcileTrade> toList(List<ChannelReconcileTradeBo> in);

}
