package org.dromara.daxpay.payment.old.pay.param;

import org.dromara.daxpay.platform.core.enums.client.ClientEnum;
import org.dromara.daxpay.platform.common.mybatisplus.query.entity.SortParam;
import org.dromara.daxpay.platform.core.annotation.QueryParam;
import org.dromara.daxpay.platform.iam.service.client.ClientCodeService;
import cn.hutool.extra.spring.SpringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Objects;

/// # 商户查询参数
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "商户查询参数")
public class MchQuery extends SortParam {

    /// 商户号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "商户号")
    private String mchNo;

    /// 应用号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "应用号")
    private String appId;

    public String getMchNo() {
        // 判断是否管理端, 如果不是管理端将值设置为null, 防止越权
        var clientCodeService = SpringUtil.getBean(ClientCodeService.class);
        if (!Objects.equals(clientCodeService.getClientCode(), ClientEnum.ADMIN.getCode())){
            return null;
        }
        return mchNo;
    }
}
