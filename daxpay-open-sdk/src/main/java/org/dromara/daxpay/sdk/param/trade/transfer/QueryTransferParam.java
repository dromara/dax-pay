package org.dromara.daxpay.sdk.param.trade.transfer;

import org.dromara.daxpay.sdk.net.DaxPayRequest;
import org.dromara.daxpay.sdk.response.DaxResult;
import org.dromara.daxpay.sdk.result.trade.transfer.TransferOrderResult;
import org.dromara.daxpay.sdk.util.JsonUtil;
import cn.hutool.core.lang.TypeReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 转账查询参数
 * @author xxm
 * @since 2024/6/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "转账查询参数")
public class QueryTransferParam extends DaxPayRequest<TransferOrderResult> {
    /** 商户转账号 */
    @Size(max = 100, message = "商户转账号不可超过100位")
    @Schema(description = "商户转账号")
    private String bizTransferNo;

    /** 转账号 */
    @Size(max = 32, message = "转账号不可超过100位")
    @Schema(description = "转账号")
    private String transferNo;

    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/query/transferOrder";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxResult<TransferOrderResult> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxResult<TransferOrderResult>>() {});
    }
}
