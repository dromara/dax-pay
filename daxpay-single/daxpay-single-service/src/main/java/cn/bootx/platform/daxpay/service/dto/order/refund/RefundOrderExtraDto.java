package cn.bootx.platform.daxpay.service.dto.order.refund;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.param.channel.AliPayParam;
import cn.bootx.platform.daxpay.param.channel.WalletPayParam;
import cn.bootx.platform.daxpay.param.channel.WeChatPayParam;
import cn.bootx.table.modify.annotation.DbColumn;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 退款订单扩展信息
 * @author xxm
 * @since 2024/4/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "退款订单扩展信息")
public class RefundOrderExtraDto extends BaseDto {

    /** 异步通知地址 */
    private String notifyUrl;

    /** 商户扩展参数,回调时会原样返回, 以最后一次为准 */
    private String attach;

    /**
     * 附加参数 以最后一次为准
     * @see AliPayParam
     * @see WeChatPayParam
     * @see WalletPayParam
     */
    private String extraParam;

    /** 请求时间，时间戳转时间 */
    private LocalDateTime reqTime;

    /** 终端ip */
    private String clientIp;
}
