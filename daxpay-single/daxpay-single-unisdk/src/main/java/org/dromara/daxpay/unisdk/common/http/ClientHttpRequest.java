package org.dromara.daxpay.unisdk.common.http;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.dromara.daxpay.unisdk.common.bean.MethodType;
import org.dromara.daxpay.unisdk.common.bean.result.PayException;
import org.dromara.daxpay.unisdk.common.exception.PayErrorException;
import org.dromara.daxpay.unisdk.common.util.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;

import static org.dromara.daxpay.unisdk.common.http.UriVariables.getMapToParameters;

/**
 * 一个HTTP请求的客户端
 *
 * @author egan
 * <pre>
 * email egzosn@gmail.com
 * date 2017/3/4 17:56
 *  </pre>
 */
@Slf4j
public class ClientHttpRequest<T> extends HttpEntityEnclosingRequestBase implements org.apache.http.client.ResponseHandler<T> {
    protected static final Logger LOG = LoggerFactory.getLogger(ClientHttpRequest.class);
    public static final ContentType APPLICATION_FORM_URLENCODED_UTF_8 = ContentType.create("application/x-www-form-urlencoded", Consts.UTF_8);
    public static final ContentType APPLICATION_XML_UTF_8 = ContentType.create("application/xml", Consts.UTF_8);


    /**
     * http请求方式 get pos
     * -- SETTER --
     *  设置请求方式
     *
     * @param method 请求方式
     *               {@link MethodType} 请求方式

     */
    @Setter
    private MethodType method;
    /**
     * 默认使用的响应编码
     */
    @Setter
    private Charset defaultCharset;
    /**
     * 响应类型
     */
    private Class<T> responseType;


    public ClientHttpRequest<T> setResponseType(Class<T> responseType) {
        this.responseType = responseType;
        return this;
    }

    /**
     * 空构造
     */
    public ClientHttpRequest() {
    }

    /**
     * 根据请求地址 请求方法，请求内容对象
     *
     * @param uri            请求地址
     * @param method         请求方法
     * @param request        请求内容
     * @param defaultCharset 默认使用的响应编码
     */
    public ClientHttpRequest(URI uri, MethodType method, Object request, String defaultCharset) {
        this(uri, method);
        setParameters(request);
        if (StrUtil.isNotEmpty(defaultCharset)) {
            setDefaultCharset(Charset.forName(defaultCharset));
        }

    }

    /**
     * 根据请求地址 请求方法，请求内容对象
     *
     * @param uri            请求地址
     * @param method         请求方法
     * @param request        请求内容
     * @param defaultCharset 默认使用的响应编码
     */
    public ClientHttpRequest(URI uri, MethodType method, Object request, Charset defaultCharset) {
        this(uri, method);
        setParameters(request);
        setDefaultCharset(defaultCharset);
    }

    /**
     * 根据请求地址 请求方法，请求内容对象
     *
     * @param uri     请求地址
     * @param method  请求方法
     * @param request 请求内容
     */
    public ClientHttpRequest(URI uri, MethodType method, Object request) {
        this(uri, method);
        setParameters(request);
    }

    /**
     * 根据请求地址 请求方法
     *
     * @param uri    请求地址
     * @param method 请求方法
     */
    public ClientHttpRequest(URI uri, MethodType method) {
        this.setURI(uri);
        this.method = method;
    }

    /**
     * 根据请求地址
     *
     * @param uri 请求地址
     */
    public ClientHttpRequest(URI uri) {
        this.setURI(uri);
    }

    /**
     * 根据请求地址
     *
     * @param uri 请求地址
     */
    public ClientHttpRequest(String uri) {
        this.setURI(URI.create(uri));
    }

    /**
     * 根据请求地址 请求方法
     *
     * @param uri    请求地址
     * @param method 请求方法
     */
    public ClientHttpRequest(String uri, MethodType method) {
        this.setURI(URI.create(uri));
        this.method = method;
    }

    /**
     * 根据请求地址 请求方法，请求内容对象
     *
     * @param uri     请求地址
     * @param method  请求方法
     * @param request 请求内容
     */
    public ClientHttpRequest(String uri, MethodType method, Object request) {
        this(uri, method);
        setParameters(request);
    }

    /**
     * 获取请求方式
     *
     * @return 请求方式
     */
    @Override
    public String getMethod() {
        return method.name();
    }

    public Charset getDefaultCharset() {
        if (null == defaultCharset) {
            defaultCharset = Consts.UTF_8;
        }
        return defaultCharset;
    }

    /**
     * 设置代理
     *
     * @param httpProxy http代理配置信息
     * @return 当前HTTP请求的客户端
     */
    public ClientHttpRequest setProxy(HttpHost httpProxy) {
        if (httpProxy != null) {
            RequestConfig config = RequestConfig.custom().setProxy(httpProxy).build();
            setConfig(config);
        }
        return this;
    }


    /**
     * 设置请求参数
     *
     * @param request 请求参数
     * @return 当前HTTP请求的客户端
     */
    public ClientHttpRequest setParameters(Object request) {
        switch (request) {
            case null -> {
                return this;
            }
            case HttpHeader entity -> {
                if (null != entity.getHeaders()) {
                    if (log.isDebugEnabled()) {
                        log.debug("header : {}", JSON.toJSONString(entity.getHeaders()));
                    }
                    for (Header header : entity.getHeaders()) {
                        addHeader(header);
                    }
                }
            }
            case HttpStringEntity entity -> {
                if (!entity.isEmpty()) {
                    setEntity(entity);
                }
                if (null != entity.getHeaders()) {
                    if (log.isDebugEnabled()) {
                        log.debug("header : {}", JSON.toJSONString(entity.getHeaders()));
                    }
                    for (Header header : entity.getHeaders()) {
                        addHeader(header);
                    }
                }
            }
            case HttpEntity httpEntity -> setEntity(httpEntity);
            case Map map -> {
                String parameters = getMapToParameters(map);
                if (log.isDebugEnabled()) {
                    log.debug("Parameter : {}", parameters);
                }
                StringEntity entity = new StringEntity(parameters, APPLICATION_FORM_URLENCODED_UTF_8);
                setEntity(entity);
            }
            case String s -> {
                if (log.isDebugEnabled()) {
                    log.debug("Parameter : {}", request);
                }
                StringEntity entity = new StringEntity(s, APPLICATION_FORM_URLENCODED_UTF_8);
                setEntity(entity);
            }
            default -> {
                String body = JSON.toJSONString(request);
                if (log.isDebugEnabled()) {
                    log.debug("body : {}", request);
                }
                StringEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);
                setEntity(entity);
            }
        }

        return this;

    }


    @Override
    public T handleResponse(HttpResponse response) throws IOException {
        final StatusLine statusLine = response.getStatusLine();
        final HttpEntity entity = response.getEntity();

        if (null == entity){
            return null;
        }
        String[] value;
        if (null == entity.getContentType()) {
            value = new String[]{"application/x-www-form-urlencoded"};
        } else {
            value = entity.getContentType().getValue().split(";");
        }
        //这里进行特殊处理，如果状态码非正常状态，但内容类型匹配至对应的结果也进行对应的响应类型转换
        if (statusLine.getStatusCode() >= 300 && statusLine.getStatusCode() != 304) {
            if (isJson(value[0], "") || isXml(value[0], "")) {
                return toBean(entity, value);
            }

            EntityUtils.consume(entity);
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }
        if (null == responseType) {
            responseType = (Class<T>) String.class;
        }


        return toBean(entity, value);

    }

    /**
     * 对请求进行转化至对应的可转化类型
     *
     * @param entity      响应实体
     * @param contentType 内容类型编码数组，第一个值为内容类型，第二个值为编码类型
     * @return 对应的响应对象
     * @throws IOException 响应类型文本转换时抛出异常
     */
    @SneakyThrows
    private T toBean(HttpEntity entity, String[] contentType) throws IOException {


        //是否为 输入流
        if (InputStream.class.isAssignableFrom(responseType)) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            entity.writeTo(os);
            return (T) new ByteArrayInputStream(os.toByteArray());
        }
        //是否为 字节数数组
        if (byte[].class.isAssignableFrom(responseType)) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            entity.writeTo(os);
            return (T) os.toByteArray();
        }
        //输出流
        if (OutputStream.class.isAssignableFrom(responseType)) {
            try {
                OutputStream t;
                if (responseType == OutputStream.class){
                    t= new ByteArrayOutputStream();
                }else {
                    t = (OutputStream) responseType.getDeclaredConstructor().newInstance();
                }
                entity.writeTo( t);
                return (T) t;
            } catch (InstantiationException e) {
                throw new PayErrorException(new PayException("InstantiationException", e.getMessage()));
            } catch (IllegalAccessException e) {
                throw new PayErrorException(new PayException("IllegalAccessException", e.getMessage()));
            }
        }

        //判断内容类型是否为文本类型
        if (isText(contentType[0])) {
/*            String charset = "UTF-8";
            if (null != contentType && 2 == charset.length()) {
                charset = contentType[1].substring(contentType[1].indexOf("=") + 1);
            }*/

            //获取响应的文本内容
            String result = EntityUtils.toString(entity, getDefaultCharset());
            if (log.isDebugEnabled()) {
                log.debug("请求响应内容：\r\n{}", result);
            }
            if (responseType.isAssignableFrom(String.class)) {
                return (T) result;
            }

            String first = result.substring(0, 1);
            //json类型
            if (isJson(contentType[0], first)) {
                try {
                    if (responseType.isAssignableFrom(JSONObject.class)) {
                        return (T)JSON.parseObject(result);
                    }
                    return JSON.parseObject(result, responseType);
                } catch (JSONException e) {
                    throw new PayErrorException(new PayException("failure", String.format("类型转化异常,contentType: %s\n%s", entity.getContentType().getValue(), e.getMessage()), result));
                }
            }
            //xml类型
            if (isXml(contentType[0], first)) {
                try {
                    if (responseType.isAssignableFrom(JSONObject.class)) {
                        return (T) XML.toJSONObject(result, getDefaultCharset());
                    }
                    return XML.toJSONObject(result, getDefaultCharset()).toJavaObject(responseType);
                } catch (Exception e) {
                    ;
                }
            }
            throw new PayErrorException(new PayException("failure", "类型转化异常,contentType:" + entity.getContentType().getValue(), result));
        }

        throw new PayErrorException(new PayException("failure", "类型转化异常,contentType:" + entity.getContentType().getValue()));
    }

    /**
     * 检测响应类型是否为json
     *
     * @param contentType 内容类型
     * @param textFirst   文本第一个字符
     * @return 布尔型， true为json内容类型
     */
    private boolean isJson(String contentType, String textFirst) {
        return (ContentType.APPLICATION_JSON.getMimeType().equals(contentType) || "{[".contains(textFirst));
    }

    /**
     * 检测响应类型是否为文本类型
     *
     * @param contentType 内容类型
     * @return 布尔型， true为文本内容类型
     */
    private boolean isText(String contentType) {
        return contentType.contains("xml") || contentType.contains("json") || contentType.contains("text") || contentType.contains("form-data") || contentType.contains("x-www-form-urlencoded");
    }

    /**
     * 检测响应类型是否为xml
     *
     * @param contentType 内容类型
     * @param textFirst   文本第一个字符
     * @return 布尔型， true为xml内容类型
     */
    private boolean isXml(String contentType, String textFirst) {
        return (ContentType.APPLICATION_XML.getMimeType().equals(contentType) || "<".contains(textFirst));
    }


}
