package org.dromara.daxpay.service.common.param;

import cn.bootx.platform.common.mybatisplus.query.entity.SortParam;
import cn.bootx.platform.core.annotation.QueryParam;
import cn.bootx.platform.iam.service.client.ClientCodeService;
import org.dromara.daxpay.service.code.DaxPayCode;
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

    /** 应用号 */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "应用号")
    private String appId;

}
