package cn.daxpay.multi.service.dao.order.refund;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.multi.service.entity.order.refund.RefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/6/27
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class RefundOrderManager extends BaseManager<RefundOrderMapper, RefundOrder> {
}
