package cn.bootx.platform.daxpay.core.merchant.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.core.merchant.dao.MchApplicationManager;
import cn.bootx.platform.daxpay.core.merchant.entity.MchApplication;
import cn.bootx.platform.daxpay.dto.merchant.MchApplicationDto;
import cn.bootx.platform.daxpay.param.merchant.MchApplicationParam;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商户应用
 * @author xxm
 * @date 2023-05-19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MchApplicationService {
    private final MchApplicationManager mchApplicationManager;

    /**
     * 添加
     */
    public void add(MchApplicationParam param){
        MchApplication mchApplication = MchApplication.init(param);
        mchApplicationManager.save(mchApplication);
    }

    /**
     * 修改
     */
    public void update(MchApplicationParam param){
        MchApplication mchApplication = mchApplicationManager.findById(param.getId()).orElseThrow(DataNotExistException::new);

        BeanUtil.copyProperties(param,mchApplication, CopyOptions.create().ignoreNullValue());
        mchApplicationManager.updateById(mchApplication);
    }

    /**
     * 分页
     */
    public PageResult<MchApplicationDto> page(PageParam pageParam,MchApplicationParam mchApplicationParam){
        return MpUtil.convert2DtoPageResult(mchApplicationManager.page(pageParam,mchApplicationParam));
    }

    /**
     * 获取单条
     */
    public MchApplicationDto findById(Long id){
        return mchApplicationManager.findById(id).map(MchApplication::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 获取全部
     */
    public List<MchApplicationDto> findAll(){
        return ResultConvertUtil.dtoListConvert(mchApplicationManager.findAll());
    }

    /**
     * 删除
     */
    public void delete(Long id){
        mchApplicationManager.deleteById(id);
    }


}
