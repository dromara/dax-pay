package cn.daxpay.open.platform.core.enums.unipay;

import lombok.AllArgsConstructor;
import lombok.Getter;

/// # 通道认证状态
///
@Getter
@AllArgsConstructor
public enum ChannelAuthStatusEnum {

    /// 获取中
    WAITING("waiting"),
    /// 获取成功
    SUCCESS("success"),
    /// 数据不存在
    NOT_EXIST("not_exist"),
    /// 获取失败(认证异常, 供前端轮询及时退出, 避免死等 TTL)
    FAIL("fail");

    private final String code;

}
