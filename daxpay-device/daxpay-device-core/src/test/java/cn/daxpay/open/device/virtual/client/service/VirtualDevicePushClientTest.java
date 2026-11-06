package cn.daxpay.open.device.virtual.client.service;

import cn.daxpay.open.device.print.service.PrintPushService;
import cn.daxpay.open.platform.common.artemis.service.ArtemisTemplateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

/// # 虚拟设备推送主题契约测试
///
/// 锁定 Artemis 2.5x(ARTEMIS-4532)下的主题映射: 设备订阅斜杠主题, broker 映射为点号多段
/// core 地址; 平台 JMS Topic 必须发布到同一多段地址才能路由到订阅队列。改回点号主题或
/// 字面转义地址(单段)时, 本测试当场红。
@ExtendWith(MockitoExtension.class)
class VirtualDevicePushClientTest {

    /// 32 字符激活密钥(AES-256-GCM 要求)
    private static final String SECRET = "0123456789abcdef0123456789abcdef";

    @Mock
    private ArtemisTemplateService artemisTemplateService;

    @Mock
    private VirtualDeviceStatService virtualDeviceStatService;

    @Mock
    private PrintPushService printPushService;

    @InjectMocks
    private VirtualDevicePushClient client;

    @Test
    @DisplayName("主题契约: 设备订阅斜杠主题, 平台发布点号多段地址")
    void testTopicContract() {
        assertEquals("daxpay/device/audio/VD001", VirtualDevicePushClient.topic("VD001"));
        assertEquals("daxpay.device.audio.VD001", VirtualDevicePushClient.publishAddress("VD001"));
    }

    @Test
    @DisplayName("试播: 发布到点号多段地址(与 MQTT 订阅队列同址), 不计日推送笔数")
    void testBroadcastPublishAddress() {
        client.testBroadcast("VD001", SECRET, 100L, "other");

        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);
        verify(artemisTemplateService).sendTopic(eq("daxpay.device.audio.VD001"), jsonCaptor.capture());
        assertTrue(jsonCaptor.getValue().contains("\"cmd\":\"audio.test\""));
    }

    @Test
    @DisplayName("踢出: 同样发布到点号多段地址")
    void testKickPublishAddress() {
        client.kick("VD001", "凭证已作废");

        verify(artemisTemplateService).sendTopic(eq("daxpay.device.audio.VD001"), anyString());
    }
}
