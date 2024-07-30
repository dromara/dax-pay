package cn.daxpay.multi.service.entity.notice.notify;

import cn.daxpay.multi.service.enums.NotifyTypeEnum;
import cn.daxpay.multi.service.common.entity.MchBaseEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 客户通知消息任务
 * @author xxm
 * @since 2024/7/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_client_notify_task")
public class ClientNotifyTask extends MchBaseEntity {

    /** 本地交易ID */
    private Long tradeId;

    /** 本地交易号 */
    private String tradeNo;

    /**
     * 通知类型
     * @see NotifyTypeEnum
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
}
