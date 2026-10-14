package cn.daxpay.open.platform.iam.service.social.scan;

import java.util.Objects;

import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.capability.social.justauth.SocialAuthConfig;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthUser;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.iam.enums.SocialAuthMode;
import cn.daxpay.open.platform.iam.enums.SocialClientEnum;
import cn.daxpay.open.platform.iam.result.social.SocialBindScanConfirmResult;
import cn.daxpay.open.platform.iam.result.social.SocialBindScanQrResult;
import cn.daxpay.open.platform.iam.result.social.SocialBindScanStatusResult;
import cn.daxpay.open.platform.iam.result.social.SocialExchangeResult;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import cn.daxpay.open.platform.iam.service.social.IamUserSocialBindStore;
import cn.daxpay.open.platform.iam.service.social.SocialAuthSupport;
import cn.daxpay.open.platform.iam.service.user.UserQueryService;
import cn.daxpay.open.platform.system.service.config.auth.PlatformWechatMpAuthConfigService;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 公众号扫码绑定编排服务(跨设备)
///
/// 公众号网页授权仅微信客户端内可完成, PC 浏览器直接发起必被微信拒绝;
/// 由 PC 端生成带短期票据的 H5 页面地址并转二维码, 手机微信扫码完成授权后
/// 凭票据中的用户身份落绑定(手机端无登录态), PC 端轮询票据状态刷新绑定列表.
/// 授权链路原语(code 兑换/授权地址构建/凭据回填)复用 [SocialAuthSupport].
///
@Slf4j
@Service
@RequiredArgsConstructor
public class SocialBindScanService {

    /// 扫码绑定回调路径(公众号跨设备绑定专用, 落在网关域名的 H5 回调页, 不经透传页)
    private static final String SCAN_BIND_CALLBACK_PATH = "/social/bind-scan-callback";

    /// 扫码绑定 H5 发起页路径(二维码内容, 手机微信扫码打开)
    private static final String SCAN_BIND_PAGE_PATH = "/social/bind-scan";

    private final SocialAuthSupport socialAuthSupport;

    private final SocialBindScanStore socialBindScanStore;

    private final IamUserSocialBindStore socialBindStore;

    private final UserQueryService userQueryService;

    /// 生成扫码绑定二维码(PC 端发起, 需登录)
    ///
    /// 票据 5 分钟过期, 绑定结果经 [#queryBindScanStatus] 轮询获取.
    public SocialBindScanQrResult createBindScanTicket(String source, String client) {
        SocialSourceEnum socialSource = SocialSourceEnum.of(source);
        if (Objects.isNull(socialSource) || !socialSource.isWechatOnly()) {
            // 仅公众号受微信客户端限制, 其余平台 PC 可直接发起绑定
            throw new OperationFailException("error.social.bind.scanNotSupported");
        }
        socialAuthSupport.requireSocialClient(client);
        Long userId = SecurityUtil.getUserId();
        // 当前用户必须属于该身份域, 防止跨端绑定(对齐标准 OAuth 绑定兑换 exchangeForBind 的校验)
        UserInfoResult userInfo = userQueryService.findById(userId);
        if (!Objects.equals(client, userInfo.getClientCode())) {
            throw new OperationFailException("error.social.bind.clientMismatch");
        }
        // 预校验配置(平台启用/公众号凭据/网关地址), 避免二维码生成后手机端才发现配置缺失;
        // 传入临时空配置仅为触发 [SocialAuthSupport#fillPlatformRedirectCredentials] 内的凭据完整性校验
        socialAuthSupport.loadEnabledConfig(source);
        socialAuthSupport.fillPlatformRedirectCredentials(socialSource, new SocialAuthConfig());
        String gatewayBase = socialAuthSupport.requireGatewayBase();
        String ticket = IdUtil.fastSimpleUUID();
        socialBindScanStore.saveWaiting(ticket, userId, client, source);
        String qrUrl = gatewayBase + SCAN_BIND_PAGE_PATH + "?ticket=" + ticket;
        return new SocialBindScanQrResult().setTicket(ticket).setQrUrl(qrUrl);
    }

    /// 生成扫码绑定的微信授权地址(手机端 H5 发起页调用, 凭票据定位待绑定用户)
    ///
    /// state 仍为纯 UUID 零编码, 经 Redis 一次性映射回票据(见 [SocialBindScanStore#saveState]),
    /// 授权回调直接落网关域名的扫码绑定回调页(不经透传页, 与登录/普通绑定回调互不干扰).
    public String buildBindScanAuthUrl(String ticket) {
        SocialBindScanStore.BindScanContext context = this.requireScanContext(ticket);
        SocialSourceEnum socialSource = SocialSourceEnum.of(context.getSource());
        String state = IdUtil.fastSimpleUUID();
        socialBindScanStore.saveState(state, ticket);
        return socialAuthSupport.buildScanAuthorizeUrl(socialSource, context.getClientCode(), state, SCAN_BIND_CALLBACK_PATH);
    }

    /// 扫码绑定的授权码兑换(手机端回调页调用)
    ///
    /// state 一次性映射换取票据, 凭票据中的用户身份完成绑定(手机端无登录态);
    /// 兑换成功后将票据标记为 success, PC 端轮询到该状态后刷新绑定列表.
    public SocialExchangeResult exchangeBindScan(String code, String state) {
        try {
            String ticket = socialBindScanStore.consumeTicketByState(state);
            SocialBindScanStore.BindScanContext context = Objects.isNull(ticket) ? null : socialBindScanStore.get(ticket);
            if (Objects.isNull(context)) {
                // state 映射或票据已过期
                throw new OperationFailException("error.social.bind.scanTicketInvalid");
            }
            // 回调地址与发起侧保持一致(扫码绑定走网关独立回调路径)
            String redirectUri = socialAuthSupport.requireGatewayBase() + SCAN_BIND_CALLBACK_PATH;
            AuthUser authUser = socialAuthSupport.doExchange(code, state, context.getSource(),
                SocialClientEnum.findByCode(context.getClientCode()).orElse(null), SocialAuthMode.BIND, redirectUri);
            socialBindStore.saveBind(context.getUserId(), context.getClientCode(), authUser);
            socialBindScanStore.markSuccess(ticket, context);
            return new SocialExchangeResult().setResult("bind_success");
        }
        catch (OperationFailException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("扫码绑定失败: msg={}", e.getMessage(), e);
            return new SocialExchangeResult().setError("oauth_failed");
        }
    }

    /// 查询扫码绑定结果(PC 端轮询, 需登录)
    ///
    /// 票据不存在、已过期或不属于当前用户时统一返回 not_exist(防票据遍历探测).
    public SocialBindScanStatusResult queryBindScanStatus(String ticket) {
        SocialBindScanStore.BindScanContext context = socialBindScanStore.get(ticket);
        Long userId = SecurityUtil.getUserId();
        String status = Objects.isNull(context) || !Objects.equals(userId, context.getUserId())
            ? SocialBindScanStatusResult.NOT_EXIST
            : context.getStatus();
        return new SocialBindScanStatusResult().setStatus(status);
    }

    /// 查询扫码绑定确认信息(手机端 H5 确认页调用, 匿名)
    ///
    /// 返回脱敏的目标账号名, 供扫码者核对是否本人的电脑端账号——这是
    /// 「微信身份抢占」钓鱼(诱导扫码把微信绑到他人账号)的唯一拦截点.
    /// 手机端打开确认页即视为已扫码, 顺带把票据标记为 scanned(仅 waiting 迁入,
    /// PC 端轮询凭此展示"扫码成功待操作"中间态).
    public SocialBindScanConfirmResult queryBindScanConfirm(String ticket) {
        SocialBindScanStore.BindScanContext context = this.requireScanContext(ticket);
        if (Objects.equals(context.getStatus(), SocialBindScanStore.STATUS_WAITING)) {
            socialBindScanStore.markScanned(ticket, context);
        }
        UserInfoResult userInfo = userQueryService.findById(context.getUserId());
        String account = Objects.isNull(userInfo) ? null : userInfo.getAccount();
        return new SocialBindScanConfirmResult().setMaskedAccount(this.maskAccount(account));
    }

    /// 取消扫码绑定(手机端确认页取消按钮调用, 匿名)
    ///
    /// 手机端确认页是扫码绑定的按次用户同意环节, 取消后拒绝为票据对应的
    /// PC 端账号落绑定. waiting/scanned 均可取消(确认页在扫码后展示, 取消时
    /// 票据多处于 scanned 中间态; 授权已完成或票据不存在时为空操作, 不暴露票据状态).
    public void cancelBindScan(String ticket) {
        SocialBindScanStore.BindScanContext context = socialBindScanStore.get(ticket);
        boolean cancellable = Objects.nonNull(context)
            && (Objects.equals(context.getStatus(), SocialBindScanStore.STATUS_WAITING)
                || Objects.equals(context.getStatus(), SocialBindScanStore.STATUS_SCANNED));
        if (!cancellable) {
            return;
        }
        socialBindScanStore.markCancelled(ticket, context);
    }

    /// 账号脱敏: 手机号/邮箱各按通行规则, 其余(用户名)保留首尾各一位
    private String maskAccount(String account) {
        if (StrUtil.isBlank(account)) {
            return StrUtil.EMPTY;
        }
        if (Validator.isEmail(account)) {
            return DesensitizedUtil.email(account);
        }
        if (Validator.isMobile(account)) {
            return DesensitizedUtil.mobilePhone(account);
        }
        // 过短时全量脱敏, 避免保留首尾即暴露全文
        if (account.length() <= 2) {
            return StrUtil.repeat('*', 4);
        }
        return account.charAt(0) + StrUtil.repeat('*', 4) + account.charAt(account.length() - 1);
    }

    /// 查询扫码票据上下文, 不存在或已过期抛业务异常
    private SocialBindScanStore.BindScanContext requireScanContext(String ticket) {
        SocialBindScanStore.BindScanContext context = socialBindScanStore.get(ticket);
        if (Objects.isNull(context)) {
            throw new OperationFailException("error.social.bind.scanTicketInvalid");
        }
        return context;
    }
}
