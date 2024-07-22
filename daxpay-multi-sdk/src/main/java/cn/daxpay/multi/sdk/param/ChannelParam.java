package cn.daxpay.multi.sdk.param;

import cn.hutool.json.JSONUtil;

/**
 * 通道支付参数标识接口
 * @author xxm
 * @since 2023/12/17
 */
public interface ChannelParam extends SortMapParam {

    /**
     * 转换成字符串
     */
    default String toJson(){
        return JSONUtil.toJsonStr(this);
    }
}
