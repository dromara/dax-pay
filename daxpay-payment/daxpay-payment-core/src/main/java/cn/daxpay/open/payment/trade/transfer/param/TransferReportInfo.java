package cn.daxpay.open.payment.trade.transfer.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 转账场景报备信息项
///
/// 通用结构(微信转账场景报备用), [infoType] 为微信协议固定的信息类型(如"活动名称"),
/// [infoContent] 为商户填写的信息内容(如"新会员有礼")。
@Data
@Accessors(chain = true)
@Schema(title = "转账场景报备信息项")
public class TransferReportInfo {

    @Schema(description = "信息类型(微信协议固定中文, 如: 活动名称)")
    private String infoType;

    @Schema(description = "信息内容(商户自定义填写)")
    private String infoContent;
}
