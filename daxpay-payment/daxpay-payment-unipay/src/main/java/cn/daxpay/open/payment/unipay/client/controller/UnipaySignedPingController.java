package cn.daxpay.open.payment.unipay.client.controller;

import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.common.util.DaxRes;
import cn.daxpay.open.payment.common.util.PaySignUtil;
import cn.daxpay.open.payment.unipay.aop.PaymentVerify;
import cn.daxpay.open.payment.unipay.param.assist.UnipayPingParam;
import cn.daxpay.open.payment.unipay.result.assist.UnipayPingResult;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 签名自检探针
///
/// 对接自检专用端点 `POST /unipay/ping`: 与免签名的部署自检探针([UnipayPingController])互补,
/// 本端点走完整 [PaymentVerify] 链路(商户身份 → 验签 → 防重放), 供**持商户私钥的对接方**
/// (SDK/联调工具)一键判断「当前配置的商户号/应用/私钥/签名串构造」是否正确、能否发起真实调用。
///
/// 与部署自检探针的区别:
/// - 免签名探针(`GET /unipay/callback/ping`)回答「公网链路是否可达」, 面板网关放行「通道回调」组即可通;
/// - 本探针回答「签名链路是否可用」, 落在 `/unipay` 前缀(「商户开放 API」放行组), 与真实业务接口同组同语义。
///
/// 密码学约束: 验签用商户公钥(商户上传), 平台不持有商户私钥, 故本检查只能由持私钥方发起,
/// 平台自身无法单方面自证签名通过。
///
/// 零业务副作用: 不建单/不改状态; 调用正常进入接口审计。请勿当高频健康检查使用(消耗 nonce 防重放槽位)。
@PaymentVerify
@IgnoreAuth
@Tag(name = "签名自检探针")
@RestController
@RequestMapping("/unipay")
@RequiredArgsConstructor
public class UnipaySignedPingController {

    private final MerchantContextLoader merchantContextLoader;

    /// 签名自检探针
    ///
    /// 验签通过后解析应用(存在/启用/归属校验)并回显, 响应携带平台签名(对接方可顺带验证平台公钥配置)。
    @Operation(summary = "签名自检探针")
    @PostMapping("/ping")
    public DaxResult<UnipayPingResult> ping(@RequestBody UnipayPingParam param) {
        // 应用解析: appId 空则取默认应用, 校验启用与商户匹配(失败分类提示, 便于排查)
        boolean appFromDefault = StrUtil.isBlank(param.getAppId());
        var app = merchantContextLoader.resolveApp(param.getMchNo(), param.getAppId());
        UnipayPingResult result = new UnipayPingResult()
                .setMchNo(param.getMchNo())
                .setAppId(app.getAppId())
                .setAppFromDefault(appFromDefault)
                // 服务端待签串与验签时计算的是同一份, 成功响应回显供双方确认签名串构造一致
                .setServerSignStr(PaySignUtil.buildSignStr(param));
        return DaxRes.ok(result);
    }
}
