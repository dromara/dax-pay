package org.dromara.daxpay.unisdk.common.util.str;

import cn.hutool.core.util.StrUtil;

import java.io.UnsupportedEncodingException;

/**
 * Created by ZaoSheng on 2016/6/4.
 */
public class StringUtils {

    /**
     * @param content 需要加密串
     * @param charset 字符集
     * @return 加密后的字节数组
     */
    public static byte[] getContentBytes(String content, String charset) {
        if (StrUtil.isEmpty(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException("转码过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

}
