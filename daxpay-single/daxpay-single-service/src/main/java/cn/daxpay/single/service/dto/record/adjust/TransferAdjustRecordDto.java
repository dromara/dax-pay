package cn.daxpay.single.service.dto.record.adjust;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 退款调整记录
 * @author xxm
 * @since 2024/7/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "交易调整记录")
public class TransferAdjustRecordDto extends BaseDto {

}
