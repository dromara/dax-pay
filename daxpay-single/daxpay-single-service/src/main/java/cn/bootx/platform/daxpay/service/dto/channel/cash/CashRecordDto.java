package cn.bootx.platform.daxpay.service.dto.channel.cash;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 现金记录
 * @author xxm
 * @since 2024/2/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "现金记录")
public class CashRecordDto extends BaseDto {
}
