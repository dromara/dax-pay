package cn.daxpay.open.channel.union.strategy.pay;

import cn.daxpay.open.channel.union.service.direct.UnionDirectConfigAssembler;
import cn.daxpay.open.channel.union.service.payment.close.UnionCloseService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import org.springframework.stereotype.Service;

/// # 云闪付(B扫C)关闭策略
@Service
public class UnionBarcodeCloseStrategy extends AbsUnionCloseStrategy {

    public UnionBarcodeCloseStrategy(UnionCloseService unionCloseService, UnionDirectConfigAssembler unionDirectConfigAssembler) {
        super(unionCloseService, unionDirectConfigAssembler);
    }

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UNION_BARCODE;
    }
}
