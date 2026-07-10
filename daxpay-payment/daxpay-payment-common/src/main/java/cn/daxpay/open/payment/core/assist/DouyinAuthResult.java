package cn.daxpay.open.payment.core.assist;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 抖音 H5 授权结果
///
/// 封装抖音开放平台 `/oauth/access_token/` 换票返回的核心字段(openId / accessToken)。
///
@Data
@Accessors(chain = true)
public class DouyinAuthResult {

    /// 抖音用户唯一标识
    private String openId;

    /// 接口调用凭证
    private String accessToken;
}
