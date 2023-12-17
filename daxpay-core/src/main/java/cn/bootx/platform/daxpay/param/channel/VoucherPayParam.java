package cn.bootx.platform.daxpay.param.channel;

import cn.bootx.platform.daxpay.param.ChannelParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 储值卡支付参数
 *
 * @author xxm
 * @since 2022/3/14
 */
@Data
@Schema(title = "储值卡支付参数")
public class VoucherPayParam implements ChannelParam {

    @Schema(description = "储值卡号")
    private String cardNo;

}
