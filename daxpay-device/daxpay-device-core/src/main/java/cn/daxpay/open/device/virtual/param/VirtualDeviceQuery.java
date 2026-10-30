package cn.daxpay.open.device.virtual.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 虚拟设备查询
///
/// (开源版无代理商域: 无 agentNo 维度)
@Data
@Accessors(chain = true)
@Schema(title = "虚拟设备查询")
public class VirtualDeviceQuery {

    @Schema(description = "商户号")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String mchNo;

    @Schema(description = "设备名称")
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    private String name;

    @Schema(description = "设备号")
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    private String deviceNo;

    @Schema(description = "批次号")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String batchNo;

    @Schema(description = "关联应用号")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String appId;

    @Schema(description = "使用门店号")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String storeNo;

    @Schema(description = "是否启用")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private Boolean enable;
}
