package org.dromara.daxpay.single.sdk.param;


import org.dromara.daxpay.single.sdk.util.JsonUtil;

/**
 * 通道支付参数标识接口
 * @author xxm
 * @since 2023/12/17
 */
public interface ChannelParam {

    /**
     * 转换成字符串
     */
    default String toJson(){
        return JsonUtil.toJsonStr(this);
    }
}
