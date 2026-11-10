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

    /// 状态筛选: 待激活(已启用且未绑定播报凭证)
    public static final String STATUS_PENDING = "pending";

    /// 状态筛选: 在线(已启用、已激活且在心跳窗口内)
    public static final String STATUS_ONLINE = "online";

    /// 状态筛选: 离线(已启用、已激活但不在心跳窗口内)
    public static final String STATUS_OFFLINE = "offline";

    /// 状态筛选: 已停用(启用状态为否)
    public static final String STATUS_DISABLED = "disabled";

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

    /// 关键字(设备号/名称 模糊匹配, 二者取并集)
    /// 非实体列, 由 [cn.daxpay.open.device.virtual.dao.VirtualDeviceManager#page] 手工展开为 OR 条件
    @Schema(description = "关键字(设备号/名称)")
    @QueryParam(ignore = true)
    private String keyword;

    /// 状态筛选(pending/online/offline/disabled, 见本类常量)
    /// 非实体列, 由 [cn.daxpay.open.device.virtual.dao.VirtualDeviceManager#page] 手工展开;
    /// online/offline 依赖 Redis 在线名单, 由服务层查得后传入
    @Schema(description = "状态筛选")
    @QueryParam(ignore = true)
    private String status;
}
