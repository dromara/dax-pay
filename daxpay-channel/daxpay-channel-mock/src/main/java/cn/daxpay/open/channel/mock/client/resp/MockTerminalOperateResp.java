package cn.daxpay.open.channel.mock.client.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/// # Mock 终端报备操作响应(主应用侧)
///
/// 与 mock 子应用 `channel.resp.MockTerminalOperateResp` 镜像, 字段一字不差(人工维护双份)。
/// 报备/同步返回通道侧终端号; status 为台账状态回读补充字段(主应用只消费 deviceNo)。
@Data
@Accessors(chain = true)
public class MockTerminalOperateResp {

    /// 通道侧终端号(MOCKT 前缀; 重复报备幂等回传原值)
    private String deviceNo;

    /// 台账状态(ACTIVE=已报备 / CANCELLED=已注销)
    private String status;
}
