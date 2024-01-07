package cn.bootx.platform.daxpay.service.core.record.pay.entity;


import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
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
 * 支付订单扩展信息
 * @author xxm
 * @since 2023/12/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "支付订单扩展信息")
@TableName("pay_order_extra")
public class PayOrderExtra extends MpBaseEntity {

    /** 描述 */
    @DbColumn(comment = "描述")
    private String description;

    /** 支付终端ip */
    @DbColumn(comment = "支付终端ip")
    private String clientIp;

    /** 是否不启用异步通知 */
    @DbColumn(comment = "是否不启用异步通知，以最后一次为准")
    private boolean notNotify;

    /** 异步通知地址 */
    @DbColumn(comment = "异步通知地址，以最后一次为准")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String notifyUrl;

    /** 同步通知 */

    /** 签名类型 */
    @DbColumn(comment = "签名类型")
    private String signType;

    /** 签名 */
    @DbColumn(comment = "签名，以最后一次为准")
    private String sign;

    @Schema(description = "商户扩展参数,回调时会原样返回")
    private String attach;

    /** API版本号 */
    @DbColumn(comment = "API版本号")
    private String apiVersion;

    /** 请求时间，时间戳转时间, 以最后一次为准 */
    @DbColumn(comment = "请求时间，传输时间戳，以最后一次为准")
    private LocalDateTime reqTime;

    /** 错误码 */
    @DbColumn(comment = "错误码")
    private String errorCode;

    /** 错误信息 */
    @DbColumn(comment = "错误信息")
    private String errorMsg;

}
