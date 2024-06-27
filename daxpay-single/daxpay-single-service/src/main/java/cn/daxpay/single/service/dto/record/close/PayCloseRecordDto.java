package cn.daxpay.single.service.dto.record.close;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.daxpay.single.core.code.PayChannelEnum;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.daxpay.single.service.code.PayCloseTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付关闭记录
 * @author xxm
 * @since 2024/1/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付关闭记录")
public class PayCloseRecordDto extends BaseDto {

    /** 订单号 */
    @DbColumn(comment = "订单号")
    private String orderNo;

    /** 商户订单号 */
    @DbColumn(comment = "商户订单号")
    private String bizOrderNo;

    /**
     * 关闭的支付通道
     * @see PayChannelEnum
     */
    @DbColumn(comment = "关闭的异步支付通道")
    private String channel;

    /**
     * 关闭类型 关闭/撤销
     * @see PayCloseTypeEnum
     */
    @DbColumn(comment = "关闭类型")
    private String closeType;

    /**
     * 是否关闭成功
     */
    @DbColumn(comment = "是否关闭成功")
    private boolean closed;

    /** 错误码 */
    @DbColumn(comment = "错误码", length = 10)
    private String errorCode;

    /** 错误信息 */
    @DbColumn(comment = "错误信息", length = 2048)
    private String errorMsg;

    /** 终端ip */
    @DbColumn(comment = "支付终端ip", length = 64)
    private String clientIp;
}
