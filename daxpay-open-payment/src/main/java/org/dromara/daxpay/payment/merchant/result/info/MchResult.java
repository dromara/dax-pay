package org.dromara.daxpay.payment.merchant.result.info;

import cn.bootx.platform.core.result.BaseResult;
import org.dromara.daxpay.payment.isv.entity.info.IsvInfo;
import org.dromara.daxpay.payment.merchant.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.payment.merchant.entity.app.MchApp;
import org.dromara.daxpay.payment.merchant.entity.info.Merchant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.dromara.core.trans.anno.Trans;
import org.dromara.core.trans.constant.TransType;
import org.dromara.core.trans.vo.TransPojo;

/**
 * 商户应用基础返回结果
 * @author xxm
 * @since 2024/7/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@Schema(title = "商户应用基础返回结果")
public class MchResult extends BaseResult implements TransPojo {

    @Schema(description = "商户号")
    @Trans(type = TransType.SIMPLE, target = Merchant.class, fields = Merchant.Fields.mchName, ref = Fields.mchName, uniqueField=Merchant.Fields.mchNo)
    private String mchNo;

    @Schema(description = "商户名称")
    private String mchName;

    @Schema(description = "商户应用AppId")
    @Trans(type = TransType.SIMPLE, target = MchApp.class, fields = MchApp.Fields.appName, ref = Fields.appName, uniqueField= MchAppBaseEntity.Fields.appId)
    private String appId;

    @Schema(description = "商户应用名称")
    private String appName;

    @Schema(description = "服务商号")
    @Trans(type = TransType.SIMPLE, target = IsvInfo.class, fields = IsvInfo.Fields.name, ref = Fields.isvName, uniqueField=IsvInfo.Fields.isvNo)
    private String isvNo;

    @Schema(description = "服务商名称")
    private String isvName;

}
