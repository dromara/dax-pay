package org.dromara.daxpay.service.entity.allocation.order;

import java.util.List;

/**
 * 分账订单和分账明细
 * @author xxm
 * @since 2024/11/14
 */
public record AllocAndDetail(AllocOrder transaction, List<AllocDetail> details) {}
