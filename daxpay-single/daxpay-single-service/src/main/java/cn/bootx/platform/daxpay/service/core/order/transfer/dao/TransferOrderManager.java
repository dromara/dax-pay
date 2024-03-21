package cn.bootx.platform.daxpay.service.core.order.transfer.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.service.core.order.transfer.entity.TransferOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/3/21
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class TransferOrderManager extends BaseManager<TransferOrderMapper, TransferOrder> {
}
