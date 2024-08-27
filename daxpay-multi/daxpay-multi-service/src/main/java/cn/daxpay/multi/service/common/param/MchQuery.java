package cn.daxpay.multi.service.common.param;

import cn.bootx.platform.common.config.BootxConfigProperties;
import cn.bootx.platform.common.mybatisplus.query.entity.SortParam;
import cn.daxpay.multi.service.code.DaxPayCode;
import cn.hutool.extra.spring.SpringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
    @Schema(description = "商户号")
    private String mchNo;

    /** 应用号 */
    @Schema(description = "应用号")
    private String appId;

    public String getMchNo() {
        // 判断是否管理端, 如果不是将值进行过滤
        var bootxConfigProperties = SpringUtil.getBean(BootxConfigProperties.class);
        if (!bootxConfigProperties.getClientCode().equals(DaxPayCode.Client.ADMIN)){
            return null;
        }
        return mchNo;
    }
}
