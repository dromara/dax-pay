package cn.bootx.platform.starter.wechat.core.login.service;

import cn.bootx.platform.starter.auth.exception.LoginFailureException;
import cn.bootx.platform.common.redis.RedisClient;
import cn.bootx.platform.starter.wechat.dto.login.WeChatLoginQrCode;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpQrcodeService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static cn.bootx.platform.starter.wechat.code.WeChatCode.*;

/**
 * 扫码事件
 *
 * @author xxm
 * @since 2022/8/4
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatQrLoginService {

    private final RedisClient redisClient;

    private final WxMpService wxMpService;

    private final String PREFIX_KEY = "third:wechat:login:qr:";

    /**
     * 申请待扫描的二维码
     */
    @SneakyThrows
    public WeChatLoginQrCode applyQrCode() {
        WxMpQrcodeService qrcodeService = wxMpService.getQrcodeService();
        long timeout = 5 * 60 * 1000;
        String qrCodeKey = IdUtil.getSnowflakeNextIdStr();
        // 创建微信临时Key, 需要有指定的前缀
        String wxKey = QRSCENE_LOGIN + qrCodeKey;
        WxMpQrCodeTicket wxMpQrCodeTicket = qrcodeService.qrCodeCreateTmpTicket(wxKey, (int) timeout);
        String url = wxMpQrCodeTicket.getUrl();
        redisClient.setWithTimeout(PREFIX_KEY + qrCodeKey, "", timeout);
        return new WeChatLoginQrCode(qrCodeKey, url);
    }

    /**
     * 查询二维码状态 等待扫码/登录成功/过期
     */
    public String getStatus(String key) {
        String openId = redisClient.get(PREFIX_KEY + key);
        // 超时
        if (Objects.isNull(openId)) {
            return QR_LOGIN_EXPIRED;
        }
        // 等待
        else if (StrUtil.isBlank(openId)) {
            return QR_LOGIN_WAIT;
        }
        // 登录成功
        else {
            return QR_LOGIN_OK;
        }
    }

    /**
     * 设置微信openId
     */
    public void setOpenId(String key, String openId) {
        if (redisClient.exists(PREFIX_KEY + key)) {
            redisClient.set(PREFIX_KEY + key, openId);
        }
    }

    /**
     * 获取 openId
     */
    public String getOpenId(String key) {
        String openId = redisClient.get(PREFIX_KEY + key);
        if (StrUtil.isBlank(openId)) {
            throw new LoginFailureException("数据已过期或不存在");
        }
        return openId;
    }

    /**
     * 清除扫码信息
     */
    public void clear(String key) {
        redisClient.deleteKey(PREFIX_KEY + key);
    }

}
