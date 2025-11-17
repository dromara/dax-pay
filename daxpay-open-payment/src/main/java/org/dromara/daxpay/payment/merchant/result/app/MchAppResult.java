package org.dromara.daxpay.payment.merchant.result.app;

import cn.bootx.platform.core.result.BaseResult;
import org.dromara.daxpay.payment.merchant.entity.info.Merchant;
import org.dromara.daxpay.payment.merchant.enums.MchAppStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.dromara.core.trans.anno.Trans;
import org.dromara.core.trans.constant.TransType;
import org.dromara.core.trans.vo.TransPojo;

/**
 * 商户应用
 * @author xxm
 * @since 2024/6/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@Schema(title = "商户应用")
public class MchAppResult extends BaseResult implements TransPojo {

    @Schema(description = "商户号")
    @Trans(type = TransType.SIMPLE, target = Merchant.class, fields = Merchant.Fields.mchName, ref = MchAppResult.Fields.mchName, uniqueField=Merchant.Fields.mchNo)
    private String mchNo;

    @Schema(description = "商户名称")
    private String mchName;

    /** 应用号 */
    @Schema(description = "应用号")
    private String appId;

    /** 应用名称 */
    @Schema(description = "应用名称")
    private String appName;

    /**
     * 状态
     * @see MchAppStatusEnum
     */
    @Schema(description = "状态")
    private String status;

    /**
     * 默认应用
     */
    @Schema(description = "默认应用")
    private boolean defaultApp;

    /**
     * 通知地址, http/WebSocket 需要配置
     */
    @Schema(description = "通知地址")
    private String notifyUrl;

}
