package cn.bootx.platform.daxpay.service.core.order.refund.entity;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 退款订单扩展信息
 * @author xxm
 * @since 2024/2/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_refund_order_extra")
@DbTable(comment = "退款订单扩展信息")
public class RefundOrderExtra extends MpBaseEntity {

    /** 回调通知时是否需要进行签名 */
    @DbColumn(comment = "回调通知时是否需要进行签名")
    private boolean noticeSign;

    /** 异步通知地址 */
    @DbColumn(comment = "异步通知地址")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String notifyUrl;

    /** 商户扩展参数,回调时会原样返回 */
    @DbColumn(comment = "商户扩展参数")
    private String attach;

    /** 请求签名类型 */
    @DbColumn(comment = "请求签名值")
    private String reqSignType;

    /** 请求签名值 */
    @DbColumn(comment = "请求签名值")
    private String reqSign;

    /** 请求链路ID */
    @DbColumn(comment = "请求链路ID")
    private String reqId;

    /** 请求时间，时间戳转时间 */
    @DbColumn(comment = "请求时间，传输时间戳")
    private LocalDateTime reqTime;

    /** 终端ip */
    @DbColumn(comment = "支付终端ip")
    private String clientIp;

}
