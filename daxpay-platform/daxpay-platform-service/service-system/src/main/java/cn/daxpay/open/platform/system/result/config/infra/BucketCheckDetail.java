package cn.daxpay.open.platform.system.result.config.infra;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 存储桶连通性检查明细
///
/// 单个存储桶的分步检查结果, 公开桶与私有桶各自独立检查, 一桶失败不影响另一桶的明细呈现.
@Data
@Accessors(chain = true)
@Schema(title = "存储桶连通性检查明细")
public class BucketCheckDetail {

    /// 桶名称
    @Schema(description = "桶名称")
    private String bucketName;

    /// 该桶读写闭环是否通过(公开访问地址不可达属警告, 不影响本结论)
    @Schema(description = "该桶读写闭环是否通过")
    private boolean success;

    /// 失败概述(已国际化, 通过时为空)
    @Schema(description = "失败概述")
    private String message;

    /// 分步检查结果
    @Schema(description = "分步检查结果")
    private List<CheckStep> steps;
}
