package cn.daxpay.open.platform.common.config.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/// # 通道适配子应用配置
///
/// 统一管理各通道适配子应用(dax-pay-channel-one/two/...)的连接地址。
/// 各通道模块通过注入本类获取对应子应用的 baseUrl。
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "daxpay.channel")
public class DaxpayChannelProperties {

    /// 子应用1(支付宝/微信)
    private ChannelApp one = new ChannelApp();

    /// 子应用2(银联/拉卡拉) — 未来扩展
    private ChannelApp two = new ChannelApp();

    /// 子应用3(抖音/其他) — 未来扩展
    private ChannelApp three = new ChannelApp();

    /// # 子应用连接配置
    @Data
    @Accessors(chain = true)
    public static class ChannelApp {

        /// 基础地址
        private String baseUrl = "http://127.0.0.1:20100";
    }
}
