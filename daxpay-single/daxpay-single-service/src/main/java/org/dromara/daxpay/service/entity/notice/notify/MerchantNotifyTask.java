package org.dromara.daxpay.service.entity.notice.notify;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.convert.notice.MerchantNotifyConvert;
import org.dromara.daxpay.service.enums.NotifyContentTypeEnum;
import org.dromara.daxpay.service.result.notice.notify.MerchantNotifyTaskResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 客户订阅通知消息任务
 * @author xxm
 * @since 2024/7/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_merchant_notify_task")
public class MerchantNotifyTask extends MchAppBaseEntity implements ToResult<MerchantNotifyTaskResult> {

    /** 本地交易ID */
    private Long tradeId;

    /** 平台交易号 */
    private String tradeNo;

    /**
     * 通知类型
     * @see NotifyContentTypeEnum
     */
    private String notifyType;

    /** 消息内容 */
    private String content;

    /** 是否发送成功 */
    private boolean success;

    /** 发送次数 */
    private Integer sendCount;

    /** 延迟次数 */
    private Integer delayCount;

    /** 下次发送时间 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private LocalDateTime nextTime;

    /** 最后发送时间 */
    private LocalDateTime latestTime;

    /**
     * 转换
     */
    @Override
    public MerchantNotifyTaskResult toResult() {
        return MerchantNotifyConvert.CONVERT.toResult(this);
    }
}
