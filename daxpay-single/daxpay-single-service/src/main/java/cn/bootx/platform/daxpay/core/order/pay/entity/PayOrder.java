package cn.bootx.platform.daxpay.core.order.pay.entity;

import cn.bootx.platform.common.core.annotation.BigField;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.handler.JacksonRawTypeHandler;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.annotation.DbMySqlIndex;
import cn.bootx.table.modify.mysql.constants.MySqlFieldTypeEnum;
import cn.bootx.table.modify.mysql.constants.MySqlIndexType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 支付订单
 * @author xxm
 * @since 2023/12/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_order")
public class PayOrder extends MpBaseEntity {

    /** 关联的业务id */
    @DbMySqlIndex(comment = "业务业务号索引",type = MySqlIndexType.UNIQUE)
    @DbColumn(comment = "关联的业务id")
    private String businessNo;

    /** 标题 */
    @DbColumn(comment = "标题")
    private String title;

    /** 是否是异步支付 */
    @DbColumn(comment = "是否是异步支付")
    private boolean asyncPayMode;

    /** 是否是组合支付 */
    @DbColumn(comment = "是否是组合支付")
    private boolean combinationPayMode;

    /**
     * 异步支付渠道
     * @see PayChannelEnum#ASYNC_TYPE_CODE
     */
    @DbColumn(comment = "异步支付渠道")
    private String asyncPayChannel;

    /** 金额 */
    @DbColumn(comment = "金额")
    private Integer amount;

    /** 可退款余额 */
    @DbColumn(comment = "可退款余额")
    private Integer refundableBalance;

    /**
     * 退款信息列表
     * @see PayOrderRefundableInfo
     */
    @TableField(typeHandler = JacksonRawTypeHandler.class)
    @BigField
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @DbColumn(comment = "退款信息列表")
    private List<PayOrderRefundableInfo> refundableInfos;

    /**
     * 支付状态
     * @see PayStatusEnum
     */
    @DbColumn(comment = "支付状态")
    private String status;

    /** 支付时间 */
    @DbColumn(comment = "支付时间")
    private LocalDateTime payTime;

    /** 过期时间 */
    @DbColumn(comment = "过期时间")
    private LocalDateTime expiredTime;
}
