package cn.daxpay.open.platform.iam.result.dashboardpreference;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@Schema(title = "工作台快捷入口偏好")
public class QuickEntryResult {

    /// 已选快捷入口有序序列(纯 key 数组); 为 null 表示用户尚未自定义, 前端使用默认序列
    @Schema(description = "已选快捷入口有序序列(纯 key 数组), null 表示未自定义")
    private List<String> entries;

}
