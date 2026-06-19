package cn.daxpay.open.payment.pay.order.entity;

import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import cn.daxpay.open.payment.common.enums.AuthStatusEnum;

import java.time.OffsetDateTime;

/// # 预授权协议
///
/// 预授权场景的容器，记录冻结总额和捕获/解冻累计
/// 关联 authorize 和 capture 等多笔 pay_trade
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pay_auth")
public class PayAuthOrder extends MchBaseEntity {

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String appId;

    /// 商户业务单号
    private String bizOrderNo;

    /// 标题
    private String title;

    /// 描述
    private String description;

    /// 冻结总额（最小货币单位）
    private Long frozenAmount;

    /// 已捕获累计（每次 capture 递增，最小货币单位）
    private Long capturedAmount;

    /// 已解冻累计（每次 unfreeze 递增，最小货币单位）
    private Long unfrozenAmount;

    /// 币种 ISO 4217，默认 CNY
    private String currency;

    /// 授权状态
    /// @see AuthStatusEnum
    private String status;

    /// 授权完成时间
    private OffsetDateTime authTime;

    /// 授权过期时间
    private OffsetDateTime expireTime;

    /// 异步通知地址
    private String notifyUrl;

    /// 同步跳转地址
    private String returnUrl;

    /// 商户附加参数
    private String attach;
}
