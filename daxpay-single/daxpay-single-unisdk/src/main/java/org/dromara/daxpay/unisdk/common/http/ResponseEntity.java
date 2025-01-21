package org.dromara.daxpay.unisdk.common.http;

import lombok.Getter;
import org.apache.http.Header;

/**
 * 响应实体
 * @author Egan
 * email egzosn@gmail.com
 * date 2021/8/1
 */
@Getter
public class ResponseEntity<T> {
    private final int statusCode;
    private final Header[] headers;
    private final T body;

    public ResponseEntity(int statusCode, Header[] headers, T body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

}
