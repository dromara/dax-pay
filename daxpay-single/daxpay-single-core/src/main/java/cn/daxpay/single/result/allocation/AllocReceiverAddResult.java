package cn.daxpay.single.result.allocation;

import cn.daxpay.single.result.pay.PayCloseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分账接收方添加返回结果
 * @author xxm
 * @since 2024/5/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "分账接收方返回结果")
public class AllocReceiverAddResult extends PayCloseResult {
}
