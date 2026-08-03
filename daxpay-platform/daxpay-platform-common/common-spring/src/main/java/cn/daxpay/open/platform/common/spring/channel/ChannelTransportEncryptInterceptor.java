package cn.daxpay.open.platform.common.spring.channel;

import cn.daxpay.open.platform.common.config.encrypt.ChannelAesGcmEncryptor;
import cn.daxpay.open.platform.core.exception.ChannelResultUnknownException;
import cn.daxpay.open.platform.core.exception.ChannelUnavailableException;
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
import org.springframework.web.client.RestClientException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.UnknownHostException;
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

    /// 通道子应用返回明文错误响应（子应用主动返回的未加密错误，如入站解密失败 400）
    public static final String MSG_PLAIN_ERROR_RESPONSE =
            "channel.error.transportEncrypt.plainErrorResponse";

    /// 通道结果未知 messageKey(网络超时/响应中断等, 请求是否到达通道无法确认)
    public static final String MSG_CHANNEL_RESULT_UNKNOWN = "pay.error.channelResultUnknown";

    /// 通道服务不可用 messageKey(连接被拒绝/主机不可达, 请求未到达通道子应用)
    public static final String MSG_CHANNEL_UNAVAILABLE = "pay.error.channelUnavailable";

    private static final MediaType TEXT_PLAIN_UTF8 =
            new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8);

    /// 明文错误 body 片段最大长度，防止异常日志爆炸
    private static final int SNIPPET_MAX_LENGTH = 200;

    /// 解析子应用明文错误响应的独立 ObjectMapper（拦截器为手动 new，非 Spring Bean，无法注入）
    private static final ObjectMapper PLAIN_ERROR_MAPPER = JsonMapper.builder().build();

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

        try {
            ClientHttpResponse response = execution.execute(request, requestBody);
            return decryptResponse(response);
        } catch (ChannelResultUnknownException e) {
            // 协议违规/明文错误已由 decryptResponse 归入结果未知, 直接上抛
            throw e;
        } catch (IOException | RestClientException e) {
            if (isConnectionRefused(e)) {
                // 连接未建立(子应用宕机/不可达): 请求未到达通道, 结果确定(未受理), 抛硬错误
                // 订单将被置 FAIL(资金确定未动), 不再归入结果未知触发不必要的同步查单
                log.error("通道子应用不可用, 连接未建立: {}", e.getMessage());
                throw new ChannelUnavailableException(MSG_CHANNEL_UNAVAILABLE, e);
            }
            // 网络超时/响应读取中断: 请求可能已到达通道(可能已扣款), 归入结果未知,
            // 由主流程保持 PROCESSING/PROGRESS 并交由定时同步查通道真实状态纠正
            throw new ChannelResultUnknownException(MSG_CHANNEL_RESULT_UNKNOWN, e);
        }
    }

    /// 判断异常链是否为连接未建立类异常(连接被拒绝/主机不可达/DNS 解析失败)
    ///
    /// 这类异常表示请求根本未发出, 通道结果确定(未受理), 应抛硬错误([ChannelUnavailableException]);
    /// 读超时([java.net.SocketTimeoutException])不在其列——读超时意味着请求已发出, 资金后果未知。
    private static boolean isConnectionRefused(Throwable e) {
        Throwable cur = e;
        while (cur != null) {
            if (cur instanceof ConnectException
                    || cur instanceof UnknownHostException
                    || cur instanceof NoRouteToHostException) {
                return true;
            }
            cur = cur.getCause();
        }
        return false;
    }

    /// 解密响应体；无 body 原样返回；有加密头则解密；无加密头按状态码区分协议违规与明文错误响应
    private ClientHttpResponse decryptResponse(ClientHttpResponse response) throws IOException {
        byte[] responseBytes = StreamUtils.copyToByteArray(response.getBody());
        if (responseBytes.length == 0) {
            return new DecryptedClientHttpResponse(response, responseBytes, false);
        }

        String encryptedFlag = response.getHeaders().getFirst(HEADER_X_DAX_PAYLOAD_ENCRYPTED);
        if ("true".equalsIgnoreCase(encryptedFlag)) {
            // 正常加密响应：解密后交给业务层
            String plaintext = encryptor.decrypt(new String(responseBytes, StandardCharsets.UTF_8));
            byte[] plainBytes = plaintext.getBytes(StandardCharsets.UTF_8);
            return new DecryptedClientHttpResponse(response, plainBytes, true);
        }

        // 无加密头：子应用未按加密协议响应。区分「成功响应未加密」与「明文错误响应」
        // 状态码须在 close 前获取，避免部分实现 close 后状态丢失
        HttpStatusCode statusCode = response.getStatusCode();
        response.close();
        if (statusCode.is2xxSuccessful()) {
            // 2xx 成功响应却未加密：真正的协议违规(子应用未加密或被劫持), 无法确认通道真实结果, 归入结果未知
            throw new ChannelResultUnknownException(MSG_RESPONSE_HEADER_MISSING,
                    new RuntimeException(HEADER_X_DAX_PAYLOAD_ENCRYPTED));
        }
        // 非 2xx 明文错误响应：子应用入站解密失败/加密失败等主动返回的明文错误（如 400 解密失败）
        // 通道真实结果未知, 归入结果未知交由同步纠正; 透传子应用返回的真实错误详情便于排查
        String detail = extractPlainErrorDetail(responseBytes);
        throw new ChannelResultUnknownException(MSG_PLAIN_ERROR_RESPONSE,
                new RuntimeException("status=" + statusCode.value() + ", detail=" + detail));
    }

    /// 从子应用明文错误响应体提取错误详情
    /// 优先解析 JSON 的 msg 字段（子应用返回 DaxResult 结构 {"code":400,"msg":"..."}），解析失败回退原始 body 片段
    private static String extractPlainErrorDetail(byte[] responseBytes) {
        String body = new String(responseBytes, StandardCharsets.UTF_8);
        try {
            JsonNode node = PLAIN_ERROR_MAPPER.readTree(body);
            String msg = node.path("msg").asText("");
            if (!msg.isEmpty()) {
                return msg;
            }
        } catch (Exception ignored) {
            // 解析失败回退到原始 body 片段
        }
        // 截断防止异常日志爆炸
        return body.length() > SNIPPET_MAX_LENGTH
                ? body.substring(0, SNIPPET_MAX_LENGTH) + "..."
                : body;
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
