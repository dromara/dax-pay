package cn.daxpay.open.payment.web.develop.service;

import cn.daxpay.open.payment.core.assist.ChannelAuthService;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 认证调试服务
///
/// 首期仅支持支付宝: 平台级配置生成 H5 中间页授权链接, 轮询 queryCode 取结果。
@Slf4j
@Service
@RequiredArgsConstructor
public class DevelopAuthService {

    private final ChannelAuthService channelAuthService;

    /// 生成支付宝授权链接(中间页 + queryCode)
    public AuthUrlResult generateAlipayAuthUrl() {
        return channelAuthService.generateAlipayAuthUrl();
    }

    /// 通过查询码获取认证结果
    public AuthResult queryAuthResult(String queryCode) {
        return channelAuthService.queryAuthResult(queryCode);
    }
}
