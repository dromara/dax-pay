package cn.daxpay.multi.service.convert.reconcile;

import cn.daxpay.multi.service.bo.reconcile.ChannelReconcileTradeBo;
import cn.daxpay.multi.service.entity.reconcile.ChannelReconcileTrade;
import cn.daxpay.multi.service.entity.reconcile.ReconcileDiscrepancy;
import cn.daxpay.multi.service.entity.reconcile.ReconcileStatement;
import cn.daxpay.multi.service.result.reconcile.ReconcileDiscrepancyResult;
import cn.daxpay.multi.service.result.reconcile.ReconcileStatementResult;
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
