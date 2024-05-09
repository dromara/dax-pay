package cn.daxpay.single.service.core.order.allocation.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.single.code.AllocOrderResultEnum;
import cn.daxpay.single.code.AllocOrderStatusEnum;
import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.service.core.order.allocation.convert.AllocationConvert;
import cn.daxpay.single.service.dto.order.allocation.AllocationOrderDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 分账订单
 * @author xxm
 * @since 2024/4/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "分账订单")
@TableName("pay_allocation_order")
public class AllocationOrder extends MpBaseEntity implements EntityBaseFunction<AllocationOrderDto> {

    /**
     * 分账单号
     */
    @DbColumn(comment = "分账单号")
    private String allocationNo;

    /**
     * 商户分账单号
     */
    @DbColumn(comment = "商户分账单号")
    private String bizAllocationNo;

    /**
     * 通道分账号
     */
    @DbColumn(comment = "通道分账号")
    private String outAllocationNo;

    /** 支付订单ID */
    @DbColumn(comment = "支付订单ID")
    private Long orderId;

    /**
     * 支付订单号
     */
    @DbColumn(comment = "支付订单号")
    private String orderNo;

    /**
     * 商户支付订单号
     */
    @DbColumn(comment = "商户支付订单号")
    private String bizOrderNo;

    /**
     * 通道系统支付订单号
     */
    @DbColumn(comment = "通道支付订单号")
    private String outOrderNo;

    /**
     * 支付订单标题
     */
    @Schema(description = "支付订单标题")
    private String title;


    /**
     * 所属通道
     * @see PayChannelEnum
     */
    @DbColumn(comment = "所属通道")
    private String channel;

    /**
     * 总分账金额
     */
    @DbColumn(comment = "总分账金额")
    private Integer amount;

    /**
     * 分账描述
     */
    @DbColumn(comment = "分账描述")
    private String description;

    /**
     * 状态
     * @see AllocOrderStatusEnum
     */
    @DbColumn(comment = "状态")
    private String status;

    /**
     * 分账处理结果
     * @see AllocOrderResultEnum
     */
    @DbColumn(comment = "分账处理结果")
    private String result;

    /**
     * 错误码
     */
    @DbColumn(comment = "错误码")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorCode;

    /**
     * 错误信息
     */
    @DbColumn(comment = "错误原因")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;

    /** 分账完成时间 */
    @DbColumn(comment = "分账完成时间")
    private LocalDateTime finishTime;

    /**
     * 转换
     */
    @Override
    public AllocationOrderDto toDto() {
        return AllocationConvert.CONVERT.convert(this);
    }
}
