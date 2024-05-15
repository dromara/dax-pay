package cn.bootx.platform.iam.core.client.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.entity.QueryParams;
import cn.bootx.platform.iam.param.client.LoginTypeParam;
import cn.bootx.platform.iam.core.client.dao.LoginTypeManager;
import cn.bootx.platform.iam.core.client.entity.LonginType;
import cn.bootx.platform.iam.dto.client.LoginTypeDto;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 终端
 *
 * @author xxm
 * @since 2021/8/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginTypeService {

    private final LoginTypeManager loginTypeManager;

    /**
     * 添加
     */
    public LoginTypeDto add(LoginTypeParam param) {
        if (loginTypeManager.existsByCode(param.getCode())) {
            throw new BizException("终端编码不得重复");
        }
        LonginType longinType = LonginType.init(param);
        longinType.setInternal(false);
        return loginTypeManager.save(longinType).toDto();
    }

    /**
     * 修改
     */
    public LoginTypeDto update(LoginTypeParam param) {
        LonginType longinType = loginTypeManager.findById(param.getId()).orElseThrow(() -> new BizException("终端不存在"));
        if (loginTypeManager.existsByCode(param.getCode(), longinType.getId())) {
            throw new BizException("终端编码不得重复");
        }
        if (longinType.isInternal()) {
            longinType.setEnable(true);
        }
        BeanUtil.copyProperties(param, longinType, CopyOptions.create().ignoreNullValue());
        return loginTypeManager.updateById(longinType).toDto();
    }

    /**
     * 分页
     */
    public PageResult<LoginTypeDto> page(PageParam pageParam, LoginTypeParam loginTypeParam) {
        return MpUtil.convert2DtoPageResult(loginTypeManager.page(pageParam, loginTypeParam));
    }

    /**
     * 超级查询
     */
    public PageResult<LoginTypeDto> superPage(PageParam pageParam, QueryParams queryParams) {
        return MpUtil.convert2DtoPageResult(loginTypeManager.supperPage(pageParam, queryParams));
    }

    /**
     * 获取单条
     */
    public LoginTypeDto findById(Long id) {
        return loginTypeManager.findById(id).map(LonginType::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 获取单条
     */
    public LoginTypeDto findByCode(String code) {
        return loginTypeManager.findByCode(code).map(LonginType::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 获取全部
     */
    public List<LoginTypeDto> findAll() {
        return ResultConvertUtil.dtoListConvert(loginTypeManager.findAll());
    }

    /**
     * 删除
     */
    public void delete(Long id) {
        LonginType longinType = loginTypeManager.findById(id).orElseThrow(DataNotExistException::new);
        if (longinType.isInternal()) {
            throw new BizException("系统内置终端，不可删除");
        }
        loginTypeManager.deleteById(id);
    }

    /**
     * 编码是否已经存在
     */
    public boolean existsByCode(String code) {
        return loginTypeManager.existsByCode(code);
    }

    /**
     * 编码是否已经存在(不包含自身)
     */
    public boolean existsByCode(String code, Long id) {
        return loginTypeManager.existsByCode(code, id);
    }

}
