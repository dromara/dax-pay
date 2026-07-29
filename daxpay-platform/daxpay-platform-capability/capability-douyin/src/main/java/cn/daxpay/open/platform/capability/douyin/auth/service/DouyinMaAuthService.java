package cn.daxpay.open.platform.capability.douyin.auth.service;

import cn.daxpay.open.platform.capability.douyin.auth.result.DouyinAuthResult;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 抖音小程序认证服务
///
/// 封装抖音小程序场景的 jscode2session 调用, 与 H5 场景([DouyinH5AuthService]) 不同:
/// - H5/网站应用: 走 OAuth `/oauth/access_token/`(form-urlencoded)
/// - 小程序: 走 `/api/apps/v2/jscode2session`(JSON body)
///
/// 两者底层接口不同, 因此独立成类, 不混用 [DouyinH5AuthService]。
///
/// 参考文档: https://developer.open-douyin.com/docs/resource/zh-CN/mini-app/develop/server/log-in/code-2-session
@Slf4j
@Service
public class DouyinMaAuthService {

    /// 抖音小程序 code2session 地址(v2 接口)
    private static final String JSCODE2SESSION_URL = "https://developer.toutiao.com/api/apps/v2/jscode2session";

    /// 通过授权码换取用户 openId
    ///
    /// 调用抖音小程序 `jscode2session` 接口(POST JSON)。
    /// 官方要求 `code` 与 `anonymous_code` 为**分字段**, 至少传一个:
    /// - 非匿名登录: 传 `code`(必有 openid); 同时有 `anonymous_code` 时一并传用于数据同步
    /// - 匿名登录: 仅传 `anonymous_code`(返回 anonymous_openid)
    ///
    /// 响应优先取 `openid`, 其次 `anonymous_openid`。
    ///
    /// @param appId          抖音小程序 AppId(即开放平台 clientKey)
    /// @param appSecret      抖音小程序 AppSecret(即开放平台 clientSecret)
    /// @param code           tt.login 返回的登录凭证(可空)
    /// @param anonymousCode  tt.login 返回的匿名登录凭证(可空)
    /// @return 授权结果(openId)
    public DouyinAuthResult getOpenId(String appId, String appSecret, String code, String anonymousCode) {
        if (StrUtil.isAllBlank(code, anonymousCode)) {
            // 抖音: 换取用户标识失败: {0}
            throw new OperationFailException("error.douyin.authFailed", "code and anonymous_code are blank");
        }
        JSONObject requestBody = new JSONObject();
        requestBody.set("appid", appId);
        requestBody.set("secret", appSecret);
        // 官方分字段: 有值才写入, 禁止把 anonymous_code 塞进 code
        if (StrUtil.isNotBlank(code)) {
            requestBody.set("code", code);
        }
        if (StrUtil.isNotBlank(anonymousCode)) {
            requestBody.set("anonymous_code", anonymousCode);
        }

        String body;
        try (HttpResponse response = HttpUtil.createPost(JSCODE2SESSION_URL)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .execute()) {
            body = response.body();
        }
        log.info("抖音小程序 jscode2session 响应: {}", body);

        JSONObject object = JSONUtil.parseObj(body);
        // v2 接口: err_no 为 0 表示成功
        int errNo = object.getInt("err_no", -1);
        if (errNo != 0) {
            // 抖音: 换取用户标识失败: {0}
            throw new OperationFailException("error.douyin.authFailed",
                    object.getStr("err_tips", body));
        }
        JSONObject data = object.getJSONObject("data");
        if (data == null) {
            // 抖音: 换取用户标识失败: {0}
            throw new OperationFailException("error.douyin.authFailed", "data is null");
        }
        // 优先 openid(用户授权), 其次 anonymous_openid(静默)
        String openId = StrUtil.blankToDefault(data.getStr("openid"), data.getStr("anonymous_openid"));
        if (StrUtil.isBlank(openId)) {
            // 抖音: 获取用户标识失败
            throw new OperationFailException("error.douyin.authFailed", "openId is blank");
        }
        return new DouyinAuthResult()
                .setOpenId(openId);
    }
}
