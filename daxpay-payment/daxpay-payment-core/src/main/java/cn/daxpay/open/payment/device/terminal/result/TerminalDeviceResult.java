package cn.daxpay.open.payment.device.terminal.result;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
import cn.daxpay.open.platform.core.annotation.Trans;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.util.List;

/// # 商户终端
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@Schema(title = "商户终端")
public class TerminalDeviceResult extends MchBaseResult {

    @Schema(description = "商户终端编码")
    private String terminalNo;

    @Schema(description = "终端名称")
    private String name;

    @Schema(description = "绑定门店号")
    private String storeNo;

    /// 门店名称
    @Trans(
            entity = MchStoreInfo.class,
            source = Fields.storeNo,
            result = MchStoreInfo.Fields.storeName)
    @Schema(description = "门店名称")
    private String storeName;

    @Schema(description = "是否启用")
    private Boolean enable;

    @Schema(description = "是否门店默认终端(同门店至多一个)")
    private Boolean defaultTerminal;

    @Schema(description = "备注")
    private String remark;

    /// 关联的通道终端(只读聚合, 总览/商户端分页时批量填充, 不落库)
    @Schema(description = "关联的通道终端")
    private List<ChannelTerminalResult> channelTerminals;
}
