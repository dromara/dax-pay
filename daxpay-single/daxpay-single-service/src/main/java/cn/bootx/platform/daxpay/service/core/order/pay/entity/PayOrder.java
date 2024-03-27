package cn.bootx.platform.daxpay.service.core.order.pay.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.core.order.pay.convert.PayOrderConvert;
import cn.bootx.platform.daxpay.service.dto.order.pay.PayOrderDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlIndex;
import cn.bootx.table.modify.mysql.constants.MySqlIndexType;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 支付订单
 * @author xxm
 * @since 2023/12/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "支付订单")
@Accessors(chain = true)
@TableName(value = "pay_order",autoResultMap = true)
public class PayOrder extends MpBaseEntity implements EntityBaseFunction<PayOrderDto> {

    /** 关联的业务id */
    @DbMySqlIndex(comment = "业务业务号索引",type = MySqlIndexType.UNIQUE)
    @DbColumn(comment = "关联的业务号")
    private String businessNo;

    /** 标题 */
    @DbColumn(comment = "标题")
    private String title;

    /** 是否是组合支付 */
    @DbColumn(comment = "是否是组合支付")
    private boolean combinationPay;

    /** 是否是异步支付 */
    @DbColumn(comment = "是否是异步支付")
    private boolean asyncPay;

    /** 是否需要分账 */
    @DbColumn(comment = "是否需要分账")
    private boolean allocation;

    /**
     * 异步支付通道
     * @see PayChannelEnum#ASYNC_TYPE_CODE
     */
    @DbColumn(comment = "异步支付通道")
    private String asyncChannel;

    /**
     *  如果有异步支付的情况下, 保存关联网关订单号
     */
    @DbColumn(comment = "网关订单号")
    private String gatewayOrderNo;

    /** 金额 */
    @DbColumn(comment = "金额")
    private Integer amount;

    /** 可退款余额 */
    @DbColumn(comment = "可退款余额")
    private Integer refundableBalance;

    /**
     * 支付状态
     * @see PayStatusEnum
     */
    @DbColumn(comment = "支付状态")
    private String status;

    /** 支付时间 */
    @DbColumn(comment = "支付时间")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private LocalDateTime payTime;

    /** 关闭时间 */
    @DbColumn(comment = "关闭时间")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private LocalDateTime closeTime;

    /** 过期时间 */
    @DbColumn(comment = "过期时间")
    private LocalDateTime expiredTime;

    /**
     * 转换
     */
    @Override
    public PayOrderDto toDto() {
        return PayOrderConvert.CONVERT.convert(this);
    }
}
