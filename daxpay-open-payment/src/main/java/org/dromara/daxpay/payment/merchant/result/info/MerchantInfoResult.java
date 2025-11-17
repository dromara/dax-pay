package org.dromara.daxpay.payment.merchant.result.info;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.merchant.enums.MerchantProfileAuthEnum;
import org.dromara.daxpay.payment.merchant.enums.MerchantStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商户及相关信息
 * @author xxm
 * @since 2025/10/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "商户及相关信息")
public class MerchantInfoResult extends MchResult implements ToResult<MerchantInfoResult> {

    /** 商户号 */
    @Schema(description = "商户号")
    private String mchNo;

    /** 商户名称 */
    @Schema(description = "商户名称")
    private String mchName;

    /** 商户简称 */
    @Schema(description = "商户简称")
    private String mchShortName;

    /**
     * 主体认证状态
     * @see MerchantProfileAuthEnum
     */
    @Schema(description = "主体认证状态")
    private String profileAuth;

    @Schema(description = "主体认证错误信息")
    private String profileAuthErrorMsg;

    /**
     * 商户状态
     * @see MerchantStatusEnum
     */
    @Schema(description = "商户状态")
    private String status;

    /**
     * 转换
     */
    @Override
    public MerchantInfoResult toResult() {
        return this;
    }
}
