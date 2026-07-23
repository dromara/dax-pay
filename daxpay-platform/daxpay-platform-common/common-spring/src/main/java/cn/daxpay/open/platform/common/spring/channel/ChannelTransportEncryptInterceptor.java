package cn.daxpay.open.platform.common.spring.channel;

import cn.daxpay.open.platform.common.config.encrypt.ChannelAesGcmEncryptor;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/// # 通道传输报文加解密拦截器
///
/// 主应用出站：非空请求 body 强制 AES-GCM 加密；响应强制解密后交给业务层。
/// 对业务 HttpExchange Client 透明。
///
/// Header 名与 WebHeaderCode.X_DAX_PAYLOAD_ENCRYPTED 保持一致，
/// 此处硬编码以避免 common-spring → platform-core 依赖。
@Slf4j
@RequiredArgsConstructor
public class ChannelTransportEncryptInterceptor implements ClientHttpRequestInterceptor {

    /// 通道传输报文已加密标记（与 WebHeaderCode.X_DAX_PAYLOAD_ENCRYPTED 一致）
    public static final String HEADER_X_DAX_PAYLOAD_ENCRYPTED = "X-Dax-Payload-Encrypted";

    /// 通道子应用响应未携带传输加密头
    public static final String MSG_RESPONSE_HEADER_MISSING =
            "channel.error.transportEncrypt.responseHeaderMissing";

    private static final MediaType TEXT_PLAIN_UTF8 =
            new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8);

    private final ChannelAesGcmEncryptor encryptor;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        byte[] requestBody = body;
        if (body.length > 0) {
            String ciphertext = encryptor.encrypt(new String(body, StandardCharsets.UTF_8));
            requestBody = ciphertext.getBytes(StandardCharsets.UTF_8);
            request.getHeaders().set(HEADER_X_DAX_PAYLOAD_ENCRYPTED, "true");
            request.getHeaders().setContentType(TEXT_PLAIN_UTF8);
            request.getHeaders().setContentLength(requestBody.length);
        }

        ClientHttpResponse response = execution.execute(request, requestBody);
        return decryptResponse(response);
    }

    /// 解密响应体；无 body 原样返回；有 body 但无加密头视为协议错误
    private ClientHttpResponse decryptResponse(ClientHttpResponse response) throws IOException {
        byte[] responseBytes = StreamUtils.copyToByteArray(response.getBody());
        if (responseBytes.length == 0) {
            return new DecryptedClientHttpResponse(response, responseBytes, false);
        }

        String encryptedFlag = response.getHeaders().getFirst(HEADER_X_DAX_PAYLOAD_ENCRYPTED);
        if (!"true".equalsIgnoreCase(encryptedFlag)) {
            response.close();
            // 通道子应用响应未携带传输加密头
            throw new BizInfoException(
                    CommonErrorCode.SYSTEM_ERROR, MSG_RESPONSE_HEADER_MISSING, HEADER_X_DAX_PAYLOAD_ENCRYPTED);
        }

        String plaintext = encryptor.decrypt(new String(responseBytes, StandardCharsets.UTF_8));
        byte[] plainBytes = plaintext.getBytes(StandardCharsets.UTF_8);
        return new DecryptedClientHttpResponse(response, plainBytes, true);
    }

    /// 包装已解密的响应，供 RestClient/HttpExchange 按 JSON 解析
    static final class DecryptedClientHttpResponse implements ClientHttpResponse {

        private final ClientHttpResponse delegate;
        private final byte[] body;
        private final HttpHeaders headers;

        DecryptedClientHttpResponse(ClientHttpResponse delegate, byte[] body, boolean decrypted) {
            this.delegate = delegate;
            this.body = body;
            this.headers = new HttpHeaders();
            this.headers.putAll(delegate.getHeaders());
            if (decrypted) {
                this.headers.setContentType(MediaType.APPLICATION_JSON);
                this.headers.setContentLength(body.length);
            }
        }

        @Override
        public HttpStatusCode getStatusCode() throws IOException {
            return delegate.getStatusCode();
        }

        @Override
        public String getStatusText() throws IOException {
            return delegate.getStatusText();
        }

        @Override
        public void close() {
            delegate.close();
        }

        @Override
        public InputStream getBody() {
            return new ByteArrayInputStream(body);
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }
    }
}
