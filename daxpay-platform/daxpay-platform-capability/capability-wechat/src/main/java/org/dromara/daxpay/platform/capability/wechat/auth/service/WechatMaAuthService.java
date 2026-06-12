package org.dromara.daxpay.platform.capability.wechat.auth.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import org.dromara.daxpay.platform.core.exception.operation.OperationFailException;
import org.dromara.daxpay.platform.capability.wechat.auth.result.WechatAuthResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;

/// # 微信小程序认证服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatMaAuthService {

    /// 获取微信小程序OpenId
    /// @param authCode 授权码
    /// @param appId 微信小程序AppId
    /// @param appSecret 微信小程序AppSecret
    public WechatAuthResult getOpenId(String authCode, String appId, String appSecret) {
        WxMaService wxMaService = this.getWxMaService(appId, appSecret);
        WxMaJscode2SessionResult sessionResult;
        try {
            sessionResult = wxMaService.jsCode2SessionInfo(authCode);
        } catch (WxErrorException e) {
            // 微信小程序认证失败: {0}
            throw new OperationFailException("error.channel.wechat.maAuthFailed", e.getMessage());
        }
        return new WechatAuthResult()
                .setOpenId(sessionResult.getOpenid());
    }

    /// 获取微信小程序API的Service
    public WxMaService getWxMaService(String appId, String appSecret) {
        WxMaService wxMaService = new WxMaServiceImpl();
        WxMaDefaultConfigImpl wxMaConfig = new WxMaDefaultConfigImpl();
        wxMaConfig.setAppid(appId);
        wxMaConfig.setSecret(appSecret);
        try {
            wxMaService.setWxMaConfig(wxMaConfig);
        } catch (Exception e) {
            // 微信小程序认证配置错误: {0}
            throw new OperationFailException("error.channel.wechat.maAuthConfigError", e.getMessage());
        }
        return wxMaService;
    }
}

