package cn.daxpay.open.channel.union.strategy.pay;

import cn.daxpay.open.channel.union.service.direct.UnionDirectConfigAssembler;
import cn.daxpay.open.channel.union.service.payment.close.UnionCloseService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import org.springframework.stereotype.Service;

/// # 云闪付(C扫B)关闭策略
@Service
public class UnionQrcodeCloseStrategy extends AbsUnionCloseStrategy {

    public UnionQrcodeCloseStrategy(UnionCloseService unionCloseService, UnionDirectConfigAssembler unionDirectConfigAssembler) {
        super(unionCloseService, unionDirectConfigAssembler);
    }

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UNION_QRCODE;
    }
}
