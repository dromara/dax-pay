package cn.bootx.platform.daxpay.service.func;

import cn.bootx.platform.daxpay.service.core.order.transfer.entity.TransferOrder;
import lombok.Getter;
import lombok.Setter;

/**
 * 转账抽象策略
 * @author xxm
 * @since 2024/3/21
 */
@Getter
@Setter
public abstract class AbsTransferStrategy implements PayStrategy{
    /** 转账订单 */
    private TransferOrder transferOrder;

    /**
     * 转账前操作
     */
    public void doBeforeHandler(){}

}
