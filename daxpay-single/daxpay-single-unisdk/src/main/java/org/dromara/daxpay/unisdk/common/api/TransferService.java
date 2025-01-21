package org.dromara.daxpay.unisdk.common.api;

import org.dromara.daxpay.unisdk.common.bean.AssistOrder;
import org.dromara.daxpay.unisdk.common.bean.UniTransferOrder;

import java.util.Map;

/**
 * 转账服务
 *
 * @author Egan
 * <pre>
 *  email egan@egzosn.com
 *  date 2023/1/8
 *  </pre>
 */
public interface TransferService {

    /**
     * 转账
     *
     * @param transferOrder 转账订单
     * @return 结果
     */
    Map<String, Object> transfer(UniTransferOrder transferOrder);

    /**
     * 转账查询
     *
     * @param assistOrder 辅助交易订单
     * @return 对应的转账订单
     */
    Map<String, Object> transferQuery(AssistOrder assistOrder);

}
