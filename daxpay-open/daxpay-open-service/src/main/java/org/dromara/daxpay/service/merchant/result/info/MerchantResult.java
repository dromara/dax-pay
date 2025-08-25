package org.dromara.daxpay.service.merchant.result.info;

import cn.bootx.platform.core.result.BaseResult;
import org.dromara.daxpay.core.enums.MerchantStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.dromara.core.trans.vo.TransPojo;

/**
 * 商户信息
 * @author xxm
 * @since 2024/6/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@Schema(title = "商户信息")
public class MerchantResult extends BaseResult implements TransPojo {

    /** 商户号 */
    @Schema(description = "商户号")
    private String mchNo;

    /** 商户名称 */
    @Schema(description = "商户名称")
    private String mchName;

    /** 商户简称 */
    @Schema(description = "商户简称")
    private String mchShortName;

    /** 是否有关联管理员 */
    @Schema(description = "是否有关联管理员")
    private Boolean administrator;

    /** 关联管理员用户 */
    @Schema(description = "关联管理员用户")
    private Long adminUserId;

    /**
     * 商户状态
     * @see MerchantStatusEnum
     */
    @Schema(description = "商户状态")
    private String status;

}
