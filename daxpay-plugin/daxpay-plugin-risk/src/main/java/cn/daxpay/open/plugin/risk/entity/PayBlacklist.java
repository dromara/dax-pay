package cn.daxpay.open.plugin.risk.entity;

import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.plugin.risk.convert.PayBlacklistConvert;
import cn.daxpay.open.plugin.risk.enums.PayBlacklistStatusEnum;
import cn.daxpay.open.plugin.risk.enums.PayBlacklistTypeEnum;
import cn.daxpay.open.plugin.risk.result.PayBlacklistResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.time.OffsetDateTime;

/// # 支付黑名单配置
///
/// 平台级名单，不含商户号事前拉黑。
/// - [PayBlacklistTypeEnum#IP]：全局 IP
/// - [PayBlacklistTypeEnum#ALIPAY_USER]：支付宝 userId（不区分应用）
/// - [PayBlacklistTypeEnum#WECHAT_OPENID]：微信 openId，必须绑定 [wxAppId]
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("pay_blacklist")
public class PayBlacklist extends MpBaseEntity implements ToResult<PayBlacklistResult> {

    /// 类型
    /// @see PayBlacklistTypeEnum
    private String type;

    /// 名单值（IP / 支付宝 userId / 微信 openId）
    private String value;

    /// 微信平台支付应用 AppId（仅 wechat_openid 使用）
    private String wxAppId;

    /// 状态
    /// @see PayBlacklistStatusEnum
    private String status;

    /// 拉黑原因
    private String reason;

    /// 过期时间（空=永久）
    private OffsetDateTime expireTime;

    /// 备注
    private String remark;

    @Override
    public PayBlacklistResult toResult() {
        return PayBlacklistConvert.CONVERT.toResult(this);
    }
}
