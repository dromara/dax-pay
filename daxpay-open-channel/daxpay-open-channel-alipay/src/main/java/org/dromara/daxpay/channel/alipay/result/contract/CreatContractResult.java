package org.dromara.daxpay.channel.alipay.result.contract;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * @author xxm
 * @since 2024/11/5
 */
@Data
@Accessors(chain = true)
public class CreatContractResult {

    /**
     * 【描述】本次代商户操作的全局唯一事务编号，后续代商户创建小程序、代签约当面付等产品、提交事务等接口都需要传递该batch_no值，
     * 且要确认只有 init 状态的batch_no才能发起调用。
     * 【示例值】2017110616474516400082883
     */
    @JsonAlias("batch_no")
    private String batchNo;
    /**
     * 【描述】ISV 代商户操作事务状态，事务状态包括： init=初始状态，本接口alipay.open.agent.create返回 init 状态，
     * 只有init状态允许进行各种业务接口调用； submit=提交状态，事务已经到达终态，
     * 调用alipay.open.agent.confirm接口可以提交init状态的事务 cancel=取消状态，事务已经到达终态，
     * 调用alipay.open.agent.cancel接口可以取消init状态的事务 timeout=超时状态，事务已经到达终态，
     * init状态的事务，在【1个小时】后会自动超时 注意：只有 init 状态才允许进行接口调用，其
     * 它状态都是终态，不允许继续进行接口调用。
     * 【示例值】init
     *
     */
    @JsonAlias("batch_status")
    private String batchStatus;
}
