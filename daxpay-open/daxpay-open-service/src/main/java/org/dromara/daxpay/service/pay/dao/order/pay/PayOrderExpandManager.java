package org.dromara.daxpay.service.pay.dao.order.pay;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.service.pay.entity.order.pay.PayOrderExpand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 支付订单扩展信息
 * @author xxm
 * @since 2025/6/16
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayOrderExpandManager extends BaseManager<PayOrderExpandMapper, PayOrderExpand> {

}
