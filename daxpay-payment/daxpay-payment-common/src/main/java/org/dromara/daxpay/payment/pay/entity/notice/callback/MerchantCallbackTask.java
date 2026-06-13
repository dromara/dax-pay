package org.dromara.daxpay.payment.pay.entity.notice.callback;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.common.entity.merchant.MchAppBaseEntity;
import org.dromara.daxpay.payment.pay.convert.notice.MerchantCallbackConvert;
import org.dromara.daxpay.platform.core.enums.pay.notice.CallbackNoticeTypeEnum;
import org.dromara.daxpay.platform.core.enums.pay.trade.TradeTypeEnum;
import org.dromara.daxpay.payment.pay.result.notice.callback.MerchantCallbackTaskResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 客户回调消息任务
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_merchant_callback_task")
public class MerchantCallbackTask extends MchAppBaseEntity implements ToResult<MerchantCallbackTaskResult> {

    /// 本地交易ID
    private Long tradeId;

    /// 平台交易号
    private String tradeNo;

    /// 消息类型
    /// @see CallbackNoticeTypeEnum
    private String noticeType;

    /// 交易类型
    /// @see TradeTypeEnum
    private String tradeType;

    /// 消息内容 json
    private String content;

    /// 是否发送成功
    private boolean success;

    /// 下次发送时间
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private OffsetDateTime nextTime;

    /// 发送次数
    private Integer sendCount;

    /// 延迟重试次数
    private Integer delayCount;

    /// 发送地址
    private String url;

    /// 最后发送时间
    private OffsetDateTime latestTime;

    /// 转换
    @Override
    public MerchantCallbackTaskResult toResult() {
        return MerchantCallbackConvert.CONVERT.toResult(this);
    }
}

