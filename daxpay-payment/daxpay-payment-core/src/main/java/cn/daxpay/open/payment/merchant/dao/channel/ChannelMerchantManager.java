package cn.daxpay.open.payment.merchant.dao.channel;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.merchant.param.channel.ChannelMerchantQuery;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/// # 通道商户管理
///
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelMerchantManager extends BaseManager<ChannelMerchantMapper, ChannelMerchant> {

    /// 分页
    public Page<ChannelMerchant> page(PageParam pageParam, ChannelMerchantQuery query){
        Page<ChannelMerchant> mpPage = MpUtil.getMpPage(pageParam, ChannelMerchant.class);
        QueryWrapper<ChannelMerchant> generator = QueryGenerator.generator(query);
        return this.page(mpPage,generator);
    }

    /// 判断商户是否拥有该通道商户
    public boolean checkMchHasChannel(String mchNo, String channelMchNo, String product){
        return this.lambdaQuery()
                .eq(ChannelMerchant::getMchNo, mchNo)
                .eq(ChannelMerchant::getChannelMchNo, channelMchNo)
                .eq(ChannelMerchant::getProduct, product)
                .exists();
    }

    /// 根据商户号和支付产品下拉
    public List<ChannelMerchant> findAllByMchNoAndProduct(String mchNo, String product){
        return this.lambdaQuery()
                .eq(ChannelMerchant::getMchNo, mchNo)
                .eq(ChannelMerchant::getProduct, product)
                .list();
    }

    /// 根据商户号查询所有通道商户
    public List<ChannelMerchant> findAllByMchNo(String mchNo){
        return this.lambdaQuery()
                .eq(ChannelMerchant::getMchNo, mchNo)
                .list();
    }

    /// 根据商户号与通道商户号查询唯一通道商户(不存在返回 empty)
    public Optional<ChannelMerchant> findByMchNoAndChannelMchNo(String mchNo, String channelMchNo){
        return this.lambdaQuery()
                .eq(ChannelMerchant::getMchNo, mchNo)
                .eq(ChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 根据通道商户号查询唯一通道商户(通道商户号为系统生成号, 全局唯一, 不存在返回 empty)
    ///
    /// 缓存说明: 运行时路由高频调用, 配置态低频修改。写侧通过 @CacheEvict 失效。
    /// @Cacheable 不缓存 null(由 RedisCacheConfiguration.disableCachingNullValues 保证),
    /// 但 Optional.empty() 非 null 可正常缓存。
    @Cacheable(value = "payment:channel-mch", key = "#channelMchNo")
    public Optional<ChannelMerchant> findByChannelMchNo(String channelMchNo){
        return this.lambdaQuery()
                .eq(ChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 根据通道商户号查询唯一通道商户(认证引导场景, 忽略租户隔离)
    ///
    /// 用于 OAuth 回调等无 mchNo 上下文的引导阶段(微信直连/服务商扫码认证),
    /// channelMchNo 为系统生成全局唯一号, 可独立定位行。须经 Spring 注入的 Bean 外部调用
    /// (同类 this 自调用不走 AOP)。配置态/已装载上下文的场景请用 [#findByChannelMchNo]。
    @IgnoreTenant
    public Optional<ChannelMerchant> findByChannelMchNoNotTenant(String channelMchNo) {
        return findByChannelMchNo(channelMchNo);
    }

    /// 通道商户号 → 产品编码；不存在返回 null（配置单条候选/反推用，仅单次路径）
    public String findProductByChannelMchNo(String channelMchNo) {
        if (StrUtil.isBlank(channelMchNo)) {
            return null;
        }
        return findByChannelMchNo(channelMchNo)
                .map(ChannelMerchant::getProduct)
                .orElse(null);
    }

    /// 通道商户号 → 产品编码；空白或不存在抛业务异常（运行时路由用，仅单次路径）
    public String requireProductByChannelMchNo(String channelMchNo) {
        if (StrUtil.isBlank(channelMchNo)) {
            // 路由: 通道商户号不能为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.channelMchNoRequired");
        }
        return findByChannelMchNo(channelMchNo)
                .map(ChannelMerchant::getProduct)
                // 路由: 通道商户不存在
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.route.error.channelMchNotExist", channelMchNo));
    }
}
