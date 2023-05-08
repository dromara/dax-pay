package cn.bootx.daxpay.dto.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author xxm
 * @date 2021/2/28
 */
@Data
@Accessors(chain = true)
@Schema(title = "异步支付线程信息")
public class AsyncPayInfo implements Serializable {

    private static final long serialVersionUID = 8239742916705144905L;

    /** 支付参数体(通常用于发起支付的参数) */
    private String payBody;

    /** 第三方支付平台订单号(付款码支付直接成功时会出现) */
    private String tradeNo;

    /** 是否记录超时时间,默认记录 */
    private boolean expiredTime = true;

}
