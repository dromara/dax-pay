package cn.bootx.platform.daxpay.service.core.order.reconcile.conver;

import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileDetail;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileDiffRecord;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileOrder;
import cn.bootx.platform.daxpay.service.dto.order.reconcile.ReconcileDetailDto;
import cn.bootx.platform.daxpay.service.dto.order.reconcile.ReconcileDiffRecordDto;
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

    ReconcileDetailDto convert(ReconcileDetail in);

    ReconcileDiffRecordDto convert(ReconcileDiffRecord in);

    ReconcileOrderDto convert(ReconcileOrder in);
}
