package cn.daxpay.open.payment.unipay.param.trade.transfer;

import cn.daxpay.open.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/// # 转账状态同步参数
///
/// 主动查询通道网关方转账终态并回写本地转账单。
/// 定位方式二选一: 平台转账单号(transferNo)单独可查; 商户转账号(bizTransferNo)须配转账通道(channel),
/// 与发起幂等维度(通道+商户转账号+商户号)保持一致, 组合完整性由服务层校验。
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "转账状态同步参数")
public class TransferSyncParam extends MerchantPaymentCommonParam {

    /// 平台转账单号
    @Schema(description = "平台转账单号")
    @Size(max = 100, message = "{validation.field.transferNo.size}")
    private String transferNo;

    /// 转账通道(与商户转账号配对使用)
    @Schema(description = "转账通道")
    @Size(max = 32, message = "{validation.field.channel.size}")
    private String channel;

    /// 商户转账号(与转账通道配对使用)
    @Schema(description = "商户转账号")
    @Size(max = 100, message = "{validation.field.bizTransferNo.size}")
    private String bizTransferNo;

}
