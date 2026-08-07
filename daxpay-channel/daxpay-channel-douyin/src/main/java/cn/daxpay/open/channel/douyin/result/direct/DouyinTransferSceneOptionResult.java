package cn.daxpay.open.channel.douyin.result.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 抖音转账场景选项结果
///
/// 供前端下拉选择与报备字段动态渲染。报备字段 [reportInfoTypes] 为抖音协议固定中文
/// [infoType], 不可更改; 顺序即发起转账时 [infoContent] 的填写下标。
///
/// [reportInfoDescriptions] 与 [reportInfoTypes] 平行, 描述每个字段含义和抖音文档示例。
/// [userRecvPerceptionOptions] 为收款人在抖音中看到的感知文案可选值。
@Data
@Accessors(chain = true)
@Schema(title = "抖音转账场景选项")
public class DouyinTransferSceneOptionResult {

    @Schema(description = "转账场景ID")
    private String code;

    @Schema(description = "场景名称")
    private String name;

    @Schema(description = "报备字段定义(抖音协议固定中文 infoType)")
    private List<String> reportInfoTypes;

    @Schema(description = "报备字段说明(与 reportInfoTypes 平行, 含抖音文档示例)")
    private List<String> reportInfoDescriptions;

    @Schema(description = "用户收款感知可选值(收款人在抖音中看到的文案)")
    private List<String> userRecvPerceptionOptions;
}
