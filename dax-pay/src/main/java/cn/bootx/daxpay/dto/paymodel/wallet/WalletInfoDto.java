package cn.bootx.daxpay.dto.paymodel.wallet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 钱包综合信息
 *
 * @author xxm
 * @date 2022/3/13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "钱包综合信息")
public class WalletInfoDto extends WalletDto {

    @Schema(description = "钱包关联的账号名称")
    private String userName;

}
