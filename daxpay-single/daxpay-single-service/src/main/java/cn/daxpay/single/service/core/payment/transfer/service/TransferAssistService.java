package cn.daxpay.single.service.core.payment.transfer.service;

import cn.daxpay.single.param.payment.transfer.TransferParam;
import cn.daxpay.single.result.transfer.TransferResult;
import cn.daxpay.single.service.core.order.transfer.entity.TransferOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 转账辅助服务
 * @author xxm
 * @since 2024/6/6
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferAssistService {

    /**
     * 创建转账订单
     */
    public TransferOrder createOrder(TransferParam param) {
        // 1. 创建转账订单

        return null;
    }

    /**
     * 更新转账订单错误信息
     */
    public void updateOrderByError(TransferOrder order) {

    }

    /**
     * 构造
     * @param order
     */
    public TransferResult buildResult(TransferOrder order) {

        return null;
    }
}
