package cn.daxpay.open.platform.common.spring.channel;

import cn.daxpay.open.platform.common.config.encrypt.ChannelAesGcmEncryptor;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/// # 通道传输报文加解密拦截器测试
///
/// 覆盖 [ChannelTransportEncryptInterceptor#decryptResponse] 的四种分支：
/// 1. 正常加密响应（2xx + 加密头）应解密为明文 JSON
/// 2. 成功响应未加密（2xx + 无加密头）应抛「响应未携带传输加密头」（协议违规）
/// 3. 子应用明文错误响应（非 2xx + 无加密头）应抛「明文错误响应」并透传真实错误详情
/// 4. 空 body 响应原样放行
class ChannelTransportEncryptInterceptorTest {

    private ChannelAesGcmEncryptor encryptor;
    private ChannelTransportEncryptInterceptor interceptor;

    @BeforeEach
    void initInterceptor() {
        encryptor = new ChannelAesGcmEncryptor(generateKey(32));
        interceptor = new ChannelTransportEncryptInterceptor(encryptor);
    }

    @Test
    @DisplayName("正常加密响应（200 + 加密头）应解密为明文 JSON")
    void shouldDecryptEncryptedResponse() throws IOException {
        // 模拟子应用返回的加密响应
        String plaintext = "{\"code\":0,\"data\":\"ok\"}";
        String ciphertext = encryptor.encrypt(plaintext);
        ClientHttpResponse response = mockResponse(200, ciphertext, true);

        ClientHttpResponse result = interceptor.intercept(mockRequest(), new byte[0], executionReturning(response));

        // 出站请求 body 为空时不会被加密；响应应被解密为明文 JSON
        byte[] body = result.getBody().readAllBytes();
        assertEquals(plaintext, new String(body, StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("成功响应未携带加密头应抛协议违规错误")
    void shouldThrowWhenSuccessResponseMissingEncryptedHeader() {
        // 2xx 成功响应却未加密，属于真正的协议违规
        ClientHttpResponse response = mockResponse(200, "{\"code\":0,\"data\":\"ok\"}", false);

        BizInfoException ex = assertThrows(BizInfoException.class,
                () -> interceptor.intercept(mockRequest(), new byte[0], executionReturning(response)));

        assertEquals(ChannelTransportEncryptInterceptor.MSG_RESPONSE_HEADER_MISSING, ex.getMessageKey());
    }

    @Test
    @DisplayName("子应用明文错误响应（400 解密失败）应透传真实错误详情")
    void shouldUnwrapPlainErrorResponseDetail() {
        // 模拟子应用入站解密失败返回的 400 明文 JSON
        String plainErrorBody = "{\"code\":400,\"msg\":\"通道传输解密失败\"}";
        ClientHttpResponse response = mockResponse(400, plainErrorBody, false);

        BizInfoException ex = assertThrows(BizInfoException.class,
                () -> interceptor.intercept(mockRequest(), new byte[0], executionReturning(response)));

        // 应抛「明文错误响应」而非「响应未携带加密头」，并携带状态码与真实错误 msg
        assertEquals(ChannelTransportEncryptInterceptor.MSG_PLAIN_ERROR_RESPONSE, ex.getMessageKey());
        Object[] args = ex.getArgs();
        assertEquals(400, args[0]);
        assertEquals("通道传输解密失败", args[1]);
    }

    @Test
    @DisplayName("明文错误 body 非 JSON 时应回退为原始片段")
    void shouldFallbackToRawSnippetWhenBodyNotJson() {
        // 子应用返回非 JSON 明文（如网关/代理介入）
        ClientHttpResponse response = mockResponse(502, "Bad Gateway", false);

        BizInfoException ex = assertThrows(BizInfoException.class,
                () -> interceptor.intercept(mockRequest(), new byte[0], executionReturning(response)));

        assertEquals(ChannelTransportEncryptInterceptor.MSG_PLAIN_ERROR_RESPONSE, ex.getMessageKey());
        assertEquals(502, ex.getArgs()[0]);
        // 回退为原始 body 片段
        assertEquals("Bad Gateway", ex.getArgs()[1]);
    }

    @Test
    @DisplayName("空 body 响应原样放行")
    void shouldPassThroughEmptyBody() throws IOException {
        ClientHttpResponse response = mockResponse(200, "", true);

        ClientHttpResponse result = interceptor.intercept(mockRequest(), new byte[0], executionReturning(response));

        // 空 body 不解密，原样返回空字节数组
        assertArrayEquals(new byte[0], result.getBody().readAllBytes());
    }

    @Test
    @DisplayName("出站非空请求 body 应被加密并携带加密头")
    void shouldEncryptNonEmptyRequestBody() throws IOException {
        String requestBody = "{\"channel\":\"alipay\"}";
        byte[] body = requestBody.getBytes(StandardCharsets.UTF_8);
        // 加密响应，确保入站不抛错
        String ciphertext = encryptor.encrypt("{\"code\":0}");
        ClientHttpResponse response = mockResponse(200, ciphertext, true);

        // 捕获实际发给 execution 的请求 body
        HttpRequest request = mockRequest();
        interceptor.intercept(request, body, executionReturning(response));

        // 请求应被加密并设置加密头
        assertEquals("true", request.getHeaders().getFirst(ChannelTransportEncryptInterceptor.HEADER_X_DAX_PAYLOAD_ENCRYPTED));
    }

    /// 构造返回固定响应的 execution
    private static ClientHttpRequestExecution executionReturning(ClientHttpResponse response) {
        return (request, body) -> response;
    }

    /// 构造最简 HttpRequest（带可写 headers）
    private static HttpRequest mockRequest() {
        HttpHeaders headers = new HttpHeaders();
        return new HttpRequest() {
            @Override
            public HttpMethod getMethod() {
                return HttpMethod.POST;
            }

            @Override
            public URI getURI() {
                return URI.create("http://localhost/channel/test");
            }

            @Override
            public HttpHeaders getHeaders() {
                return headers;
            }

            @Override
            public Map<String, Object> getAttributes() {
                return Map.of();
            }
        };
    }

    /// 构造 mock 响应
    private static ClientHttpResponse mockResponse(int status, String body, boolean withEncryptedHeader) {
        HttpHeaders headers = new HttpHeaders();
        if (withEncryptedHeader) {
            headers.add(ChannelTransportEncryptInterceptor.HEADER_X_DAX_PAYLOAD_ENCRYPTED, "true");
        }
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        return new ClientHttpResponse() {
            @Override
            public HttpStatusCode getStatusCode() {
                return HttpStatusCode.valueOf(status);
            }

            @Override
            public String getStatusText() {
                return "";
            }

            @Override
            public void close() {
                // no-op
            }

            @Override
            public InputStream getBody() {
                return new ByteArrayInputStream(bytes);
            }

            @Override
            public HttpHeaders getHeaders() {
                return headers;
            }
        };
    }

    /// 生成指定长度的随机密钥字符串
    private static String generateKey(int length) {
        byte[] keyBytes = new byte[length];
        new SecureRandom().nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes).substring(0, length);
    }
}
