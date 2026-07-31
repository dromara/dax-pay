package cn.daxpay.open.channel.ums.strategy.refund;

import cn.daxpay.open.channel.ums.service.direct.UmsDirectConfigAssembler;
import cn.daxpay.open.channel.ums.service.payment.refund.UmsRefundService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import org.springframework.stereotype.Service;

/// # 银联商务(公众号)退款策略
///
/// 对应 [ProductEnum#UMS_JSAPI], 共享 [AbsUmsRefundStrategy] 的退款执行逻辑。
@Service
public class UmsJsapiRefundStrategy extends AbsUmsRefundStrategy {

    public UmsJsapiRefundStrategy(UmsRefundService umsRefundService, UmsDirectConfigAssembler umsDirectConfigAssembler) {
        super(umsRefundService, umsDirectConfigAssembler);
    }

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UMS_JSAPI;
    }
}
