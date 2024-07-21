package cn.daxpay.multi.service.param.record;

import cn.bootx.platform.common.mybatisplus.query.entity.SortParam;
import cn.daxpay.multi.core.enums.ChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付关闭记录
 * @author xxm
 * @since 2024/1/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付关闭记录")
public class PayCloseRecordQuery extends SortParam {

    /** 订单号 */
    @Schema(description = "订单号")
    private String orderNo;

    /** 商户订单号 */
    @Schema(description = "商户订单号")
    private String bizOrderNo;

    /**
     * 关闭的支付通道
     * @see ChannelEnum
     */
    @Schema(description = "关闭的支付通道")
    private String channel;

    /**
     * 是否关闭成功
     */
    @Schema(description = "是否关闭成功")
    private Boolean closed;

    /** 错误码 */
    @Schema(description = "错误码")
    private String errorCode;

    /** 错误消息 */
    @Schema(description = "错误消息")
    private String errorMsg;

    /** 终端ip */
    @Schema(description = "支付终端ip")
    private String clientIp;
}
