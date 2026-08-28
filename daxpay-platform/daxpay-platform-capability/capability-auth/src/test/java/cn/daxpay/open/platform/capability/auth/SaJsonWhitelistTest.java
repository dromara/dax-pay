package cn.daxpay.open.platform.capability.auth;

import cn.dev33.satoken.json.SaJsonTemplateForJackson3;
import cn.dev33.satoken.session.SaSession;
import cn.daxpay.open.platform.core.entity.UserDetail;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/// # Sa-Token JSON 多态反序列化白名单回归测试
///
/// Sa-Token 1.46.0 修复 Jackson DefaultTyping 多态反序列化 RCE, 新增 `SaJsonStrategy`
/// 全局类型白名单(默认仅放行 JDK 内置类型)。项目将 `UserDetail` 存入 `SaSession`
/// 持久化到 Redis(TokenService/UserInfoService 写入, SecurityUtil/OnlineUserService 读取),
/// 必须经 `META-INF/satoken/sa-json-type.list` SPI 注册进白名单, 否则读取会话时抛
/// `SaJsonConvertException`(无法反序列化的类型)。
///
/// 本测试模拟会话的完整写入/读取往返, 防止白名单注册失效或漏注册新对象导致升级回归。
class SaJsonWhitelistTest {

    /// 含 UserDetail 的会话经 Jackson3 模板序列化后, 必须能通过白名单完整还原
    @Test
    void roundTripUserDetailSession() {
        SaJsonTemplateForJackson3 jsonTemplate = new SaJsonTemplateForJackson3();

        // 写入侧: 等效 TokenService#login 时 session.set(USER, userDetail)
        // (显式给 id, set() 内部会触发 update() 持久化到默认内存 dao)
        SaSession session = new SaSession("test-session-id");
        UserDetail userDetail = UserDetail.of(1000L, "测试用户", "admin", "test", true, "active")
                .setPasswordExpireTime(OffsetDateTime.parse("2026-12-31T00:00:00Z"));
        session.set("user", userDetail);
        String json = jsonTemplate.objectToJson(session);
        assertTrue(json.contains("@class"), "持久化 JSON 应包含多态类型信息");

        // 读取侧: 等效 dao 反序列化 + OnlineUserService 的手动 jsonToObject 路径,
        // UserDetail 未注册白名单时此处抛 SaJsonConvertException
        SaSession parsed = jsonTemplate.jsonToObject(json, SaSession.class);
        assertNotNull(parsed);
        UserDetail restored = parsed.getModel("user", UserDetail.class);
        assertNotNull(restored);
        assertEquals(1000L, restored.getId().longValue());
        assertEquals("测试用户", restored.getName());
        assertEquals("test", restored.getAccount());
        assertTrue(restored.isAdmin());
        assertEquals(OffsetDateTime.parse("2026-12-31T00:00:00Z"), restored.getPasswordExpireTime());
    }
}
