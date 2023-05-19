package cn.bootx.platform.daxpay.core.merchant.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.core.merchant.dao.MchAppPayConfigManager;
import cn.bootx.platform.daxpay.core.merchant.entity.MchAppPayConfig;
import cn.bootx.platform.daxpay.dto.merchant.MchAppPayConfigDto;
import cn.bootx.platform.daxpay.param.merchant.MchAppPayConfigParam;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商户应用支付配置
 * @author xxm
 * @date 2023-05-19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MchAppPayConfigService {
    private final MchAppPayConfigManager mchAppPayConfigManager;

    /**
     * 添加
     */
    public void add(MchAppPayConfigParam param){
        MchAppPayConfig mchAppPayConfig = MchAppPayConfig.init(param);
        mchAppPayConfigManager.save(mchAppPayConfig);
    }

    /**
     * 修改
     */
    public void update(MchAppPayConfigParam param){
        MchAppPayConfig mchAppPayConfig = mchAppPayConfigManager.findById(param.getId()).orElseThrow(DataNotExistException::new);

        BeanUtil.copyProperties(param,mchAppPayConfig, CopyOptions.create().ignoreNullValue());
        mchAppPayConfigManager.updateById(mchAppPayConfig);
    }

    /**
     * 分页
     */
    public PageResult<MchAppPayConfigDto> page(PageParam pageParam,MchAppPayConfigParam mchAppPayConfigParam){
        return MpUtil.convert2DtoPageResult(mchAppPayConfigManager.page(pageParam,mchAppPayConfigParam));
    }

    /**
     * 获取单条
     */
    public MchAppPayConfigDto findById(Long id){
        return mchAppPayConfigManager.findById(id).map(MchAppPayConfig::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 获取全部
     */
    public List<MchAppPayConfigDto> findAll(){
        return ResultConvertUtil.dtoListConvert(mchAppPayConfigManager.findAll());
    }

    /**
     * 删除
     */
    public void delete(Long id){
        mchAppPayConfigManager.deleteById(id);
    }
}
