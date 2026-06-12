package org.dromara.daxpay.payment.merchant.param.info;

import org.dromara.daxpay.platform.common.mybatisplus.query.entity.SortParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 商户用户查询参数
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "商户用户查询参数")
public class MerchantUserQuery extends SortParam {

    /// 商户号
    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "登录账号")
    private String account;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "账号状态")
    private String status;
}
