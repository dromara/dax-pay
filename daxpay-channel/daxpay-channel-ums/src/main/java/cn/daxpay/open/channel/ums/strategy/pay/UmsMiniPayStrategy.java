package cn.daxpay.open.channel.ums.strategy.pay;

import cn.daxpay.open.channel.ums.service.direct.UmsDirectConfigAssembler;
import cn.daxpay.open.channel.ums.service.payment.pay.UmsPayService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import org.springframework.stereotype.Service;

/// # 银联商务(小程序)支付策略
///
/// 对应 [ProductEnum#UMS_MINI], 共享 [AbsUmsPayStrategy] 的支付执行逻辑。
@Service
public class UmsMiniPayStrategy extends AbsUmsPayStrategy {

    public UmsMiniPayStrategy(UmsPayService umsPayService, UmsDirectConfigAssembler umsDirectConfigAssembler) {
        super(umsPayService, umsDirectConfigAssembler);
    }

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UMS_MINI;
    }
}
