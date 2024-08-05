package cn.daxpay.multi.service.entity.reconcile;

import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.service.common.entity.MchBaseEntity;
import cn.daxpay.multi.service.enums.ReconcileResultEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 对账报告
 * @author xxm
 * @since 2024/1/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_channel_statement")
public class ReconcileStatement extends MchBaseEntity {

    /** 对账号 */
    private String reconcileNo;

    /** 日期 */
    private LocalDate date;

    /**
     * 通道
     * @see ChannelEnum
     */
    private String channel;

    /** 交易对账文件是否下载或上传成功 */
    private boolean downOrUpload;

    /** 交易对账文件是否比对完成 */
    private boolean compare;

    /** 支付订单数 */
    private Integer orderCount;

    /** 退款订单数 */
    private Integer refundCount;

    /** 通道支付订单数 */
    private Integer channelOrderCount;

    /** 通道退款订单数 */
    private Integer channelRefundCount;

    /** 支付交易金额 */
    private BigDecimal tradeAmount;

    /** 退款交易金额 */
    private BigDecimal refundAmount;

    /** 通道支付交易金额 */
    private BigDecimal channelTradeAmount;

    /** 通道退款交易金额 */
    private BigDecimal channelRefundAmount;

    /**
     * 交易对账结果
     * @see ReconcileResultEnum
     */
    private String result;

    /**
     *  原始通道对账单文件id
     */
    private Long channelFileId;

    /**
     * 生成平台对账单文件ID
     */
    private Long platformFileId;

    /** 错误码 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorCode;

    /** 错误信息 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;
}
