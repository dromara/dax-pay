package cn.daxpay.open.payment.admin.result.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户工作台基础计数(运营端查看指定商户)
///
/// 供商户工作台「基础管理/支付配置/接入配置」磁贴数字角标使用,
/// 计数口径一律存量总数: 只过滤逻辑删除, 不按启用/状态过滤;
/// 用户为商户与用户的关联关系表(无逻辑删除字段), 行数即用户数, 含停用账号。
/// 字段刻意用 Integer 而非 Long, 规避全局 Long→字符串序列化契约(同设备计数口径)。
@Data
@Accessors(chain = true)
@Schema(title = "商户工作台基础计数")
public class AdminMchBaseCountResult {

    /// 商户用户数(关系存量, 含停用账号)
    @Schema(description = "商户用户数(含停用)")
    private Integer merchantUserCount;

    /// 名下门店数
    @Schema(description = "名下门店数")
    private Integer storeCount;

    /// 名下应用数
    @Schema(description = "名下应用数")
    private Integer appCount;

    /// 名下通道商户数
    @Schema(description = "名下通道商户数")
    private Integer channelMerchantCount;

    /// 名下域名校验文件数(平台级校验文件不计入)
    @Schema(description = "名下域名校验文件数")
    private Integer wxVerifyCount;
}
