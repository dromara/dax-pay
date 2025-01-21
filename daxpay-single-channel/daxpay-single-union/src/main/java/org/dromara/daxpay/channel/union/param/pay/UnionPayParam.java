package org.dromara.daxpay.channel.union.param.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 云闪付支付参数
 * @author xxm
 * @since 2024/3/6
 */
@Data
@Schema(title = "云闪付支付参数")
public class UnionPayParam {

    @Schema(description = "授权码(主动扫描用户的付款码)")
    private String authCode;

}
