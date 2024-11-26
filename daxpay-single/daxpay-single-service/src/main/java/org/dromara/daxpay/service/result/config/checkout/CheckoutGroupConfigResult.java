package org.dromara.daxpay.service.result.config.checkout;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.result.MchAppResult;

/**
 * 收银台分类配置
 * @author xxm
 * @since 2024/11/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "")
public class CheckoutGroupConfigResult extends MchAppResult {
}
