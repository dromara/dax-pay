package org.dromara.daxpay.service.convert.reconcile;

import org.dromara.daxpay.service.bo.reconcile.ChannelReconcileTradeBo;
import org.dromara.daxpay.service.entity.reconcile.ChannelReconcileTrade;
import org.dromara.daxpay.service.entity.reconcile.ReconcileDiscrepancy;
import org.dromara.daxpay.service.entity.reconcile.ReconcileStatement;
import org.dromara.daxpay.service.result.reconcile.ReconcileDiscrepancyResult;
import org.dromara.daxpay.service.result.reconcile.ReconcileStatementResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 对账转换类
 * @author xxm
 * @since 2024/8/6
 */
@Mapper
public interface ReconcileConvert {
    ReconcileConvert CONVERT = Mappers.getMapper(ReconcileConvert.class);

    ReconcileStatementResult toResult(ReconcileStatement in);

    ReconcileDiscrepancyResult toResult(ReconcileDiscrepancy in);

    ChannelReconcileTrade toEntity(ChannelReconcileTradeBo in);

    List<ChannelReconcileTrade> toList(List<ChannelReconcileTradeBo> in);

}
