package cn.daxpay.open.channel.mock.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # Mock 通道分账响应(发起/完结/同步共用)
///
/// 与通道子应用 one 侧 `cn.daxpay.open.channel.mock.resp.MockAllocResp` 镜像, 字段对齐。
/// mock 分账为整单三态(PROCESSING/SUCCESS/FAIL), 策略层把整单状态映射为全部明细同结果。
@Data
@Accessors(chain = true)
public class MockAllocResp {

    /// 分账单号(请求透传)
    private String allocNo;

    /// Mock 通道分账单号(MOCKA 前缀生成)
    private String outAllocNo;

    /// 分账状态(PROCESSING / SUCCESS / FAIL)
    private String status;

    /// 是否完结单(解冻剩余冻结资金, 无接收方明细)
    private boolean finish;

    /// 终态时间(成功/失败时返回, 处理中为 null)
    private OffsetDateTime finishTime;
}
