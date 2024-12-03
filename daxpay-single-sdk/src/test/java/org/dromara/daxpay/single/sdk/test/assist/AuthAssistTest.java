package org.dromara.daxpay.single.sdk.test.assist;

import org.dromara.daxpay.single.sdk.code.ChannelEnum;
import org.dromara.daxpay.single.sdk.code.SignTypeEnum;
import org.dromara.daxpay.single.sdk.model.assist.AuthModel;
import org.dromara.daxpay.single.sdk.model.assist.AuthUrlModel;
import org.dromara.daxpay.single.sdk.net.DaxPayConfig;
import org.dromara.daxpay.single.sdk.net.DaxPayKit;
import org.dromara.daxpay.single.sdk.param.assist.AuthAndSetParam;
import org.dromara.daxpay.single.sdk.param.assist.AuthCodeParam;
import org.dromara.daxpay.single.sdk.param.assist.GenerateAuthUrlParam;
import org.dromara.daxpay.single.sdk.response.DaxPayResult;
import org.dromara.daxpay.single.sdk.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 认证服务测试类
 * @author xxm
 * @since 2024/12/3
 */
public class AuthAssistTest {
    @Before
    public void init() {
        // 初始化支付配置
        DaxPayConfig config = DaxPayConfig.builder()
                .serviceUrl("http://127.0.0.1:9999")
                .signSecret("123456")
                .appId("M7934041241299655")
                .signType(SignTypeEnum.HMAC_SHA256)
                .build();
        DaxPayKit.initConfig(config);
    }

    /**
     * 获取认证链接
     */
    @Test
    public void generateAuthUrl(){
        GenerateAuthUrlParam param = new GenerateAuthUrlParam();
        param.setChannel(ChannelEnum.WECHAT.getCode());
        param.setClientIp("127.0.0.1");
        DaxPayResult<AuthUrlModel> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }

    /**
     * 获取认证信息
     */
    @Test
    public void auth(){
        AuthCodeParam param = new AuthCodeParam();
        param.setChannel(ChannelEnum.WECHAT.getCode());
        param.setAuthCode("auth");
        param.setClientIp("127.0.0.1");
        DaxPayResult<AuthModel> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }

    /**
     * 设置认证信息. 不需要签名
     */
    @Test
    public void authAndSet(){
        AuthAndSetParam param = new AuthAndSetParam();
        param.setChannel(ChannelEnum.WECHAT.getCode());
        param.setAuthCode("auth");
        param.setQueryCode("query13585312378555");
        param.setClientIp("127.0.0.1");
        DaxPayResult<Void> execute = DaxPayKit.execute(param,false);
        System.out.println(JsonUtil.toJsonStr(execute));
    }


}
