package cn.daxpay.open.channel.union.strategy.pay;

import cn.daxpay.open.channel.union.service.direct.UnionDirectConfigAssembler;
import cn.daxpay.open.channel.union.service.payment.pay.UnionPayService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import org.springframework.stereotype.Service;

/// # 云闪付(C扫B 主扫)支付策略
///
/// 对应 [ProductEnum#UNION_QRCODE], 共享 [AbsUnionPayStrategy] 的支付执行逻辑。
@Service
public class UnionQrcodePayStrategy extends AbsUnionPayStrategy {

    public UnionQrcodePayStrategy(UnionPayService unionPayService, UnionDirectConfigAssembler unionDirectConfigAssembler) {
        super(unionPayService, unionDirectConfigAssembler);
    }

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.UNION_QRCODE;
    }
}
