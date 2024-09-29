package cn.daxpay.multi.channel.wechat.service.pay;

import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.channel.wechat.param.pay.WechatPayParam;
import cn.daxpay.multi.channel.wechat.service.config.WechatPayConfigService;
import cn.daxpay.multi.core.exception.PayFailureException;
import cn.daxpay.multi.core.param.cashier.CashierAuthCodeParam;
import cn.daxpay.multi.core.param.cashier.CashierPayParam;
import cn.daxpay.multi.core.param.trade.pay.PayParam;
import cn.daxpay.multi.service.entity.config.PlatformConfig;
import cn.daxpay.multi.service.service.config.PlatformConfigService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.stereotype.Service;

/**
 * 微信收银
 * @author xxm
 * @since 2024/9/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPayCashierService {

    private final WechatPayConfigService weChatPayConfigService;

    private final PlatformConfigService platformsConfigService;

    /**
     * 生成授权链接, 主要是微信类通道使用, 用于获取OpenId
     */
    public String generateAuthUrl(CashierAuthCodeParam param) {
        WxMpService wxMpService = this.getWxMpService();
        PlatformConfig platformConfig = platformsConfigService.getConfig();
        // 判断是否独立部署前端
        if (platformConfig.isMobileEmbedded()){
            // 嵌入式
            String serverUrl = platformConfig.getGatewayMobileUrl();
            String redirectUrl = StrUtil.format("{}/h5//wechat/cashier/{}/{}", serverUrl, param.getAppId());
            return wxMpService.getOAuth2Service().buildAuthorizationUrl(redirectUrl, WxConsts.OAuth2Scope.SNSAPI_BASE, "");
        } else {
            // 独立部署
            String serverUrl = platformConfig.getGatewayMobileUrl();
            String redirectUrl = StrUtil.format("{}//wechat/cashier/{}/{}", serverUrl, param.getAppId());
            return wxMpService.getOAuth2Service().buildAuthorizationUrl(redirectUrl, WxConsts.OAuth2Scope.SNSAPI_BASE, "");

        }
    }

    /**
     * 参数与处理, 获取OpenId
     * @param cashierPayParam 收银台支付参数
     * @param payParam 统一支付参数
     */
    public void handlePayParam(CashierPayParam cashierPayParam, PayParam payParam) {
        // 获取OpenId
        WxMpService wxMpService = this.getWxMpService();
        try {
            WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(cashierPayParam.getAuthCode());
            WechatPayParam wechatPayParam = new WechatPayParam();
            wechatPayParam.setOpenId(accessToken.getOpenId());
            payParam.setExtraParam(JSONUtil.toJsonStr(wechatPayParam));
        } catch (WxErrorException e) {
            log.error("收银台支付获取OpenId失败, {}", cashierPayParam, e);
            throw new PayFailureException("收银台支付获取OpenId失败");
        }
    }

    /**
     * 获取微信公众号API的Service
     */
    private WxMpService getWxMpService() {
        WechatPayConfig config = weChatPayConfigService.getWechatPayConfig();
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpDefaultConfigImpl wxMpConfig = new WxMpDefaultConfigImpl();
        wxMpConfig.setAppId(config.getWxAppId()); // 设置微信公众号的appid
        wxMpConfig.setSecret(config.getAppSecret()); // 设置微信公众号的app corpSecret
        wxMpService.setWxMpConfigStorage(wxMpConfig);
        return wxMpService;
    }
}
