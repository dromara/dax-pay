/*
 * Copyright 2017-2023 the original  Egan.
 * email egzosn@gmail.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.dromara.daxpay.unisdk.common.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * 通知参数
 *
 * @author Egan
 * email egzosn@gmail.com
 * date 2021/8/8
 */
@Setter
@Getter
public class NoticeParams implements Attrs {

    /**
     * body原始字符串
     */
    private String bodyStr;

    /**
     * 为了获取request里面传过来的动态参数
     */
    private Map<String, ?> body;

    /**
     * 存放请求头信息
     */
    private Map<String, List<String>> headers;

    /**
     * 附加属性
     */
    private Map<String, ?> attr;

    public NoticeParams() {
    }

    public NoticeParams(Map<String, ?> body) {
        this.body = body;
    }

    public NoticeParams(Map<String, ?> body, Map<String, List<String>> headers) {
        this.body = body;
        this.headers = headers;
    }

    private <T> T getValueMatchingKey(Map<String, T> values, String key) {
        T value = values.get(key);
        if (null != value) {
            return value;
        }

        for (Map.Entry<String, T> entry : values.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(key)) {
                return entry.getValue();
            }
        }
        return null;
    }


    public String getHeader(String name) {
        List<String> value = getValueMatchingKey(headers, name);
        return (null == value || value.isEmpty()) ? null : value.getFirst();
    }

    public Enumeration<String> getHeaders(String name) {
        List<String> value = getValueMatchingKey(headers, name);
        return (Collections.enumeration(value != null ? value : Collections.<String>emptySet()));
    }

    public Enumeration<String> getHeaderNames() {
        if (null == headers) {
            return Collections.enumeration(Collections.emptySet());
        }
        return Collections.enumeration(this.headers.keySet());
    }


    /**
     * 获取属性 这里可用做覆盖已设置的信息属性，订单信息在签名前进行覆盖。
     *
     * @return 属性
     */
    @Override
    public Map<String, ?> getAttrs() {
        return attr;
    }
}
