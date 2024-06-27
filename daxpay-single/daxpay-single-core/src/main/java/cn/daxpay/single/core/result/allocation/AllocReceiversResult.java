package cn.daxpay.single.core.result.allocation;

import cn.daxpay.single.core.result.pay.PayCloseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 分账接收方返回结果
 * @author xxm
 * @since 2024/5/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "分账接收方返回结果")
public class AllocReceiversResult extends PayCloseResult {

    @Schema(description = "接收方列表")
    private List<AllocReceiverResult> receivers;

}
