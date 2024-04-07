package cn.bootx.platform.daxpay.service.core.order.allocation.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.service.core.order.allocation.convert.AllocationConvert;
import cn.bootx.platform.daxpay.service.dto.order.allocation.AllocationOrderDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.mysql.annotation.DbMySqlIndex;
import cn.bootx.table.modify.mysql.constants.MySqlIndexType;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class AllocationOrder extends MpBaseEntity implements EntityBaseFunction<AllocationOrderDto> {

    /**
     * 支付订单ID
     */
    @DbColumn(comment = "支付订单ID")
    private Long paymentId;

    /**
     * 网关支付订单号
     */
    @DbColumn(comment = "网关支付订单号")
    private String gatewayPayOrderNo;

    /**
     * 网关分账单号
     */
    @DbColumn(comment = "网关分账单号")
    private String gatewayAllocationNo;

    /**
     * 分账单号
     */
    @DbMySqlIndex(comment = "分账单号索引", type = MySqlIndexType.UNIQUE)
    @DbColumn(comment = "分账单号")
    private String allocationNo;


    /**
     * 外部请求号
     */
    @DbColumn(comment = "外部请求号")
    private String outReqNo;

    /**
     * 所属通道
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
     */
    @DbColumn(comment = "状态")
    private String status;

    /**
     * 错误原因
     */
    @DbColumn(comment = "错误原因")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;

    /**
     * 完成时间
     */
    @DbColumn(comment = "完成时间")
    private LocalDateTime finishTime;

    /**
     * 转换
     */
    @Override
    public AllocationOrderDto toDto() {
        return AllocationConvert.CONVERT.convert(this);
    }
}
