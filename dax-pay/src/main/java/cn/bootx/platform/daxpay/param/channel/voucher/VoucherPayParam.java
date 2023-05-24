package cn.bootx.platform.daxpay.param.channel.voucher;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 储值卡支付参数
 *
 * @author xxm
 * @date 2022/3/14
 */
@Data
@Accessors(chain = true)
@Schema(title = "储值卡支付参数")
public class VoucherPayParam {

    @Schema(description = "储值卡号")
    private List<String> cardNoList;

}
