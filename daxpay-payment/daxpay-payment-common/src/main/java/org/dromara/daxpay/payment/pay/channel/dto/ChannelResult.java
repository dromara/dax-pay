package org.dromara.daxpay.payment.pay.channel.dto;

import lombok.Data;

/// # 通道服务统一响应
///
/// 通道适配服务(daxpay-channel-one)统一返回结构，与通道服务侧 `DaxResult` 保持一致。
/// code 为 0 表示成功，其他值表示业务错误。
///
@Data
public class ChannelResult<T> {

    /// 状态码(0=成功)
    private int code;

    /// 提示信息
    private String msg;

    /// 业务数据
    private T data;

    /// 是否成功
    public boolean isSuccess() {
        return code == 0;
    }
}
