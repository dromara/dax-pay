package cn.bootx.platform.daxpay.core.order.pay.entity;


import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 支付订单扩展信息
 * @author xxm
 * @since 2023/12/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_order_extra")
public class PayOrderExtra extends MpBaseEntity {

    /** 支付终端ip */
    @DbColumn(comment = "支付终端ip")
    private String clientIp;

    /** 描述 */
    @DbColumn(comment = "描述")
    private String description;

    @DbColumn(comment = "是否不进行同步通知的跳转")
    private boolean notReturn;

    /** 同步通知URL */
    @DbColumn(comment = "同步通知URL")
    private String returnUrl;

    /** 是否不启用异步通知 */
    @DbColumn(comment = "是否不启用异步通知")
    private boolean notNotify;

    /** 异步通知地址 */
    @DbColumn(comment = "异步通知地址")
    private String notifyUrl;

    /** 签名类型 */
    @DbColumn(comment = "签名类型")
    private String signType;

    /** 签名 */
    @DbColumn(comment = "签名")
    private String sign;

    /** API版本号 */
    @DbColumn(comment = "API版本号")
    private String apiVersion;

    /** 请求时间，时间戳转时间 */
    @DbColumn(comment = "请求时间，传输时间戳")
    private LocalDateTime reqTime;

    /** 错误码 */
    @DbColumn(comment = "错误码")
    private String errorCode;

    /** 错误信息 */
    @DbColumn(comment = "错误信息")
    private String errorMsg;

}
