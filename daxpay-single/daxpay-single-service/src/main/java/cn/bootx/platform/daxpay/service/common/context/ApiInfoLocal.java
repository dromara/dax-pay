package cn.bootx.platform.daxpay.service.common.context;

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

    /** 是否开启通知 */
    private boolean notice;

    /** 回调地址 */
    private String noticeUrl;

    /** 请求参数是否签名 */
    private boolean reqSign;
}
