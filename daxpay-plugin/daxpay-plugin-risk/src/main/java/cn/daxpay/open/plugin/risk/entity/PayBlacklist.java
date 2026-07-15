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
/// 平台级名单，维度为 IP / openId，不含商户号事前拉黑。
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("pay_blacklist")
public class PayBlacklist extends MpBaseEntity implements ToResult<PayBlacklistResult> {

    /// 类型
    /// @see PayBlacklistTypeEnum
    private String type;

    /// 名单值（IP 或 openId）
    private String value;

    /// 通道族（openId 建议 wechat/alipay；IP 可空）
    private String channel;

    /// 通道应用 AppId（可选，防 openId 跨应用误杀）
    private String channelAppId;

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
