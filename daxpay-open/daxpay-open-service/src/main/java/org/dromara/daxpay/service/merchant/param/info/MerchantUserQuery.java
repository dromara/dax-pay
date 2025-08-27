package org.dromara.daxpay.service.merchant.param.info;

import cn.bootx.platform.common.mybatisplus.query.entity.SortParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商户用户查询参数
 * @author xxm
 * @since 2025/8/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "商户用户查询参数")
public class MerchantUserQuery extends SortParam {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "登录账号")
    private String account;
}
