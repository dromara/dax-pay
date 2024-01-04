package cn.bootx.platform.daxpay.service.core.record.refund.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.code.PayRefundStatusEnum;
import cn.bootx.platform.daxpay.service.common.entity.OrderRefundableInfo;
import cn.bootx.platform.daxpay.service.common.typehandler.RefundableInfoTypeHandler;
import cn.bootx.platform.daxpay.service.core.record.refund.convert.RefundConvert;
import cn.bootx.platform.daxpay.service.dto.record.refund.PayRefundOrderDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.constants.MySqlFieldTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 退款记录
 *
 * @author xxm
 * @since 2022/3/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "退款订单")
@TableName(value = "pay_refund_order", autoResultMap = true)
public class PayRefundOrder extends MpBaseEntity implements EntityBaseFunction<PayRefundOrderDto> {

    /** 支付单号 */
    @DbColumn(comment = "关联的业务号")
    private Long paymentId;

    /** 关联的业务号 */
    @DbColumn(comment = "关联的业务号")
    private String businessNo;

    /** 异步方式关联退款请求号(部分退款情况) */
    @DbColumn(comment = "异步方式关联退款请求号(部分退款情况)")
    private String refundRequestNo;

    /** 标题 */
    @DbColumn(comment = "标题")
    private String title;

    /** 退款金额 */
    @DbColumn(comment = "退款金额")
    private Integer amount;

    /** 剩余可退 */
    @DbColumn(comment = "剩余可退")
    private Integer refundableBalance;

    /** 请求链路ID */
    @DbColumn(comment = "请求链路ID")
    private String reqId;

    /** 退款终端ip */
    @DbColumn(comment = "退款终端ip")
    private String clientIp;

    /** 退款原因 */
    @DbColumn(comment = "退款原因")
    private String reason;

    /** 退款时间 */
    @DbColumn(comment = "退款时间")
    private LocalDateTime refundTime;

    /**
     * 退款信息列表
     */
    @DbColumn(comment = "退款信息列表")
    @TableField(typeHandler = RefundableInfoTypeHandler.class)
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    private List<OrderRefundableInfo> refundableInfo;

    /**
     * 退款状态
     * @see PayRefundStatusEnum
     */
    @DbColumn(comment = "退款状态")
    private String status;

    /** 错误码 */
    @DbColumn(comment = "错误码")
    private String errorCode;

    /** 错误信息 */
    @DbColumn(comment = "错误信息")
    private String errorMsg;

    @Override
    public PayRefundOrderDto toDto() {
        return RefundConvert.CONVERT.convert(this);
    }

}
