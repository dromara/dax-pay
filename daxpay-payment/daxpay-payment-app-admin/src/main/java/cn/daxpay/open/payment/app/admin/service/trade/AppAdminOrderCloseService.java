package cn.daxpay.open.payment.app.admin.service.trade;

import cn.daxpay.open.payment.admin.service.trade.OrderCloseAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 运营移动端-订单关闭统一服务
///
/// 逻辑转发至 [OrderCloseAdminService]。
@Service
@RequiredArgsConstructor
public class AppAdminOrderCloseService {

    private final OrderCloseAdminService orderCloseAdminService;

    /// 关闭订单(统一入口)
    public void close(Long containerId, String tradeType) {
        orderCloseAdminService.close(containerId, tradeType);
    }
}
