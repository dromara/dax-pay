package cn.daxpay.open.payment.app.merchant.service.trade;

import cn.daxpay.open.payment.merchant.service.trade.MchOrderCloseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 商户移动端-订单关闭服务
///
/// 转发至 [MchOrderCloseService]
@Service
@RequiredArgsConstructor
public class AppMerchantOrderCloseService {

    private final MchOrderCloseService mchOrderCloseService;

    /// 关闭订单
    public void close(Long containerId, String tradeType) {
        mchOrderCloseService.close(containerId, tradeType);
    }
}
