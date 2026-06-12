package org.dromara.daxpay.platform.capability.wechat.auth.result;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信手机号结果
///
@Data
@Accessors(chain = true)
public class WechatPhoneResult {

    /// 用户手机号（带国家码）
    private String phoneNumber;

    /// 纯手机号（不带国家码）
    private String purePhoneNumber;

    /// 国家码
    private String countryCode;
}
