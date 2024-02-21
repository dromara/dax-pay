package cn.bootx.platform.daxpay.service.core.order.refund.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrderExtra;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/2/22
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class RefundOrderExtraManager extends BaseManager<RefundOrderExtraMapper, RefundOrderExtra> {
}
