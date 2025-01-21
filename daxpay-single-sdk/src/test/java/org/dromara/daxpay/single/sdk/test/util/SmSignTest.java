package org.dromara.daxpay.single.sdk.test.util;

import cn.hutool.crypto.SmUtil;
import org.junit.Test;

/**
 * 国密签名测试
 * @author xxm
 * @since 2024/6/29
 */
public class SmSignTest {

    @Test
    public void sign2(){
        String digestHex = SmUtil.sm3("aaaaa");
        System.out.println(digestHex);
    }
}
