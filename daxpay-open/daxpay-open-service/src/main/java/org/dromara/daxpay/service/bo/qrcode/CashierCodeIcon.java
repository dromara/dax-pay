package org.dromara.daxpay.service.bo.qrcode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 收款码牌图标
 * @author xxm
 * @since 2025/7/1
 */
@Data
@Accessors(chain = true)
@Schema(title = "收款码牌图标")
public class CashierCodeIcon {

    /** icon列表 */
    @Schema(description = "icon列表")
    private List<Icon> icons = new ArrayList<>();

    /**
     * 码牌图标
     */
    @Data
    @Accessors(chain = true)
    public static class Icon {
        /**
         * 图标
         */
        @Schema(description = "图标")
        private String logo;
        /**
         * 类型
         */
        @Schema(description = "类型")
        private String type;
        /**
         * 名称
         */
        @Schema(description = "名称")
        private String name;
    }

}
