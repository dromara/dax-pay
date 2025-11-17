package org.dromara.daxpay.payment.merchant.result.onboarded;

import cn.bootx.platform.core.result.BaseResult;
import org.dromara.daxpay.payment.merchant.entity.info.Merchant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.core.trans.anno.Trans;
import org.dromara.core.trans.constant.TransType;
import org.dromara.core.trans.vo.TransPojo;
import org.dromara.daxpay.payment.merchant.result.info.MchResult;

/**
 * 进件商户信息
 * @author xxm
 * @since 2025/11/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "进件商户信息")
public class OnbMchInfoResult extends MchResult {

    /** 进件商户号 */
    @Schema(description = "进件商户号")
    private String onbMchNo;

    /** 商户名称 */
    @Schema(description = "商户名称")
    private String onbMchName;

    /** 所属通道 */
    @Schema(description = "所属通道")
    private String onbChannel;
}
