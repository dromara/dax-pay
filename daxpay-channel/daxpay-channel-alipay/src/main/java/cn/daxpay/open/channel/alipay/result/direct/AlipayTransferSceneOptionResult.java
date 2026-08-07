package cn.daxpay.open.channel.alipay.result.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 支付宝转账场景选项结果
///
/// 供前端场景卡片渲染与报备字段动态展示。报备字段 [reportInfoTypes] 为支付宝协议固定中文
/// [infoType], 不可更改; 顺序即发起转账时 [infoContent] 的填写下标。
///
/// [reportInfoDescriptions] 与 [reportInfoTypes] 平行, 描述每个字段含义和支付宝文档示例。
/// 由枚举 [cn.daxpay.open.channel.alipay.enums.AlipayTransferSceneEnum] 投影, 不查库。
@Data
@Accessors(chain = true)
@Schema(title = "支付宝转账场景选项")
public class AlipayTransferSceneOptionResult {

    @Schema(description = "转账场景名称(支付宝协议固定中文取值)")
    private String sceneName;

    @Schema(description = "报备字段定义(支付宝协议固定中文 infoType)")
    private List<String> reportInfoTypes;

    @Schema(description = "报备字段说明(与 reportInfoTypes 平行, 含支付宝文档示例)")
    private List<String> reportInfoDescriptions;
}
