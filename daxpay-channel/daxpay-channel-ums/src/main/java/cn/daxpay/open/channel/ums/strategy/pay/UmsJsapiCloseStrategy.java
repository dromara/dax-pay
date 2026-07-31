package cn.daxpay.open.channel.ums.strategy.pay;

import cn.daxpay.open.channel.ums.service.direct.UmsDirectConfigAssembler;
import cn.daxpay.open.channel.ums.service.payment.close.UmsCloseService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import org.springframework.stereotype.Service;

/// # 银联商务(公众号)支付关闭策略
///
/// 对应 [ProductEnum#UMS_JSAPI], 共享 [AbsUmsCloseStrategy] 的关单执行逻辑。
@Service
public class UmsJsapiCloseStrategy extends AbsUmsCloseStrategy {

    public UmsJsapiCloseStrategy(UmsCloseService umsCloseService, UmsDirectConfigAssembler umsDirectConfigAssembler) {
        super(umsCloseService, umsDirectConfigAssembler);
    }

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UMS_JSAPI;
    }
}
