package cn.daxpay.open.channel.ums.strategy.refund;

import cn.daxpay.open.channel.ums.service.direct.UmsDirectConfigAssembler;
import cn.daxpay.open.channel.ums.service.payment.refund.UmsRefundService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import org.springframework.stereotype.Service;

/// # 银联商务(H5)退款策略
///
/// 对应 [ProductEnum#UMS_H5], 共享 [AbsUmsRefundStrategy] 的退款执行逻辑。
@Service
public class UmsH5RefundStrategy extends AbsUmsRefundStrategy {

    public UmsH5RefundStrategy(UmsRefundService umsRefundService, UmsDirectConfigAssembler umsDirectConfigAssembler) {
        super(umsRefundService, umsDirectConfigAssembler);
    }

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UMS_H5;
    }
}
