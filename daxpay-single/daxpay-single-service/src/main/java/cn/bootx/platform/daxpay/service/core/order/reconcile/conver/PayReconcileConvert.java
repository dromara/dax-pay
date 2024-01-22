package cn.bootx.platform.daxpay.service.core.order.reconcile.conver;

import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.PayReconcileDetail;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.PayReconcileOrder;
import cn.bootx.platform.daxpay.service.dto.order.reconcile.PayReconcileDetailDto;
import cn.bootx.platform.daxpay.service.dto.order.reconcile.PayReconcileOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/1/22
 */
@Mapper
public interface PayReconcileConvert {
    PayReconcileConvert CONVERT = Mappers.getMapper(PayReconcileConvert.class);

    PayReconcileDetailDto convert(PayReconcileDetail in);

    PayReconcileOrderDto convert(PayReconcileOrder in);
}
