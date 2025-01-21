package org.dromara.daxpay.single.sdk.param.channel;

import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.daxpay.single.sdk.param.ChannelParam;

/**
 * 云闪付参数
 * @author xxm
 * @since 2024/3/13
 */
@Data
@Accessors(chain = true)
public class UnionPayParam implements ChannelParam {

    /** 授权码(主动扫描用户的付款码) */
    private String authCode;

}
