package org.dromara.daxpay.service.pay.common.param;

import cn.bootx.platform.common.mybatisplus.query.entity.SortParam;
import cn.bootx.platform.core.annotation.QueryParam;
import cn.bootx.platform.iam.service.client.ClientCodeService;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import cn.hutool.extra.spring.SpringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * 商户查询参数
 * @author xxm
 * @since 2024/8/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "商户查询参数")
public class MchQuery extends SortParam {

    /** 商户号 */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "商户号")
    private String mchNo;

    /** 应用号 */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "应用号")
    private String appId;

    public String getMchNo() {
        // 判断是否管理端, 如果不是管理端将值设置为null, 防止越权
        var clientCodeService = SpringUtil.getBean(ClientCodeService.class);
        if (!Objects.equals(clientCodeService.getClientCode(), DaxPayCode.Client.ADMIN)){
            return null;
        }
        return mchNo;
    }
}
