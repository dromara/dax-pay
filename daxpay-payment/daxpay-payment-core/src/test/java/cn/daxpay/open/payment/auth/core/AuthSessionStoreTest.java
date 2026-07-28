package cn.daxpay.open.payment.auth.core;

import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthStatusEnum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/// # 认证会话与结果缓存测试
///
/// 锁定 [AuthSessionStore] 的 Redis 读写行为(key 前缀、TTL、空值兜底、queryCode 回退),
/// 供阶段 2.5(sealed AuthSession + 序列化格式变更)回归验证。含 Jackson 序列化往返用例。
///
/// ## strictness 说明
/// `@MockitoSettings(strictness = Strictness.LENIENT)`: 因 `redisTemplate.opsForValue()` 在 setUp 中
/// 预先 stub, 但部分用例(blank 入参早返回、两者皆空不写)不会触达 opsForValue, strict 模式会误报。
/// 改用 LENIENT 放宽未使用 stub 的检查, 具体调用交互仍由各用例 verify 精确断言。
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthSessionStoreTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    private AuthSessionStore authSessionStore;

    @BeforeAll
    static void initJackson() {
        // JacksonUtil.convert 依赖静态 objectMapper, 纯单元测试无 Spring 容器初始化, 需手动注入
        if (JacksonUtil.getObjectMapper() == null) {
            JacksonUtil.setObjectMapper(JsonMapper.builder().build());
        }
    }

    @BeforeEach
    void setUp() {
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        authSessionStore = new AuthSessionStore(redisTemplate);
    }

    // ==================== saveSession / loadSession ====================

    @Test
    @DisplayName("saveSession: 以 AUTH_SESSION_KEY_PREFIX 为前缀、AUTH_TIMEOUT 为 TTL 写入")
    void saveSession_shouldUsePrefixAndTimeout() {
        AuthSession session = new AuthSession().setSource(AuthSourceEnum.PLATFORM_ALIPAY.getCode());

        authSessionStore.saveSession("tok-1", session);

        verify(valueOperations).set(
                AuthSessionStore.AUTH_SESSION_KEY_PREFIX + "tok-1",
                session,
                AuthSessionStore.AUTH_TIMEOUT);
    }

    @Test
    @DisplayName("loadSession: authToken 为空直接返回 null, 不访问 Redis")
    void loadSession_blankToken_shouldReturnNullWithoutRedis() {
        assertNull(authSessionStore.loadSession(""));
        assertNull(authSessionStore.loadSession(null));
        verifyNoInteractions(redisTemplate);
    }

    @Test
    @DisplayName("loadSession: Redis 无值返回 null")
    void loadSession_notInRedis_shouldReturnNull() {
        when(valueOperations.get(AuthSessionStore.AUTH_SESSION_KEY_PREFIX + "tok")).thenReturn(null);

        assertNull(authSessionStore.loadSession("tok"));
    }

    @Test
    @DisplayName("loadSession: Redis 有值经 JacksonUtil.convert 还原字段(序列化往返)")
    void loadSession_present_shouldRoundTripFields() {
        AuthSession session = new AuthSession()
                .setSource(AuthSourceEnum.PLATFORM_ALIPAY.getCode())
                .setAuthType("alipay")
                .setChannelMchNo("cm-no")
                .setReturnPath("/cashier/ORD/alipay")
                .setQueryCode("qc-1");
        when(valueOperations.get(AuthSessionStore.AUTH_SESSION_KEY_PREFIX + "tok")).thenReturn(session);

        AuthSession loaded = authSessionStore.loadSession("tok");

        assertNotNull(loaded);
        assertEquals(AuthSourceEnum.PLATFORM_ALIPAY.getCode(), loaded.getSource());
        assertEquals("alipay", loaded.getAuthType());
        assertEquals("cm-no", loaded.getChannelMchNo());
        assertEquals("/cashier/ORD/alipay", loaded.getReturnPath());
        assertEquals("qc-1", loaded.getQueryCode());
    }

    // ==================== deleteSession ====================

    @Test
    @DisplayName("deleteSession: authToken 为空不访问 Redis")
    void deleteSession_blank_shouldNotCallRedis() {
        authSessionStore.deleteSession("");
        authSessionStore.deleteSession(null);
        verifyNoInteractions(redisTemplate);
    }

    @Test
    @DisplayName("deleteSession: 以 AUTH_SESSION_KEY_PREFIX 为前缀删除")
    void deleteSession_normal_shouldDeleteWithPrefix() {
        authSessionStore.deleteSession("tok");

        verify(redisTemplate).delete(AuthSessionStore.AUTH_SESSION_KEY_PREFIX + "tok");
    }

    // ==================== saveWaitingResult / queryAuthResult ====================

    @Test
    @DisplayName("saveWaitingResult: 写入 WAITING 状态, 以 CHANNEL_AUTH_KEY_PREFIX 为前缀")
    void saveWaitingResult_shouldWriteWaitingStatus() {
        authSessionStore.saveWaitingResult("qc-1");

        ArgumentCaptor<AuthResult> captor = ArgumentCaptor.forClass(AuthResult.class);
        verify(valueOperations).set(
                eq(AuthSessionStore.CHANNEL_AUTH_KEY_PREFIX + "qc-1"),
                captor.capture(),
                eq(AuthSessionStore.AUTH_TIMEOUT));
        assertEquals(ChannelAuthStatusEnum.WAITING.getCode(), captor.getValue().getStatus());
    }

    @Test
    @DisplayName("queryAuthResult: Redis 无值返回 NOT_EXIST 状态")
    void queryAuthResult_notInRedis_shouldReturnNotExist() {
        when(valueOperations.get(AuthSessionStore.CHANNEL_AUTH_KEY_PREFIX + "qc")).thenReturn(null);

        AuthResult result = authSessionStore.queryAuthResult("qc");

        assertEquals(ChannelAuthStatusEnum.NOT_EXIST.getCode(), result.getStatus());
    }

    @Test
    @DisplayName("queryAuthResult: Redis 有值经 JacksonUtil.convert 还原字段")
    void queryAuthResult_present_shouldRoundTripFields() {
        AuthResult stored = new AuthResult()
                .setOpenId("oid-x")
                .setStatus(ChannelAuthStatusEnum.SUCCESS.getCode())
                .setReturnPath("/return");
        when(valueOperations.get(AuthSessionStore.CHANNEL_AUTH_KEY_PREFIX + "qc")).thenReturn(stored);

        AuthResult result = authSessionStore.queryAuthResult("qc");

        assertEquals("oid-x", result.getOpenId());
        assertEquals(ChannelAuthStatusEnum.SUCCESS.getCode(), result.getStatus());
        assertEquals("/return", result.getReturnPath());
    }

    // ==================== writeResultByQueryCode ====================

    @Test
    @DisplayName("writeResultByQueryCode: paramQueryCode 非空时优先使用它")
    void writeResultByQueryCode_paramPresent_shouldUseParam() {
        AuthResult result = new AuthResult().setOpenId("oid");

        authSessionStore.writeResultByQueryCode("qc-param", null, result);

        verify(valueOperations).set(
                AuthSessionStore.CHANNEL_AUTH_KEY_PREFIX + "qc-param",
                result,
                AuthSessionStore.AUTH_TIMEOUT);
    }

    @Test
    @DisplayName("writeResultByQueryCode: paramQueryCode 为空时回退使用 session.queryCode")
    void writeResultByQueryCode_paramBlank_shouldFallbackToSession() {
        AuthSession session = new AuthSession().setQueryCode("qc-session");
        AuthResult result = new AuthResult().setOpenId("oid");

        authSessionStore.writeResultByQueryCode(null, session, result);

        verify(valueOperations).set(
                AuthSessionStore.CHANNEL_AUTH_KEY_PREFIX + "qc-session",
                result,
                AuthSessionStore.AUTH_TIMEOUT);
    }

    @Test
    @DisplayName("writeResultByQueryCode: paramQueryCode 与 session.queryCode 均空时不写入")
    void writeResultByQueryCode_bothBlank_shouldNotWrite() {
        authSessionStore.writeResultByQueryCode(null, null, new AuthResult());

        verify(valueOperations, never()).set(any(String.class), any(), any(Duration.class));
    }
}
