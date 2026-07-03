package cn.daxpay.open.payment.old.pay.event;

import cn.daxpay.open.payment.old.pay.dao.order.pay.PayOrderManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 订单交易相关的延时事件
///
/// 同步/关闭处理已迁移至 core, 本服务预留待后续事件逻辑迁移
@Slf4j
@Service
@RequiredArgsConstructor
public class TradeOrderEventService {

    private final PayOrderManager payOrderManager;
}
