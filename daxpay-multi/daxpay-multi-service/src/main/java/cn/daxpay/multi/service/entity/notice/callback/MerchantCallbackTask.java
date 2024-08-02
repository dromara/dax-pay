package cn.daxpay.multi.service.entity.notice.callback;

import cn.daxpay.multi.core.enums.TradeTypeEnum;
import cn.daxpay.multi.core.result.order.PayOrderResult;
import cn.daxpay.multi.core.result.order.RefundOrderResult;
import cn.daxpay.multi.core.result.order.TransferOrderResult;
import cn.daxpay.multi.service.common.entity.MchBaseEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 客户回调消息任务
 * @author xxm
 * @since 2024/7/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_client_callback_task")
public class MerchantCallbackTask extends MchBaseEntity {

    /** 本地交易ID */
    private Long tradeId;

    /** 本地交易号 */
    private String tradeNo;

    /**
     * 交易类型
     * @see TradeTypeEnum
     */
    private String tradeType;

    /**
     * 消息内容
     * @see PayOrderResult
     * @see RefundOrderResult
     * @see TransferOrderResult
     */
    private String content;

    /** 是否发送成功 */
    private boolean success;

    /** 下次发送时间 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private LocalDateTime nextTime;

    /** 发送次数 */
    private Integer sendCount;

    /** 延迟次数 */
    private Integer delayCount;

    /** 发送地址 */
    private String url;

    /** 最后发送时间 */
    private LocalDateTime latestTime;
}
