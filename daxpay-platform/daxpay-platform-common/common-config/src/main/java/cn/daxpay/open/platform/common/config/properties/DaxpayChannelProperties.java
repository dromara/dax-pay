package cn.daxpay.open.platform.common.config.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/// # 通道适配子应用配置
///
/// 统一管理各通道适配子应用(dax-pay-channel-one/two/...)的连接地址与传输加密密钥。
/// 各通道模块通过注入本类获取对应子应用的 baseUrl 与 transportEncrypt。
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "daxpay.channel")
public class DaxpayChannelProperties {

    /// 子应用1(支付宝/微信)
    private ChannelApp one = new ChannelApp();

    /// 子应用2(其他支付通道) — channel-two 已搭建架子, 通道待对接
    private ChannelApp two = new ChannelApp();

    /// 子应用3(抖音/其他) — 未来扩展
    private ChannelApp three = new ChannelApp();

    /// # 子应用连接配置
    @Data
    @Accessors(chain = true)
    public static class ChannelApp {

        /// 基础地址
        private String baseUrl = "http://127.0.0.1:20100";

        /// 传输加密（强制常开，key 必填）
        private TransportEncrypt transportEncrypt = new TransportEncrypt();
    }

    /// # 通道 HTTP 报文传输加密配置
    ///
    /// 与 `daxpay.platform.config.encrypt`（DB/缓存存储加密）密钥隔离。
    @Data
    @Accessors(chain = true)
    public static class TransportEncrypt {

        /// AES-256 密钥，恰好 32 个 UTF-8 字符；创建通道 Client 时强校验
        private String key;
    }
}
