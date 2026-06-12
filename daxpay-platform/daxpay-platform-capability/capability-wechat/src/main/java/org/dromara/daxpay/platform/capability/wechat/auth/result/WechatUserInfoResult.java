package org.dromara.daxpay.platform.capability.wechat.auth.result;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信用户信息结果
///
@Data
@Accessors(chain = true)
public class WechatUserInfoResult {

    /// 用户OpenId
    private String openId;

    /// 用户昵称
    private String nickname;

    /// 用户头像
    private String headImgUrl;

    /// 性别：0-未知，1-男，2-女
    private Integer sex;

    /// 国家
    private String country;

    /// 省份
    private String province;

    /// 城市
    private String city;

    /// 语言
    private String language;

    /// 是否关注公众号
    private Boolean subscribe;

    /// 关注时间
    private Long subscribeTime;

    /// UnionId
    private String unionId;
}
