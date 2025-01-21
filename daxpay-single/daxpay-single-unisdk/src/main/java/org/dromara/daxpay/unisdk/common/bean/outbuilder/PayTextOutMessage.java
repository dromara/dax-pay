package org.dromara.daxpay.unisdk.common.bean.outbuilder;

import org.dromara.daxpay.unisdk.common.bean.PayOutMessage;

/**
 * @author egan
 *  <pre>
 *      email egzosn@gmail.com
 *      date 2016-6-1 11:40:30
 *  </pre>
 */
public class PayTextOutMessage extends PayOutMessage{

    public PayTextOutMessage() {
    }

    @Override
    public String toMessage() {
        return getContent();
    }
}
