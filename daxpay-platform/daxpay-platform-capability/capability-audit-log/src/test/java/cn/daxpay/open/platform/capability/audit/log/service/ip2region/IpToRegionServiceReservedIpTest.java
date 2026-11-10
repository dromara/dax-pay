package cn.daxpay.open.platform.capability.audit.log.service.ip2region;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/// # IpToRegionService 保留地址判定单元测试
///
/// 锁定两点:
/// 1. ip2region 对 127.0.0.1 等保留段返回 "Reserved/0", 需按内网处理;
/// 2. Hutool [cn.hutool.core.net.NetUtil#isInnerIP] 仅接受 IPv4, 传 IPv6 会抛
///    IllegalArgumentException, 分流逻辑必须先把 IPv6 挡在外面。
class IpToRegionServiceReservedIpTest {

    /// 被测服务仅依赖配置对象, 保留地址判定不读取配置, 传 null 即可
    private final IpToRegionService service = new IpToRegionService(null);

    @Test
    @DisplayName("IPv4 回环/私有地址: 判定为保留地址")
    void isReservedIp_privateV4_shouldBeTrue() {
        assertTrue(service.isReservedIp("127.0.0.1"));
        assertTrue(service.isReservedIp("10.1.2.3"));
        assertTrue(service.isReservedIp("172.20.1.1"));
        assertTrue(service.isReservedIp("192.168.1.26"));
    }

    @Test
    @DisplayName("IPv4 公网地址: 判定为非保留地址")
    void isReservedIp_publicV4_shouldBeFalse() {
        assertFalse(service.isReservedIp("172.32.0.1"));
        assertFalse(service.isReservedIp("45.12.33.8"));
    }

    @Test
    @DisplayName("IPv6 回环/链路本地/ULA: 判定为保留地址且不抛异常")
    void isReservedIp_privateV6_shouldBeTrue() {
        assertTrue(service.isReservedIp("::1"));
        assertTrue(service.isReservedIp("fe80::1"));
        assertTrue(service.isReservedIp("fd00::1"));
    }

    @Test
    @DisplayName("IPv6 公网地址: 判定为非保留地址且不抛异常")
    void isReservedIp_publicV6_shouldBeFalse() {
        assertFalse(service.isReservedIp("2408:8207:1234::1"));
    }
}
