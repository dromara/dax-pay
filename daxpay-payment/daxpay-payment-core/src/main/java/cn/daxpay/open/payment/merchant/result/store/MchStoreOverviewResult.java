package cn.daxpay.open.payment.merchant.result.store;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 门店概况聚合结果(门店工作台)
///
/// 按门店聚合默认应用(门店级/商户级回退链)与码牌绑定计数, 供门店工作台一屏展示;
/// 由 [cn.daxpay.open.payment.merchant.service.store.MchStoreInfoService#overview] 组装。
/// 计数字段一律用 Integer(全局 Long/long 序列化为字符串契约, 前端按 number 接收)。
///
/// 开源版裁剪(对照 plus): 无云音箱/云打印/系统终端默认终端域, 收款设备(虚拟设备)域
/// 模块独立无法在此聚合, 设备计数由前端经收款设备分页接口 total 取数; 本结果仅含支付链路域可直接统计的码牌数。
@Data
@Accessors(chain = true)
@Schema(title = "门店概况聚合结果")
public class MchStoreOverviewResult {

    /// 门店配置的默认应用号(未配置为 null, 前端展示「跟随商户默认」)
    @Schema(description = "门店配置的默认应用号")
    private String storeAppId;

    /// 门店配置的默认应用名(未配置为 null)
    @Schema(description = "门店配置的默认应用名")
    private String storeAppName;

    /// 商户默认应用号(门店未配置时前端展示「跟随商户默认」)
    @Schema(description = "商户默认应用号")
    private String mchDefaultAppId;

    /// 商户默认应用名
    @Schema(description = "商户默认应用名")
    private String mchDefaultAppName;

    /// 绑定本店的码牌数(store_no 匹配, 逻辑删除自动过滤)
    @Schema(description = "绑定本店的码牌数")
    private Integer qrCodeCount;
}
