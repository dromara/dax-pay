package cn.daxpay.open.payment.auth.develop;

import cn.daxpay.open.payment.auth.channel.MerchantChannelAuthService;
import cn.daxpay.open.payment.auth.core.AppScopeEnum;
import cn.daxpay.open.payment.auth.core.AuthSessionStore;
import cn.daxpay.open.payment.auth.platform.AlipayAuthProvider;
import cn.daxpay.open.payment.douyin.enums.DyAppTypeEnum;
import cn.daxpay.open.payment.douyin.facade.DouyinAppFacade;
import cn.daxpay.open.payment.douyin.facade.DyAppView;
import cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverTypeEnum;
import cn.daxpay.open.payment.unipay.param.assist.GenerateAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthResult;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.payment.wx.enums.WxAppTypeEnum;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.payment.wx.facade.WxAppView;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 分账接收方扫码授权服务
///
/// 接收方报备表单扫码获取 openId/userId, 复用认证域 OAuth 授权 + queryCode 轮询机制:
/// - **支付宝(直连/服务商)**: USER_ID 为全局 2088 用户号, 平台级 auth_base 静默授权获取
/// - **微信(直连/服务商)**: openid 与所选应用维度绑定, 公众号 OAuth 网页授权获取;
///   仅支持公众号(official_account)应用, 小程序维度 openid 无法通过网页授权获取(需小程序端登录后手填)
/// - **抖音(直连)**: openid 与所选商户档应用维度绑定, H5 silent_auth 获取, 仅支持网站应用(web_app)
///
/// 应用定位用原始 appId 字符串(与接收方报备参数同构), 由 Facade 解析为档位+主键后
/// 委托 [MerchantChannelAuthService#generateAuthUrl] 统一建会话, 授权结果经
/// [AuthSessionStore#queryAuthResult] 按 queryCode 轮询获取, 与转账扫码/认证调试共用同一机制。
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocReceiverScanAuthService {

    private final AlipayAuthProvider alipayAuthProvider;
    private final MerchantChannelAuthService merchantChannelAuthService;
    private final AuthSessionStore authSessionStore;
    private final WxAppFacade wxAppFacade;
    private final DouyinAppFacade douyinAppFacade;

    /// 生成分账接收方扫码授权链接
    ///
    /// 按支付产品路由授权通道, 各分支校验接收方类型与所需应用参数后生成授权链接。
    public AuthUrlResult generateScanAuthUrl(AllocReceiverScanAuthParam param) {
        ProductEnum product = ProductEnum.findByCode(param.getProduct());
        AllocReceiverTypeEnum receiverType = AllocReceiverTypeEnum.findByCode(param.getReceiverType());
        return switch (product) {
            case ALIPAY, ALIPAY_ISV -> this.generateAlipay(receiverType);
            case WECHAT_PAY -> this.generateWechatDirect(param, receiverType);
            case WECHAT_ISV -> this.generateWechatIsv(param, receiverType);
            case DOUYIN_PAY -> this.generateDouyin(param, receiverType);
            // 其余产品无分账接收方报备能力
            default -> throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.payment.channel.scanTypeNotSupported");
        };
    }

    /// 通过查询码获取扫码授权结果
    public AuthResult queryAuthResult(String queryCode) {
        return authSessionStore.queryAuthResult(queryCode);
    }

    /// 支付宝: USER_ID 全局 2088 用户号, 平台级 auth_base 静默授权获取
    private AuthUrlResult generateAlipay(AllocReceiverTypeEnum receiverType) {
        if (receiverType != AllocReceiverTypeEnum.USER_ID) {
            // LOGIN_NAME 为手机号/邮箱, 不支持扫码获取
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.payment.channel.scanTypeNotSupported");
        }
        return alipayAuthProvider.generateAuthUrl(null);
    }

    /// 微信直连: PERSONAL_OPENID 为所选商户档应用(channelAppId)维度
    private AuthUrlResult generateWechatDirect(AllocReceiverScanAuthParam param, AllocReceiverTypeEnum receiverType) {
        if (receiverType != AllocReceiverTypeEnum.PERSONAL_OPENID) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.payment.channel.scanTypeNotSupported");
        }
        if (StrUtil.isBlank(param.getChannelAppId())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.payment.wx.appNotConfigured", "channelAppId");
        }
        WxAppView app = wxAppFacade.resolveByWxAppId(param.getMchNo(), param.getChannelAppId());
        this.checkWechatOfficialAccount(app);
        return merchantChannelAuthService.generateAuthUrl(
                this.buildInnerParam(param, ChannelAuthTypeEnum.WECHAT, app.scope(), app.id()));
    }

    /// 微信服务商: PERSONAL_OPENID 为服务商应用(sp)维度, PERSONAL_SUB_OPENID 为子商户应用(sub)维度
    private AuthUrlResult generateWechatIsv(AllocReceiverScanAuthParam param, AllocReceiverTypeEnum receiverType) {
        String appId;
        if (receiverType == AllocReceiverTypeEnum.PERSONAL_OPENID) {
            appId = param.getSpAppId();
            if (StrUtil.isBlank(appId)) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.payment.wx.appNotConfigured", "spAppId");
            }
        } else if (receiverType == AllocReceiverTypeEnum.PERSONAL_SUB_OPENID) {
            appId = param.getSubAppId();
            if (StrUtil.isBlank(appId)) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.payment.wx.appNotConfigured", "subAppId");
            }
        } else {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.payment.channel.scanTypeNotSupported");
        }
        WxAppView app = wxAppFacade.resolveByWxAppId(param.getMchNo(), appId);
        this.checkWechatOfficialAccount(app);
        return merchantChannelAuthService.generateAuthUrl(
                this.buildInnerParam(param, ChannelAuthTypeEnum.WECHAT, app.scope(), app.id()));
    }

    /// 抖音直连: PERSONAL_OPENID 为所选商户档应用(channelAppId)维度, H5 silent_auth 需网站应用
    private AuthUrlResult generateDouyin(AllocReceiverScanAuthParam param, AllocReceiverTypeEnum receiverType) {
        if (receiverType != AllocReceiverTypeEnum.PERSONAL_OPENID) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.payment.channel.scanTypeNotSupported");
        }
        if (StrUtil.isBlank(param.getChannelAppId())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.payment.douyin.appNotConfigured", "channelAppId");
        }
        DyAppView app = douyinAppFacade.resolveByDouyinAppId(param.getMchNo(), param.getChannelAppId());
        if (!DyAppTypeEnum.WEB_APP.getCode().equals(app.appType())) {
            // 抖音 H5 静默授权仅网站应用可用
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.payment.channel.scanDouyinAppTypeNotSupported", app.appType());
        }
        return merchantChannelAuthService.generateAuthUrl(
                this.buildInnerParam(param, ChannelAuthTypeEnum.DOUYIN, app.scope(), app.id()));
    }

    /// 校验微信应用为公众号(OAuth 网页授权仅公众号可用, 小程序/移动应用无法获取 openid)
    private void checkWechatOfficialAccount(WxAppView app) {
        if (!WxAppTypeEnum.OFFICIAL_ACCOUNT.getCode().equals(app.appType())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.payment.channel.scanWechatAppTypeNotSupported", app.appType());
        }
    }

    /// 组装认证域统一参数(档位+主键定位应用, 由策略层查密钥)
    private GenerateAuthUrlParam buildInnerParam(AllocReceiverScanAuthParam param, ChannelAuthTypeEnum authType,
                                                 AppScopeEnum appScope, Long appRefId) {
        return new GenerateAuthUrlParam()
                .setAuthType(authType.getCode())
                .setMchNo(param.getMchNo())
                .setChannelMchNo(param.getChannelMchNo())
                .setAppScope(appScope.getCode())
                .setAppRefId(appRefId);
    }
}
