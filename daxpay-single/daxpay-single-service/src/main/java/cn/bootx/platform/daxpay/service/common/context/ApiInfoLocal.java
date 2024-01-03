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

    /** 是否开启回调通知 */
    private boolean notice;

    /** 请求参数是否签名 */
    private boolean reqSign;

    /** 响应参数是否签名 */
    private boolean resSign;

    /** 回调信息是否签名 */
    private boolean noticeSign;

    /** 是否记录请求的信息 */
    private boolean record;
}
