package cn.daxpay.open.payment.merchant.service.trade;

import cn.daxpay.open.payment.trade.runtime.service.close.PayCloseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 订单关闭统一管理服务(商户端)
///
/// 收归网关支付与普通支付的关闭入口, 委托 [PayCloseService#closeByContainer]。
@Service
@RequiredArgsConstructor
public class MchOrderCloseService {

    private final PayCloseService payCloseService;

    /// 关闭/撤销订单(统一入口)
    ///
    /// @param id        业务容器ID(pay_gateway_order / pay_normal_order 主键)
    /// @param type      容器类型: gateway=网关支付, normal=普通支付
    /// @param useCancel 是否以撤销方式关闭
    public void close(Long id, String type, boolean useCancel) {
        payCloseService.closeByContainer(id, type, useCancel);
    }
}
