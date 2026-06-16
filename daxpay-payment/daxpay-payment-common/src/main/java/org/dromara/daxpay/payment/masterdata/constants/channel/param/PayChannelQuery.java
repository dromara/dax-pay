package org.dromara.daxpay.payment.masterdata.constants.channel.param;

import org.dromara.daxpay.platform.common.mybatisplus.query.entity.SortParam;
import org.dromara.daxpay.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付通道查询参数
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付通道查询参数")
public class PayChannelQuery extends SortParam {

    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "通道编码")
    private String code;
}
