package cn.daxpay.open.payment.merchant.service.channel;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.merchant.param.channel.ChannelMerchantEditParam;
import cn.daxpay.open.payment.merchant.param.channel.ChannelMerchantQuery;
import cn.daxpay.open.payment.merchant.result.channel.ChannelMerchantResult;
import cn.daxpay.open.payment.masterdata.service.channel.PayChannelService;
import cn.daxpay.open.payment.masterdata.result.channel.PayChannelResult;
import cn.daxpay.open.payment.masterdata.dao.product.PayProductConfigManager;
import cn.daxpay.open.payment.masterdata.dao.product.PayProductManager;
import cn.daxpay.open.payment.masterdata.entity.product.PayProduct;
import cn.daxpay.open.platform.core.enums.pay.config.PayEnvEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/// # 通道商户管理
///
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelMerchantService {
    private final ChannelMerchantManager channelMerchantManager;
    private final TransService transService;
    private final PayChannelService payChannelService;
    private final PayProductManager payProductManager;
    private final PayProductConfigManager payProductConfigManager;

    /// 分页
    public PageResult<ChannelMerchantResult> page(PageParam pageParam, ChannelMerchantQuery query){
        PageResult<ChannelMerchantResult> pageResult = MpUtil.toPageResult(channelMerchantManager.page(pageParam,query));
        fillEnvStatus(pageResult.getRecords());
        // 翻译商户名称(mchNo -> mchName, 走系统 @Trans 机制)
        transService.translate(pageResult);
        return pageResult;
    }

    /// 查询详情
    public ChannelMerchantResult findById(Long id){
        ChannelMerchantResult result = channelMerchantManager.findById(id)
                .map(ChannelMerchant::toResult)
                // 通道: 通道商户不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        fillEnvStatus(List.of(result));
        // 翻译商户名称
        transService.translate(result);
        return result;
    }

    /// 根据商户号查询通道（开源版不按商户权限过滤，返回全部通道；mchNo 保留以兼容接口）
    public List<PayChannelResult> dropdownByMchNo(String mchNo) {
        return payChannelService.listAll();
    }

    /// 编辑
    public void update(ChannelMerchantEditParam param){
        // 通道: 通道商户不存在
        var mchInfo = channelMerchantManager.findById(param.getId()).orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        mchInfo.setChannelMerchantName(param.getChannelMerchantName());
        channelMerchantManager.updateById(mchInfo);
    }

    /// 删除
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id){
        // 通道: 通道商户不存在
        var mchInfo = channelMerchantManager.findById(id).orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        channelMerchantManager.deleteById(id);
    }

    /// 根据商户和支付产品查询通道商户号列表, 多数支付通道配置使用
    public List<LabelValue> dropdown(String mchNo, String product){
        return channelMerchantManager.findAllByMchNoAndProduct(mchNo, product).stream()
                .map(mch -> new LabelValue(mch.getChannelMerchantName(), mch.getChannelMchNo()))
                .toList();
    }

    /// 根据商户号查询所有通道商户
    public List<ChannelMerchantResult> findAllByMchNo(String mchNo){
        List<ChannelMerchantResult> results = channelMerchantManager.findAllByMchNo(mchNo).stream()
                .map(ChannelMerchant::toResult)
                .toList();
        fillEnvStatus(results);
        return results;
    }

    /// 批量填充生效环境与沙箱支持标志
    ///
    /// - sandbox: 直接读实体固化的字段(创建时按当时产品 activeEnv 写入, 之后不再随产品切换改变)
    /// - activeEnv: 当前产品生效环境(决定该商户是否参与路由), 来自 pay_md_product_config
    /// - sandboxSupport: 该产品是否支持沙箱(决定前端是否显示环境标签), 来自 pay_md_product
    private void fillEnvStatus(List<ChannelMerchantResult> results) {
        if (results.isEmpty()) {
            return;
        }
        Set<String> products = results.stream()
                .map(ChannelMerchantResult::getProduct)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (products.isEmpty()) {
            return;
        }
        // 沙箱支持标志(来自支付产品表, 决定前端是否显示环境标签)
        Map<String, Boolean> sandboxMap = payProductManager.lambdaQuery()
                .in(PayProduct::getCode, products)
                .list()
                .stream()
                .collect(Collectors.toMap(PayProduct::getCode, p -> Boolean.TRUE.equals(p.getSandbox()), (a, b) -> a));
        // 产品当前生效环境(决定路由目标; 不再回写通道商户 sandbox)
        Map<String, String> activeEnvMap = payProductConfigManager.mapActiveEnvByProducts(products);
        results.forEach(r -> {
            String activeEnv = activeEnvMap.getOrDefault(r.getProduct(), PayEnvEnum.PROD.getCode());
            r.setActiveEnv(activeEnv);
            // sandbox 字段为创建时固化的快照, 直接读实体, 不再用产品 activeEnv 覆盖
            r.setSandboxSupport(sandboxMap.getOrDefault(r.getProduct(), false));
        });
    }

    /// 更新启用状态
    public void updateEnable(Long id, Boolean enable){
        // 通道: 通道商户不存在
        var mchInfo = channelMerchantManager.findById(id).orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        mchInfo.setEnable(enable);
        channelMerchantManager.updateById(mchInfo);
    }

}
