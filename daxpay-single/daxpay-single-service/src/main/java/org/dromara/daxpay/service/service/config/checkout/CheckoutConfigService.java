package org.dromara.daxpay.service.service.config.checkout;

import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.exception.RepetitiveOperationException;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.exception.OperationFailException;
import org.dromara.daxpay.service.dao.config.checkout.CheckoutAggregateConfigManager;
import org.dromara.daxpay.service.dao.config.checkout.CheckoutConfigManager;
import org.dromara.daxpay.service.dao.config.checkout.CheckoutGroupConfigManager;
import org.dromara.daxpay.service.dao.config.checkout.CheckoutItemConfigManager;
import org.dromara.daxpay.service.entity.config.checkout.CheckoutAggregateConfig;
import org.dromara.daxpay.service.entity.config.checkout.CheckoutConfig;
import org.dromara.daxpay.service.entity.config.checkout.CheckoutGroupConfig;
import org.dromara.daxpay.service.entity.config.checkout.CheckoutItemConfig;
import org.dromara.daxpay.service.param.config.checkout.CheckoutAggregateConfigParam;
import org.dromara.daxpay.service.param.config.checkout.CheckoutConfigParam;
import org.dromara.daxpay.service.param.config.checkout.CheckoutGroupConfigParam;
import org.dromara.daxpay.service.param.config.checkout.CheckoutItemConfigParam;
import org.springframework.stereotype.Service;

/**
 * 收银台配置服务
 * @author xxm
 * @since 2024/11/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CheckoutConfigService {

    private final CheckoutConfigManager checkoutConfigManager;
    private final CheckoutGroupConfigManager checkoutGroupConfigManager;
    private final CheckoutItemConfigManager checkoutItemConfigManager;
    private final CheckoutAggregateConfigManager checkoutAggregateConfigManager;

    /**
     * 保存收银台配置
     */
    public void saveCheckoutConfig(CheckoutConfigParam param) {
        // 判断是否已经存在
        if (checkoutConfigManager.existsByAppId(param.getAppId())){
            throw new RepetitiveOperationException("该应用已存在收银台配置");
        }
        checkoutConfigManager.save(CheckoutConfig.init(param));
    }

    /**
     * 更新收银台配置
     */
    public void updateCheckoutConfig(CheckoutConfigParam param) {
        CheckoutConfig checkoutConfig = checkoutConfigManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("收银台配置不存在"));
        BeanUtil.copyProperties(param, checkoutConfig, CopyOptions.create().ignoreNullValue());
        checkoutConfigManager.updateById(checkoutConfig);
    }

    /**
     * 保存分组配置
     */
    public void saveCheckoutGroupConfig(CheckoutGroupConfigParam param) {
        checkoutGroupConfigManager.save(CheckoutGroupConfig.init(param));
    }

    /**
     * 更新分组配置
     */
    public void updateCheckoutGroupConfig(CheckoutGroupConfigParam param) {
        CheckoutGroupConfig checkoutGroupConfig = checkoutGroupConfigManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("分组配置不存在"));
        BeanUtil.copyProperties(param, checkoutGroupConfig, CopyOptions.create().ignoreNullValue());
        checkoutGroupConfigManager.updateById(checkoutGroupConfig);
    }

    /**
     * 删除分组配置
     */
    public void deleteCheckoutGroupConfig(Long id) {
        if (!checkoutGroupConfigManager.existedById(id)){
            throw new DataNotExistException("分组配置不存在");
        }

        if (checkoutItemConfigManager.existedByGroupId(id)){
            throw new OperationFailException("该分组下存在配置项，请先删除配置项");
        }
        checkoutGroupConfigManager.deleteById(id);
    }

    /**
     * 保存配置项
     */
    public void saveCheckoutItemConfig(CheckoutItemConfigParam param) {
        if (!checkoutGroupConfigManager.existedById(param.getGroupId())){
            throw new DataNotExistException("所属分组配置不存在");
        }
        checkoutItemConfigManager.save(CheckoutItemConfig.init(param));

    }

    /**
     * 更新配置项
     */
    public void updateCheckoutItemConfig(CheckoutItemConfigParam param) {
        CheckoutItemConfig checkoutItemConfig = checkoutItemConfigManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("配置项不存在"));
        BeanUtil.copyProperties(param, checkoutItemConfig, CopyOptions.create().ignoreNullValue());
        checkoutItemConfigManager.updateById(checkoutItemConfig);
    }

    /**
     * 删除配置项
     */
    public void deleteCheckoutItemConfig(Long id) {
        if (!checkoutItemConfigManager.existedById(id)){
            throw new DataNotExistException("配置项不存在");
        }
        checkoutItemConfigManager.deleteById(id);
    }

    /**
     * 新增聚合配置
     */
    public void saveCheckoutAggregateConfig(CheckoutAggregateConfigParam param) {
        // 判断支付类型是否已经存在
        if (checkoutAggregateConfigManager.existsByAppIdAndType(param.getAppId(), param.getType())){
            throw new RepetitiveOperationException("该应用已存在该聚合支付类型配置");
        }
        checkoutAggregateConfigManager.save(CheckoutAggregateConfig.init(param));
    }
    /**
     * 更新聚合配置
     */
    public void updateCheckoutAggregateConfig(CheckoutAggregateConfigParam param) {
        // 判断支付类型是否已经存在
        if (checkoutAggregateConfigManager.existsByAppIdAndType(param.getAppId(), param.getType(), param.getId())){
            throw new RepetitiveOperationException("该应用已存在该聚合支付类型配置");
        }
        CheckoutAggregateConfig checkoutAggregateConfig = checkoutAggregateConfigManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("聚合支付配置不存在"));
        BeanUtil.copyProperties(param, checkoutAggregateConfig, CopyOptions.create().ignoreNullValue());
        checkoutAggregateConfigManager.updateById(checkoutAggregateConfig);
    }

    /**
     * 删除聚合配置
     */
    public void deleteCheckoutAggregateConfig(Long id) {
        if (!checkoutAggregateConfigManager.existedById(id)){
            throw new DataNotExistException("聚合支付配置不存在");
        }
        checkoutAggregateConfigManager.deleteById(id);
    }
}
