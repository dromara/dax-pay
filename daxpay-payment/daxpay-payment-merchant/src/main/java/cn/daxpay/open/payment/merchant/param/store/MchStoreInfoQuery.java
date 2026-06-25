package cn.daxpay.open.payment.merchant.param.store;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 门店查询参数
///
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Accessors(chain = true)
@Schema(title = "门店查询参数")
public class MchStoreInfoQuery {

    /// 商户号
    @Schema(description = "商户号")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String mchNo;

    /// 门店号
    @Schema(description = "门店号")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String storeNo;

    /// 门店名称
    @Schema(description = "门店名称")
    private String storeName;

    /// 联系人电话
    @Schema(description = "联系人电话")
    private String contactPhone;

    /// 状态
    @Schema(description = "状态")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String status;
}
