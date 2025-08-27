package org.dromara.daxpay.service.isv.service.gateway;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.core.exception.OperationFailException;
import org.dromara.daxpay.service.isv.dao.gateway.IsvAggregateBarPayConfigManager;
import org.dromara.daxpay.service.isv.dao.gateway.IsvAggregatePayConfigManager;
import org.dromara.daxpay.service.isv.entity.gateway.IsvAggregateBarPayConfig;
import org.dromara.daxpay.service.isv.entity.gateway.IsvAggregatePayConfig;
import org.dromara.daxpay.service.isv.param.gateway.IsvAggregateBarPayConfigParam;
import org.dromara.daxpay.service.isv.param.gateway.IsvAggregatePayConfigParam;
import org.dromara.daxpay.service.isv.result.gateway.IsvAggregateBarPayConfigResult;
import org.dromara.daxpay.service.isv.result.gateway.IsvAggregatePayConfigResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 聚合支付配置
 * @author xxm
 * @since 2025/3/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IsvAggregateConfigService {
    private final IsvAggregatePayConfigManager aggregatePayConfigManager;
    private final IsvAggregateBarPayConfigManager aggregateBarPayConfigManager;

    /**
     * 支付配置列表
     */
    public List<IsvAggregatePayConfigResult> listByPay(){
        return MpUtil.toListResult(aggregatePayConfigManager.findAll());
    }

    /**
     * 付款码配置列表
     */
    public List<IsvAggregateBarPayConfigResult> listByBar(){
        return MpUtil.toListResult(aggregateBarPayConfigManager.findAll());
    }

    /**
     * 支付配置详情
     */
    public IsvAggregatePayConfigResult findPayById(Long id){
        return aggregatePayConfigManager.findById(id).map(IsvAggregatePayConfig::toResult).orElseThrow(() -> new DataNotExistException("聚合支付配置不存在"));
    }

    /**
     * 付款码配置详情
     */
    public IsvAggregateBarPayConfigResult findBarById(Long id){
        return aggregateBarPayConfigManager.findById(id).map(IsvAggregateBarPayConfig::toResult).orElseThrow(() -> new DataNotExistException("聚合付款码支付不存在"));
    }

    /**
     *保存聚合支付配置
     */
    public void savePay(IsvAggregatePayConfigParam param){
        if (aggregatePayConfigManager.existsByType(param.getAggregateType())){
            throw new OperationFailException("聚合支付配置已存在");
        }
        var entity = IsvAggregatePayConfig.init(param);
        aggregatePayConfigManager.save(entity);
    }

    /**
     *保存聚合付款码支付配置
     */
    public void saveBar(IsvAggregateBarPayConfigParam param){
        if (aggregateBarPayConfigManager.existsByType(param.getBarPayType())){
            throw new OperationFailException("聚合付款码支付配置已存在");
        }
        aggregateBarPayConfigManager.save(IsvAggregateBarPayConfig.init(param));
    }

    /**
     *更新聚合支付配置
     */
    public void updatePay(IsvAggregatePayConfigParam param){
        var payConfig = aggregatePayConfigManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("聚合支付配置不存在"));
        if (!Objects.equals(param.getAggregateType(), payConfig.getAggregateType()) && aggregatePayConfigManager.existsByType(param.getAggregateType())){
            throw new OperationFailException("聚合支付类型已存在");
        }
        BeanUtil.copyProperties(param, payConfig, CopyOptions.create().ignoreNullValue());
        aggregatePayConfigManager.updateById(payConfig);
    }

    /**
     *更新聚合付款码支付配置
     */
    public void updateBar(IsvAggregateBarPayConfigParam param){
        var barPayConfig = aggregateBarPayConfigManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("聚合付款码支付配置不存在"));
        if (!Objects.equals(param.getBarPayType(), barPayConfig.getBarPayType()) && aggregateBarPayConfigManager.existsByType(param.getBarPayType())){
            throw new OperationFailException("聚合付款码支付类型已存在");
        }
        BeanUtil.copyProperties(param, barPayConfig, CopyOptions.create().ignoreNullValue());
        aggregateBarPayConfigManager.updateById(barPayConfig);
    }

    /**
     *删除聚合支付配置
     */
    public void deletePay(Long id){
        aggregatePayConfigManager.deleteById(id);
    }

    /**
     *删除聚合付款码支付配置
     */
    public void deleteBar(Long id){
        aggregateBarPayConfigManager.deleteById(id);
    }

    /**
     * 根据应用ID和类型查询支付配置是否存在
     */
    public boolean existsPay(String type){
        return aggregatePayConfigManager.existsByType(type);
    }

    /**
     * 根据应用ID和类型查询支付配置是否存在
     */
    public boolean existsPay(String type, Long id){
        return aggregatePayConfigManager.existsByType(type, id);
    }

    /**
     * 根据应用ID和类型查询付款码支付配置是否存在
     */
    public boolean existsBar(String type){
        return aggregateBarPayConfigManager.existsByType( type);
    }

    /**
     * 根据应用ID和类型查询付款码支付配置是否存在
     */
    public boolean existsBar(String type, Long id){
        return aggregateBarPayConfigManager.existsByType( type, id);
    }

}
