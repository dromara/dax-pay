package org.dromara.daxpay.payment.pay.service.assist;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import org.dromara.daxpay.platform.core.exception.operation.OperationFailException;
import org.dromara.daxpay.platform.core.code.CommonCode;
import org.dromara.daxpay.payment.unipay.result.assist.AuthResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;

/// # 微信小程序认证服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatMiniAuthService {

    /// 获取微信AccessToken数据
    public AuthResult getOpenId(String authCode, String wxAppId, String appSecret){
        var wxMaService = this.getWxMaService(wxAppId,appSecret);
        WxMaJscode2SessionResult sessionResult;
        try {
            sessionResult = wxMaService.jsCode2SessionInfo(authCode);
        } catch (WxErrorException e) {
            // 微信认证失败
            throw new OperationFailException(CommonCode.FAIL_CODE, "pay.error.assist.wechatAuthFail", e.getMessage());
        }
        return new AuthResult()
                .setOpenId(sessionResult.getOpenid());
    }

    /// 获取微信小程序API的Service
    public WxMaService getWxMaService(String wxAppId, String appSecret) {
        WxMaService wxMaService = new WxMaServiceImpl();
        var wxMpConfig = new WxMaDefaultConfigImpl();
        // 设置微信小程序的appid
        wxMpConfig.setAppid(wxAppId);
        // 设置微信小程序的app corpSecret
        wxMpConfig.setSecret(appSecret);
        try {
            wxMaService.setWxMaConfig(wxMpConfig);
        } catch (Exception e) {
            // 微信小程序认证配置错误
            throw new OperationFailException(CommonCode.FAIL_CODE, "pay.error.assist.wechatMiniAuthConfigError", e.getMessage());
        }
        return wxMaService;
    }

}
