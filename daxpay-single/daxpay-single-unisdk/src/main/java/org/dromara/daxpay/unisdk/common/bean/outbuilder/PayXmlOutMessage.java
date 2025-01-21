package org.dromara.daxpay.unisdk.common.bean.outbuilder;

import lombok.Getter;
import lombok.Setter;
import org.dromara.daxpay.unisdk.common.bean.PayOutMessage;

/**
 * @author egan
 * <pre>
 *     email egzosn@gmail.com
 *     date 2016-6-1 13:53:3
 *  </pre>
 */
@Setter
@Getter
public class PayXmlOutMessage extends PayOutMessage{

    private String code;

    public PayXmlOutMessage() {
    }

    @Override
    public String toMessage() {
       return "<xml><return_code><![CDATA[" + code + "]]></return_code><return_msg><![CDATA[" + content
                + "]]></return_msg></xml>";
    }
}
