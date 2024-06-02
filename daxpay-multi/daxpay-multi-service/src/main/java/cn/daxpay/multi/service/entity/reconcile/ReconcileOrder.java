package cn.daxpay.multi.service.entity.reconcile;

import cn.daxpay.multi.service.code.PayChannelEnum;
import cn.daxpay.multi.service.common.entity.MchEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * 对账订单
 * @author xxm
 * @since 2024/6/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_reconcile_order")
public class ReconcileOrder extends MchEntity {

    /** 对账号 */
    private String reconcileNo;

    /** 日期 */
    private LocalDate date;

    /**
     * 通道
     * @see PayChannelEnum
     */
    private String channel;

    /** 交易对账文件是否下载成功 */
    private boolean downOrUpload;

    /** 交易对账文件是否比对完成 */
    private boolean compare;

    /**
     * 账单是否一致
     */
    private String result;

    /** 错误码 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorCode;

    /** 错误信息 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;
}
