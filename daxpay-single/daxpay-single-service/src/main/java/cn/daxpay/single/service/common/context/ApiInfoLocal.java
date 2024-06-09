package cn.daxpay.single.service.common.context;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付接口信息
 * @author xxm
 * @since 2023/12/24
 */
@Data
@Accessors(chain = true)
public class ApiInfoLocal {

    /** 当前支付接口编码 */
    private String apiCode;

    /** 回调地址 */
    private String noticeUrl;

}
