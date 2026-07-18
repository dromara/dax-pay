package cn.daxpay.open.payment.device.terminal.result;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
import cn.daxpay.open.platform.core.annotation.Trans;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 系统终端
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@Schema(title = "系统终端")
public class TerminalDeviceResult extends MchBaseResult {

    /// 商户名称
    @Trans(
            entity = MerchantInfo.class,
            source = MchBaseResult.Fields.mchNo,
            result = MerchantInfo.Fields.mchName)
    @Schema(description = "商户名称")
    private String mchName;

    @Schema(description = "系统终端编码")
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

    @Schema(description = "备注")
    private String remark;
}
