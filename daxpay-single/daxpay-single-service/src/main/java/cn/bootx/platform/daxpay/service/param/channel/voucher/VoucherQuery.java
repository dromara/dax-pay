package cn.bootx.platform.daxpay.service.param.channel.voucher;

import cn.bootx.platform.common.core.annotation.QueryParam;
import cn.bootx.platform.common.core.rest.param.QueryOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 储值卡查询参数
 * @author xxm
 * @since 2024/2/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@QueryParam
@Accessors(chain = true)
@Schema(title = "储值卡查询参数")
public class VoucherQuery extends QueryOrder {
}
