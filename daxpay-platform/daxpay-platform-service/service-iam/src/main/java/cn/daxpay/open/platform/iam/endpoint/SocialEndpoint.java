package cn.daxpay.open.platform.iam.endpoint;

import java.util.List;

import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.iam.param.social.SocialLoginScanCancelParam;
import cn.daxpay.open.platform.iam.param.social.SocialScanExchangeParam;
import cn.daxpay.open.platform.iam.result.social.SocialBindResult;
import cn.daxpay.open.platform.iam.result.social.SocialBindScanQrResult;
import cn.daxpay.open.platform.iam.result.social.SocialBindScanStatusResult;
import cn.daxpay.open.platform.iam.result.social.SocialEnabledPlatformResult;
import cn.daxpay.open.platform.iam.result.social.SocialExchangeResult;
import cn.daxpay.open.platform.iam.result.social.SocialLoginScanQrResult;
import cn.daxpay.open.platform.iam.result.social.SocialLoginScanStatusResult;
import cn.daxpay.open.platform.iam.service.social.SocialLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/// # 第三方社交登录端点
///
/// 瘦控制器, 仅负责 HTTP 参数解包与结果包装, 业务编排全部委托 [SocialLoginService].
/// 采用前端回调模式: 第三方平台直接重定向到前端回调页, 前端拿到 code+state 后
/// 调用 exchange-login 或 exchange-bind 接口完成换 token, 后端不做 302 跳转.
///
@IgnoreAuth
@Tag(name = "第三方社交登录")
@RestController
@RequestMapping("/social")
@RequiredArgsConstructor
public class SocialEndpoint {

    private final SocialLoginService socialLoginService;

    /// 查询已启用的第三方登录平台(登录页公开接口)
    /// 仅返回平台编码列表, 不含任何敏感字段, 供登录页动态渲染第三方登录按钮.
    @Operation(summary = "查询已启用的第三方登录平台")
    @GetMapping("/enabled-list")
    public Result<List<SocialEnabledPlatformResult>> enabledList() {
        return Res.ok(socialLoginService.enabledList());
    }

    /// 生成授权地址
    /// @param source 平台来源
    /// @param client 身份域编码(admin/merchant), 用于解析端点配置中的 baseUrl
    /// @param mode 授权场景(不传则按登录态判断: 已登录=绑定, 未登录=登录)
    @Operation(summary = "生成授权地址")
    @GetMapping("/render/{source}")
    public Result<String> render(@PathVariable String source,
                                 String client,
                                 @RequestParam(required = false) String mode,
                                 @RequestParam(required = false) Boolean silent) {
        return Res.ok(socialLoginService.generateAuthorizeUrl(source, client, mode, silent));
    }

    /// 解析跳转型平台 OAuth 回调的透传目标地址(公开: 网关 H5 透传页无登录态)
    /// 跳转型平台(支付宝/公众号)回调统一落在支付网关域名的透传路径, 透传页凭本接口
    /// 查询对应端的原有回调页地址后跳回, 各端页面与 exchange 接口零改动.
    @Operation(summary = "查询网关透传目标地址")
    @GetMapping("/transit-target")
    public Result<String> transitTarget(@RequestParam("source") String source,
                                        @RequestParam("client") String client,
                                        @RequestParam(required = false) String mode) {
        return Res.ok(socialLoginService.resolveTransitTarget(source, client, mode));
    }

    /// OAuth 授权码兑换 - 登录(公开, 无需认证)
    /// 前端登录回调页(/auth/oauth-callback/{source})收到第三方平台的 code+state 后调用,
    /// 后端完成 code 换 token 并返回登录结果.
    @Operation(summary = "授权码兑换-登录")
    @PostMapping("/exchange-login")
    public Result<SocialExchangeResult> exchangeLogin(@RequestParam("code") String code,
                                                       @RequestParam("state") String state,
                                                       @RequestParam("source") String source,
                                                       @RequestParam("client") String client,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response) {
        return Res.ok(socialLoginService.exchangeForLogin(code, state, source, client, request, response));
    }

    /// OAuth 授权码兑换 - 绑定(需登录)
    /// 前端绑定回调页(/auth/social-bind-callback/{source})收到第三方平台的 code+state 后调用,
    /// 后端完成 code 换 token 并保存绑定关系到当前登录用户.
    @IgnoreAuth(login = true)
    @Operation(summary = "授权码兑换-绑定")
    @PostMapping("/exchange-bind")
    public Result<SocialExchangeResult> exchangeBind(@RequestParam("code") String code,
                                                     @RequestParam("state") String state,
                                                     @RequestParam("source") String source,
                                                     @RequestParam("client") String client) {
        return Res.ok(socialLoginService.exchangeForBind(code, state, source, client));
    }

    /// 查询当前登录用户已绑定的第三方账号
    @IgnoreAuth(login = true)
    @Operation(summary = "已绑定的第三方账号列表")
    @GetMapping("/bind/list")
    public Result<List<SocialBindResult>> bindList() {
        return Res.ok(socialLoginService.bindList(SecurityUtil.getUserId()));
    }

    /// 生成扫码绑定二维码(需登录)
    /// 公众号网页授权仅微信客户端内可完成, PC 端发起绑定时生成含票据的 H5 页面地址,
    /// 前端转二维码供手机微信扫码, 绑定结果凭票据经 status 接口轮询.
    @IgnoreAuth(login = true)
    @Operation(summary = "生成扫码绑定二维码")
    @GetMapping("/bind-scan/create")
    public Result<SocialBindScanQrResult> bindScanCreate(@RequestParam("source") String source,
                                                         @RequestParam("client") String client) {
        return Res.ok(socialLoginService.createBindScanTicket(source, client));
    }

    /// 生成扫码绑定的微信授权地址(公开: 手机端 H5 发起页无登录态, 凭票据定位待绑定用户)
    @Operation(summary = "生成扫码绑定授权地址")
    @GetMapping("/bind-scan/auth-url")
    public Result<String> bindScanAuthUrl(@RequestParam("ticket") String ticket) {
        return Res.ok(socialLoginService.buildBindScanAuthUrl(ticket));
    }

    /// 扫码绑定的授权码兑换(公开: 手机端回调页无登录态, 绑定用户由票据决定)
    /// 调用方是 H5 回调页: H5 的 http 封装会把 POST 的 params 并入 JSON body(不挂 query),
    /// 故以 body 接参, 与 Web 端调用、走 query 参数的 exchange-login/exchange-bind 不同.
    @Operation(summary = "扫码绑定授权码兑换")
    @PostMapping("/exchange-bind-scan")
    public Result<SocialExchangeResult> exchangeBindScan(@RequestBody @Validated SocialScanExchangeParam param) {
        return Res.ok(socialLoginService.exchangeBindScan(param.getCode(), param.getState()));
    }

    /// 查询扫码绑定结果(需登录, PC 端轮询)
    @IgnoreAuth(login = true)
    @Operation(summary = "查询扫码绑定结果")
    @GetMapping("/bind-scan/status")
    public Result<SocialBindScanStatusResult> bindScanStatus(@RequestParam("ticket") String ticket) {
        return Res.ok(socialLoginService.queryBindScanStatus(ticket));
    }

    /// 生成扫码登录二维码(公开: 登录页无登录态, 票据即轮询凭证)
    @Operation(summary = "生成扫码登录二维码")
    @GetMapping("/login-scan/create")
    public Result<SocialLoginScanQrResult> loginScanCreate(@RequestParam("source") String source,
                                                           @RequestParam("client") String client) {
        return Res.ok(socialLoginService.createLoginScanTicket(source, client));
    }

    /// 生成扫码登录的微信授权地址(公开: 手机端 H5 发起页凭票据换取)
    @Operation(summary = "生成扫码登录授权地址")
    @GetMapping("/login-scan/auth-url")
    public Result<String> loginScanAuthUrl(@RequestParam("ticket") String ticket) {
        return Res.ok(socialLoginService.buildLoginScanAuthUrl(ticket));
    }

    /// 扫码登录的授权码兑换(公开: 手机端回调页无登录态, 登录用户由绑定关系决定)
    /// 调用方是 H5 回调页: 参数以 JSON body 提交(同 exchange-bind-scan).
    /// 授权微信未绑定账号时返回 error=unbind(不自动注册).
    @Operation(summary = "扫码登录授权码兑换")
    @PostMapping("/exchange-login-scan")
    public Result<SocialExchangeResult> exchangeLoginScan(@RequestBody @Validated SocialScanExchangeParam param,
                                                          HttpServletRequest request,
                                                          HttpServletResponse response) {
        return Res.ok(socialLoginService.exchangeLoginScan(param.getCode(), param.getState(), request, response));
    }

    /// 查询扫码登录结果(公开: PC 登录页轮询, success 一次性返回登录令牌;
    /// 双因素等登录拦截以业务码 40101+preAuthToken 透出, 与 exchange-login 同协议)
    @Operation(summary = "查询扫码登录结果")
    @GetMapping("/login-scan/status")
    public Result<SocialLoginScanStatusResult> loginScanStatus(@RequestParam("ticket") String ticket,
                                                               HttpServletRequest request,
                                                               HttpServletResponse response) {
        return Res.ok(socialLoginService.queryLoginScanStatus(ticket, request, response));
    }

    /// 取消扫码登录(公开: 手机端确认页取消按钮, 票据即凭证)
    /// 调用方是 H5 发起页: 参数以 JSON body 提交(同 exchange-login-scan);
    /// 票据不存在或已不可取消时空操作, 不暴露票据状态.
    @Operation(summary = "取消扫码登录")
    @PostMapping("/login-scan/cancel")
    public Result<Void> loginScanCancel(@RequestBody SocialLoginScanCancelParam param) {
        socialLoginService.cancelLoginScan(param.getTicket());
        return Res.ok();
    }

    /// 解除当前登录用户的指定平台绑定
    @IgnoreAuth(login = true)
    @Operation(summary = "解除第三方账号绑定")
    @PostMapping("/unbind")
    public Result<Void> unbind(String source) {
        socialLoginService.unbind(SecurityUtil.getUserId(), source);
        return Res.ok();
    }
}
