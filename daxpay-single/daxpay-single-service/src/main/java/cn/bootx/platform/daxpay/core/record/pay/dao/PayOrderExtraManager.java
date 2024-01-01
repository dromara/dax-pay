package cn.bootx.platform.daxpay.core.record.pay.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.core.record.pay.entity.PayOrderExtra;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 支付订单扩展信息
 * @author xxm
 * @since 2023/12/20
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayOrderExtraManager extends BaseManager<PayOrderExtraMapper, PayOrderExtra> {
}
