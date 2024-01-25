package cn.bootx.platform.daxpay.gateway.controller;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WeChatPayConfigService;
import cn.bootx.platform.daxpay.service.core.timeout.task.PayExpiredTimeTask;
import cn.bootx.platform.daxpay.service.core.timeout.task.PayWaitOrderSyncTask;
import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApiConfigKit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 测试
 * @author xxm
 * @since 2024/1/5
 */
@Tag(name = "测试")
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final PayExpiredTimeTask expiredTimeTask;;
    private final PayWaitOrderSyncTask waitOrderSyncTask;
    private final WeChatPayConfigService weChatPayConfigService;
    private final LockTemplate lockTemplate;

    @Operation(summary = "同步")
    @GetMapping("/sync")
    public ResResult<Void> sync(){
        waitOrderSyncTask.task();
        return Res.ok();
    }
    @Operation(summary = "超时")
    @GetMapping("/expired")
    public ResResult<Void> expired(){
        expiredTimeTask.task();
        return Res.ok();
    }

    @Operation(summary = "锁测试1")
    @GetMapping("/lock1")
//    @Lock4j(keys = "#name", acquireTimeout = 50)
    public ResResult<String> lock1(String name){
        LockInfo lock = lockTemplate.lock(name, 10000, 10);
        if (Objects.isNull(lock)){
            throw new BizException("未获取到锁");
        }
        System.out.println("进来了......");
        ThreadUtil.sleep(10000);
        lockTemplate.releaseLock(lock);
        return Res.ok(name);
    }


    @Operation(summary = "锁测试2")
    @GetMapping("/lock2")
//    @Lock4j(keys = "#name", acquireTimeout = 50)
    public ResResult<String> lock2(String name){
        return Res.ok(name);
    }

    @Operation(summary = "解析微信退款信息")
    @GetMapping("/wxjx")
    public ResResult<Void> wxjx(){
        WeChatPayConfig config = weChatPayConfigService.getConfig();
        String reqInfo = "5WYGjdyCumTyTutbIRILrluT7ahm/8puDs+82ARE3BYaELjmCQ2nsjpM68nQBUHj1x1a0o/5h5k4VI7hz6iEHtBwNwuxIqxesVDX6WtAcBeIR0fACDT+NxefEj1BPFGJqueyA2XxKLuhK8fvO3qiowHcNu3N0QJxSvL5V85wQ3oMnE90VUG8XVTjGBsS6ARaRafyfeHfIyH6ZfH0CBrZlpvM8hzyN1S4T+aNojAaiAQqzgDUGC/TLYgvovgzRmfkRH/dnuxHh4gzh53SqEDSMy1wp/xXQKrbJGjolAbVCVaQ1AHGa4/KZeA6UV7uUYNhGqFDHEqQ1Nqc8lJfcBg9VRMWwJtgCfx3lEilj7LJ/FPRRQKqXUZgcQsKvd87lAU5vQMeYho6mP65nazMLN5sABbAvNNzKMtXkNbL7PxPXy5+FMdj/NDiVCWvCToPpnBfK66rwvdEF4BftEoHYUAnp+WiHnVuposTwkSxQT6vDAkfui2pFOi/PGutHcfeJndUYqLgJgTMTUAApUCUIwMy+3dSeO3kcMT/x4KAPCNIEWE0qfxQOke7aaN3qFqT1p9kkWzlPFrC4FNnCpCMaJktbNQOxAqYHNAp6YHCRGIOmwJ9Rej005G2s+RjyPY3/zBE0VueghtaejszMf4OcrNGCNnXM9/6u/tu3ZeIv8vJnFfgc70GpyrHfgTjupr/cxDqAgfgJlk6aQIRtaAM8lu19zl+s2RP3oHL50G7gNOdtw7pP5shHcLhaf2OQc7IRbmOELGtjh+7fZrrwK6U6/te9JAJcNEY3RdXQ2KgNIiuvAoKHaMXEFL4D+n3wW3AHQVWze2lBZIHcCpTCTwdjjCEWTNFOEzKuzFejJSJOQqQ6gHJ2JrqTuhcE6jetLTtk51LK1C50kMjwt+bIgXbceE7csJSgcMo8/g3w4LmakSNX1kdRI6p2EesM+W/2CKLAKga8TN78Y/v6BhbbdjubpeUtN8+JjM3Wr66I2FeXWxW2Fuh0Enb73rGfrnvhrFVM3L1NeA9Fp1I3nAmWyhULYUZdaiyu8dHjfV3g5HzmOl44iEDMZ14o44QCecjNhtRUtpJLABwP3fXYoNBI7iLQ1alfKvfukh1dzX6gAvbNxTn0jM=";
        String decryptData = WxPayKit.decryptData(reqInfo, WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey());
        return Res.ok();
    }

}
