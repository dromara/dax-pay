package cn.daxpay.open.platform.iam.service.social.scan;

import java.util.Objects;

import cn.daxpay.open.platform.capability.auth.exception.LoginFailureException;
import cn.daxpay.open.platform.capability.social.justauth.SocialAuthConfig;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthUser;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.iam.enums.SocialAuthMode;
import cn.daxpay.open.platform.iam.enums.SocialClientEnum;
import cn.daxpay.open.platform.iam.result.social.SocialExchangeResult;
import cn.daxpay.open.platform.iam.result.social.SocialLoginScanQrResult;
import cn.daxpay.open.platform.iam.result.social.SocialLoginScanStatusResult;
import cn.daxpay.open.platform.iam.service.social.IamSocialLoginHandler;
import cn.daxpay.open.platform.iam.service.social.IamUserSocialBindStore;
import cn.daxpay.open.platform.iam.service.social.SocialAuthSupport;
import cn.hutool.core.util.IdUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 公众号扫码登录编排服务(跨设备)
///
/// 登录场景 PC 端无登录态, 由 PC 登录页生成带短期票据的二维码, 手机微信扫码授权后
/// 按绑定关系回填用户(未绑定不自动注册), PC 端轮询到 success 时凭票据一次性签发登录令牌.
/// 授权链路原语(code 兑换/授权地址构建/凭据回填/用户校验链)复用 [SocialAuthSupport].
///
@Slf4j
@Service
@RequiredArgsConstructor
public class SocialLoginScanService {

    /// 扫码登录回调路径(公众号跨设备登录专用, 落在网关域名的 H5 回调页, 不经透传页)
    private static final String SCAN_LOGIN_CALLBACK_PATH = "/social/login-scan-callback";

    /// 扫码登录 H5 发起页路径(二维码内容, 手机微信扫码打开)
    private static final String SCAN_LOGIN_PAGE_PATH = "/social/login-scan";

    private final SocialAuthSupport socialAuthSupport;

    private final SocialLoginScanStore socialLoginScanStore;

    private final IamUserSocialBindStore socialBindStore;

    private final IamSocialLoginHandler socialLoginHandler;

    /// 生成扫码登录二维码(PC 登录页匿名发起)
    ///
    /// 票据不携带用户身份; 手机端授权兑换后按绑定关系回填用户,
    /// PC 轮询到 success 时凭票据一次性换取登录令牌. 票据 5 分钟过期.
    public SocialLoginScanQrResult createLoginScanTicket(String source, String client) {
        SocialSourceEnum socialSource = SocialSourceEnum.of(source);
        if (Objects.isNull(socialSource) || !socialSource.isWechatOnly()) {
            // 仅公众号受微信客户端限制, 其余平台 PC 可直接跳转授权
            throw new OperationFailException("error.social.login.scanNotSupported");
        }
        socialAuthSupport.requireSocialClient(client);
        // 预校验配置(平台启用/公众号凭据/网关地址), 避免二维码生成后手机端才发现配置缺失;
        // 传入临时空配置仅为触发 [SocialAuthSupport#fillPlatformRedirectCredentials] 内的凭据完整性校验
        socialAuthSupport.loadEnabledConfig(source);
        socialAuthSupport.fillPlatformRedirectCredentials(socialSource, new SocialAuthConfig());
        String gatewayBase = socialAuthSupport.requireGatewayBase();
        String ticket = IdUtil.fastSimpleUUID();
        socialLoginScanStore.saveWaiting(ticket, client, source);
        String qrUrl = gatewayBase + SCAN_LOGIN_PAGE_PATH + "?ticket=" + ticket;
        return new SocialLoginScanQrResult().setTicket(ticket).setQrUrl(qrUrl);
    }

    /// 生成扫码登录的微信授权地址(手机端 H5 发起页调用, 凭票据定位身份域与平台)
    ///
    /// state 仍为纯 UUID 零编码, 经 Redis 一次性映射回票据(见 [SocialLoginScanStore#saveState]),
    /// 授权回调直接落网关域名的扫码登录回调页(与扫码绑定回调路径分离, 互不干扰).
    public String buildLoginScanAuthUrl(String ticket) {
        SocialLoginScanStore.LoginScanContext context = this.requireLoginScanContext(ticket);
        SocialSourceEnum socialSource = SocialSourceEnum.of(context.getSource());
        String state = IdUtil.fastSimpleUUID();
        socialLoginScanStore.saveState(state, ticket);
        return socialAuthSupport.buildScanAuthorizeUrl(socialSource, context.getClientCode(), state, SCAN_LOGIN_CALLBACK_PATH);
    }

    /// 扫码登录的授权码兑换(手机端回调页调用)
    ///
    /// state 一次性映射换取票据; 授权微信未绑定账号时标记 not_bound 并引导先用账号密码
    /// 登录后绑定(不自动注册); 已绑定则在手机端请求上下文完成用户校验(身份域归属 + 状态
    /// 检查链, 失败原因直接透出到手机页), 通过后标记 success 待 PC 轮询签发登录态.
    public SocialExchangeResult exchangeLoginScan(String code, String state,
                                                  HttpServletRequest request, HttpServletResponse response) {
        try {
            String ticket = socialLoginScanStore.consumeTicketByState(state);
            SocialLoginScanStore.LoginScanContext context = Objects.isNull(ticket) ? null : socialLoginScanStore.get(ticket);
            if (Objects.isNull(context)) {
                // state 映射或票据已过期
                throw new OperationFailException("error.social.login.scanTicketInvalid");
            }
            // 回调地址与发起侧保持一致(扫码登录走网关独立回调路径)
            String redirectUri = socialAuthSupport.requireGatewayBase() + SCAN_LOGIN_CALLBACK_PATH;
            AuthUser authUser = socialAuthSupport.doExchange(code, state, context.getSource(),
                SocialClientEnum.findByCode(context.getClientCode()).orElse(null), SocialAuthMode.LOGIN, redirectUri);
            Long userId = socialBindStore.findUserIdBySourceAndOpenId(context.getSource(), authUser.getUuid()).orElse(null);
            if (Objects.isNull(userId)) {
                // 未绑定: 不自动注册, 手机端引导用账号密码登录后在个人中心绑定
                socialLoginScanStore.markNotBound(ticket, context);
                return new SocialExchangeResult().setError("unbind");
            }
            socialAuthSupport.validateSocialLoginUser(userId, context.getClientCode(), context.getSource(), request, response);
            socialLoginScanStore.markSuccess(ticket, context.setUserId(userId));
            return new SocialExchangeResult().setResult("login_ready");
        }
        catch (OperationFailException | LoginFailureException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("扫码登录兑换失败: msg={}", e.getMessage(), e);
            return new SocialExchangeResult().setError("oauth_failed");
        }
    }

    /// 查询扫码登录结果(PC 登录页匿名轮询)
    ///
    /// 票据不存在或已过期统一返回 not_exist; success 状态一次性消费(读取即删除),
    /// 在 PC 轮询请求的上下文签发登录令牌——双因素等登录拦截以业务异常透出(40101+
    /// preAuthToken, 与 exchange-login 同协议), 由前端进入二次验证面板.
    public SocialLoginScanStatusResult queryLoginScanStatus(String ticket,
                                                            HttpServletRequest request, HttpServletResponse response) {
        SocialLoginScanStore.LoginScanContext context = socialLoginScanStore.get(ticket);
        if (Objects.isNull(context)) {
            return new SocialLoginScanStatusResult().setStatus(SocialLoginScanStatusResult.NOT_EXIST);
        }
        if (Objects.equals(context.getStatus(), SocialLoginScanStore.STATUS_NOT_BOUND)) {
            return new SocialLoginScanStatusResult().setStatus(SocialLoginScanStore.STATUS_NOT_BOUND);
        }
        if (Objects.equals(context.getStatus(), SocialLoginScanStore.STATUS_CANCELLED)) {
            return new SocialLoginScanStatusResult().setStatus(SocialLoginScanStatusResult.CANCELLED);
        }
        if (!Objects.equals(context.getStatus(), SocialLoginScanStore.STATUS_SUCCESS)) {
            return new SocialLoginScanStatusResult().setStatus(SocialLoginScanStore.STATUS_WAITING);
        }
        // 一次性消费: 并发轮询只有一方拿到上下文并签发令牌
        SocialLoginScanStore.LoginScanContext consumed = socialLoginScanStore.consume(ticket);
        if (Objects.isNull(consumed) || Objects.isNull(consumed.getUserId())) {
            return new SocialLoginScanStatusResult().setStatus(SocialLoginScanStatusResult.NOT_EXIST);
        }
        String token = socialLoginHandler.login(consumed.getUserId(), consumed.getClientCode(), consumed.getSource(), request, response);
        return new SocialLoginScanStatusResult().setStatus(SocialLoginScanStore.STATUS_SUCCESS).setToken(token);
    }

    /// 取消扫码登录(手机端确认页取消按钮调用, 匿名)
    ///
    /// 微信授权页仅首次授权时弹出, 不构成按次确认; 手机端确认页是跨设备扫码登录
    /// 唯一的按次用户同意环节, 取消后拒绝为 PC 端签发登录态.
    /// 仅 waiting 状态可取消(授权已完成或票据不存在时为空操作, 不暴露票据状态).
    public void cancelLoginScan(String ticket) {
        SocialLoginScanStore.LoginScanContext context = socialLoginScanStore.get(ticket);
        if (Objects.isNull(context) || !Objects.equals(context.getStatus(), SocialLoginScanStore.STATUS_WAITING)) {
            return;
        }
        socialLoginScanStore.markCancelled(ticket, context);
    }

    /// 查询扫码登录票据上下文, 不存在或已过期抛业务异常
    private SocialLoginScanStore.LoginScanContext requireLoginScanContext(String ticket) {
        SocialLoginScanStore.LoginScanContext context = socialLoginScanStore.get(ticket);
        if (Objects.isNull(context)) {
            throw new OperationFailException("error.social.login.scanTicketInvalid");
        }
        return context;
    }
}
