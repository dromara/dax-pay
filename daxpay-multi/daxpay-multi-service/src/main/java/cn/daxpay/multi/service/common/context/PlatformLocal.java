package cn.daxpay.multi.service.common.context;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 平台配置
 * @author xxm
 * @since 2023/12/24
 */
@Data
@Accessors(chain = true)
public class PlatformLocal {

    /** 网站地址 */
    private String websiteUrl;

}
