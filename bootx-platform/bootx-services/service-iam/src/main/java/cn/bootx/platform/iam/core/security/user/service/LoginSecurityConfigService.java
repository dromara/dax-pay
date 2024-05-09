package cn.bootx.platform.iam.core.security.user.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.iam.core.security.user.dao.LoginSecurityConfigManager;
import cn.bootx.platform.iam.core.security.user.entity.LoginSecurityConfig;
import cn.bootx.platform.iam.dto.security.LoginSecurityConfigDto;
import cn.bootx.platform.iam.param.security.LoginSecurityConfigParam;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 登录安全策略
 * @author xxm
 * @since 2023-09-19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginSecurityConfigService {
    private final LoginSecurityConfigManager loginSecurityConfigManager;

    /**
     * 添加
     */
    public void add(LoginSecurityConfigParam param){
        LoginSecurityConfig loginSecurityConfig = LoginSecurityConfig.init(param);
        loginSecurityConfigManager.save(loginSecurityConfig);
    }

    /**
     * 修改
     */
    public void update(LoginSecurityConfigParam param){
        LoginSecurityConfig loginSecurityConfig = loginSecurityConfigManager.findById(param.getId()).orElseThrow(DataNotExistException::new);

        BeanUtil.copyProperties(param,loginSecurityConfig, CopyOptions.create().ignoreNullValue());
        loginSecurityConfigManager.updateById(loginSecurityConfig);
    }

    /**
     * 分页
     */
    public PageResult<LoginSecurityConfigDto> page(PageParam pageParam,LoginSecurityConfigParam query){
        return MpUtil.convert2DtoPageResult(loginSecurityConfigManager.page(pageParam,query));
    }

    /**
     * 获取单条
     */
    public LoginSecurityConfigDto findById(Long id){
        return loginSecurityConfigManager.findById(id).map(LoginSecurityConfig::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 获取全部
     */
    public List<LoginSecurityConfigDto> findAll(){
        return ResultConvertUtil.dtoListConvert(loginSecurityConfigManager.findAll());
    }

    /**
     * 删除
     */
    public void delete(Long id){
        loginSecurityConfigManager.deleteById(id);
    }


    /**
     * 批量删除
     */
    public void deleteBatch(List<Long> ids){
        loginSecurityConfigManager.deleteByIds(ids);
    }
}
