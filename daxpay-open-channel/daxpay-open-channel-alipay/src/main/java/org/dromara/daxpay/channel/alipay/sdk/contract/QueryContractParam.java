package org.dromara.daxpay.channel.alipay.sdk.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 查询签约申请单状态参数
 * @author xxm
 * @since 2024/11/16
 */
@Data
@Accessors(chain = true)
public class QueryContractParam {
    /** 操作事务编号 */
    @JsonProperty("batch_no")
    private String batchNo;
}
