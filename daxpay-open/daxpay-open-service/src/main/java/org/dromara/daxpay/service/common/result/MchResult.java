package org.dromara.daxpay.service.common.result;

import cn.bootx.platform.core.result.BaseResult;
import org.dromara.daxpay.service.entity.merchant.MchApp;
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
    private String mchNo;

    @Schema(description = "商户名称")
    private String mchName;


    @Schema(description = "商户应用AppId")
    @Trans(type = TransType.SIMPLE, target = MchApp.class, fields = MchApp.Fields.appName, ref = Fields.appName, uniqueField=MchApp.Fields.appId)
    private String appId;

    @Schema(description = "商户应用名称")
    private String appName;

}
