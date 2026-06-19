package cn.daxpay.open.payment.pay.order.entity;

import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 周期签约
///
/// 周期扣款场景的容器，记录签约计划与扣款进度
/// 每次扣款生成一笔 pay_trade（trade_type = recurring），关联到此签约
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pay_recurring")
public class PayRecurringOrder extends MchBaseEntity {

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String appId;

    /// 商户签约业务号
    private String bizOrderNo;

    /// 标题
    private String title;

    /// 描述
    private String description;

    /// 通道返回的协议号（微信模板号 / 支付宝协议号）
    private String contractNo;

    /// 单次扣款金额（固定金额时填，可变金额时为空，最小货币单位）
    private Long singleAmount;

    /// 签约总金额上限（可空，最小货币单位）
    private Long totalAmount;

    /// 币种 ISO 4217，默认 CNY
    private String currency;

    /// 周期类型（day / week / month / year）
    private String periodType;

    /// 周期数（如 periodType=month, periodCount=3 表示每 3 月）
    private Integer periodCount;

    /// 总扣款次数上限（空 = 无限期）
    private Integer totalCount;

    /// 已扣款次数
    private Integer executedCount;

    /// 下次计划扣款时间
    private OffsetDateTime nextExecuteTime;

    /// 签约状态
    /// @see RecurringStatusEnum
    private String status;

    /// 签约时间
    private OffsetDateTime signTime;

    /// 解约时间
    private OffsetDateTime cancelTime;

    /// 签约过期时间
    private OffsetDateTime expireTime;

    /// 异步通知地址
    private String notifyUrl;

    /// 同步跳转地址
    private String returnUrl;

    /// 商户附加参数
    private String attach;
}
