package cn.bootx.platform.daxpay.service.param.channel.cash;

import cn.bootx.platform.common.core.annotation.QueryParam;
import cn.bootx.platform.common.core.rest.param.QueryOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *
 * @author xxm
 * @since 2024/2/18
 */
@QueryParam
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "现金记录查询参数")
public class CashRecordQuery extends QueryOrder {
}
