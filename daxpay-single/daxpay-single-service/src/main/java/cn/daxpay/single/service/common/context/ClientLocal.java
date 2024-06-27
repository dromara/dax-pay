package cn.daxpay.single.service.common.context;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付请求相关信息
 * @author xxm
 * @since 2023/12/25
 */
@Data
@Accessors(chain = true)
public class ClientLocal {

    /** 客户端ip */
    private String clientIp;

}
