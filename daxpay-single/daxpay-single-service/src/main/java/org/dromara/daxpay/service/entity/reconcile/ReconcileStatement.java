package org.dromara.daxpay.service.entity.reconcile;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.convert.reconcile.ReconcileConvert;
import org.dromara.daxpay.service.enums.ReconcileResultEnum;
import org.dromara.daxpay.service.result.reconcile.ReconcileStatementResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 对账报告
 * @author xxm
 * @since 2024/1/18
 */
@FieldNameConstants
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_reconcile_statement")
public class ReconcileStatement extends MchAppBaseEntity implements ToResult<ReconcileStatementResult> {

    /** 名称 */
    private String name;

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

    /** 支付交易金额 */
    private BigDecimal orderAmount;

    /** 退款订单数 */
    private Integer refundCount;

    /** 退款交易金额 */
    private BigDecimal refundAmount;

    /** 通道支付订单数 */
    private Integer channelOrderCount;

    /** 通道支付交易金额 */
    private BigDecimal channelOrderAmount;

    /** 通道退款订单数 */
    private Integer channelRefundCount;

    /** 通道退款交易金额 */
    private BigDecimal channelRefundAmount;

    /**
     * 交易对账结果
     * @see ReconcileResultEnum
     */
    private String result;

    /**
     * 原始通道对账单文件url
     */
    private String channelFileUrl;

    /**
     * 生成平台对账单文件url
     */
    private String platformFileUrl;

    /** 错误码 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorCode;

    /** 错误信息 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;

    /**
     * 转换
     */
    @Override
    public ReconcileStatementResult toResult() {
        return ReconcileConvert.CONVERT.toResult(this);
    }
}
