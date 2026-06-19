package cn.daxpay.open.payment.masterdata.constants.product.param;

import cn.daxpay.open.platform.common.mybatisplus.query.entity.SortParam;
import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付产品查询参数
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付产品查询参数")
public class PayProductQuery extends SortParam {

    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "产品编码")
    private String code;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "关联通道编码")
    private String channel;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "是否启用")
    private Boolean enable;
}