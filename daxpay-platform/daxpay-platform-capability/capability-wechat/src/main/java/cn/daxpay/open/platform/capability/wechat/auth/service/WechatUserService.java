package cn.daxpay.open.platform.capability.wechat.auth.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatPhoneResult;
import cn.daxpay.open.platform.capability.wechat.auth.result.WechatUserInfoResult;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.stereotype.Service;
import cn.daxpay.open.platform.core.code.CommonCode;

/// # 微信用户信息服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatUserService {

    /// 获取公众号用户信息
    /// @param openId 用户OpenId
    /// @param appId 公众号AppId
    /// @param appSecret 公众号AppSecret
    /// @return 用户信息结果
    @SuppressWarnings("deprecation")
    public WechatUserInfoResult getUserInfo(String openId, String appId, String appSecret) {
        // 验证参数
        if (StrUtil.isBlank(openId) || StrUtil.isBlank(appId) || StrUtil.isBlank(appSecret)) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.channel.wechat.paramsRequired");
        }
        
        try {
            // 创建微信服务
            WxMpService wxMpService = createWxMpService(appId, appSecret);
            
            // 获取用户信息
            WxMpUser wxMpUser = wxMpService.getUserService().userInfo(openId);
            
            if (wxMpUser == null) {
                throw new OperationFailException(CommonCode.FAIL_CODE, "error.channel.wechat.userInfoNotFound");
            }
            
            // 转换结果
            WechatUserInfoResult result = new WechatUserInfoResult();
            result.setOpenId(wxMpUser.getOpenId());
            result.setNickname(wxMpUser.getNickname());
            result.setHeadImgUrl(wxMpUser.getHeadImgUrl());
            // 注意：weixin-java-mp 4.8.1.B版本的WxMpUser不再提供getSex(), getCountry(), getProvince(), getCity()方法
            // 这些字段在新版本中已被移除，如需获取详细用户信息，请使用OAuth2方式
            result.setLanguage(wxMpUser.getLanguage());
            result.setSubscribe(wxMpUser.getSubscribe());
            result.setSubscribeTime(wxMpUser.getSubscribeTime());
            result.setUnionId(wxMpUser.getUnionId());
            
            log.info("获取公众号用户信息成功，openId: {}", openId);
            return result;
            
        } catch (WxErrorException e) {
            log.error("获取公众号用户信息失败，openId: {}, 错误: {}", openId, e.getMessage());
            
            // 判断是否是用户未关注
            if (e.getError().getErrorCode() == 49003) {
                throw new OperationFailException(CommonCode.FAIL_CODE, "error.channel.wechat.userNotSubscribed");
            }
            
            // 微信: 获取用户信息失败: {0}
            throw new OperationFailException("error.channel.wechat.userInfoFetchFailed", e.getError().getErrorMsg());
        } catch (Exception e) {
            log.error("获取公众号用户信息异常，openId: {}", openId, e);
            // 微信: 获取用户信息异常: {0}
            throw new OperationFailException("error.channel.wechat.userInfoFetchError", e.getMessage());
        }
    }

    /// 获取小程序用户手机号
    /// @param code 手机号授权码
    /// @param appId 小程序AppId
    /// @param appSecret 小程序AppSecret
    /// @return 手机号结果
    public WechatPhoneResult getPhoneNumber(String code, String appId, String appSecret) {
        // 验证参数
        if (StrUtil.isBlank(code) || StrUtil.isBlank(appId) || StrUtil.isBlank(appSecret)) {
            // 微信: 参数不能为空
            throw new OperationFailException("error.channel.wechat.paramsRequired");
        }
        
        try {
            // 创建微信小程序服务
            WxMaService wxMaService = createWxMaService(appId, appSecret);
            
            // 获取手机号
            WxMaPhoneNumberInfo phoneInfo = wxMaService.getUserService().getPhoneNoInfo(code);
            
            if (phoneInfo == null) {
                throw new OperationFailException(CommonCode.FAIL_CODE, "error.channel.wechat.phoneNotFound");
            }
            
            // 转换结果
            WechatPhoneResult result = new WechatPhoneResult();
            result.setPhoneNumber(phoneInfo.getPhoneNumber());
            result.setPurePhoneNumber(phoneInfo.getPurePhoneNumber());
            result.setCountryCode(phoneInfo.getCountryCode());
            
            log.info("获取小程序用户手机号成功");
            return result;
            
        } catch (WxErrorException e) {
            log.error("获取小程序用户手机号失败，错误: {}", e.getMessage());
            // 微信: 获取手机号失败: {0}
            throw new OperationFailException("error.channel.wechat.phoneFetchFailed", e.getError().getErrorMsg());
        } catch (Exception e) {
            log.error("获取小程序用户手机号异常", e);
            // 微信: 获取手机号异常: {0}
            throw new OperationFailException("error.channel.wechat.phoneFetchError", e.getMessage());
        }
    }

    /// 创建微信公众号Service
    private WxMpService createWxMpService(String appId, String appSecret) {
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(appId);
        config.setSecret(appSecret);
        wxMpService.setWxMpConfigStorage(config);
        return wxMpService;
    }

    /// 创建微信小程序Service
    private WxMaService createWxMaService(String appId, String appSecret) {
        WxMaService wxMaService = new WxMaServiceImpl();
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(appId);
        config.setSecret(appSecret);
        wxMaService.setWxMaConfig(config);
        return wxMaService;
    }
}

