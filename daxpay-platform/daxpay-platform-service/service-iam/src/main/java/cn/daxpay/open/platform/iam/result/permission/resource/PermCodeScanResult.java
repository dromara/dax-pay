package cn.daxpay.open.platform.iam.result.permission.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/// # 权限码扫描结果
///
@Data
@Accessors(chain = true)
@Schema(title = "权限码扫描结果")
public class PermCodeScanResult {

    @Schema(description = "新增数量")
    private int addedCount;

    @Schema(description = "更新数量")
    private int updatedCount;

    @Schema(description = "跳过数量")
    private int skippedCount;

    @Schema(description = "删除数量")
    private int deletedCount;

    @Schema(description = "异常数量")
    private int errorCount;

    @Schema(description = "新增权限码")
    private List<String> addedCodes = new ArrayList<>();

    @Schema(description = "更新权限码")
    private List<String> updatedCodes = new ArrayList<>();

    @Schema(description = "跳过权限码")
    private List<String> skippedCodes = new ArrayList<>();

    @Schema(description = "删除权限码")
    private List<String> deletedCodes = new ArrayList<>();

    @Schema(description = "异常信息")
    private List<String> errors = new ArrayList<>();
}
