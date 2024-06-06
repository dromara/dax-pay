package cn.daxpay.single.service.core.order.transfer.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.single.service.core.order.transfer.entity.TransferOrderExtra;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 转账订单扩展数据
 * @author xxm
 * @since 2024/6/6
 */
@Slf4j
@Repository
public class TransferOrderExtraManager extends BaseManager<TransferOrderExtraMapper, TransferOrderExtra> {
}
