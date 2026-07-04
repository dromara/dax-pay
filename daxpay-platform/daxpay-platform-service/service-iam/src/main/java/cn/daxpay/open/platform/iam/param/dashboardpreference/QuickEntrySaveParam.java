package cn.daxpay.open.platform.iam.param.dashboardpreference;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@Schema(title = "工作台快捷入口保存参数")
public class QuickEntrySaveParam {

    /// 已选快捷入口有序序列(纯 key 数组), 允许空数组表示全部隐藏
    @Schema(description = "已选快捷入口有序序列(纯 key 数组), 允许空数组")
    private List<String> entries;

}
