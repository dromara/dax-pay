package cn.bootx.platform.daxpay.service.core.order.reconcile.conver;

import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileTradeDetail;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileDiff;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileOrder;
import cn.bootx.platform.daxpay.service.dto.order.reconcile.ReconcileTradeDetailDto;
import cn.bootx.platform.daxpay.service.dto.order.reconcile.ReconcileDiffDto;
import cn.bootx.platform.daxpay.service.dto.order.reconcile.ReconcileOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/1/22
 */
@Mapper
public interface ReconcileConvert {
    ReconcileConvert CONVERT = Mappers.getMapper(ReconcileConvert.class);

    ReconcileTradeDetailDto convert(ReconcileTradeDetail in);

    ReconcileDiffDto convert(ReconcileDiff in);

    ReconcileOrderDto convert(ReconcileOrder in);
}
