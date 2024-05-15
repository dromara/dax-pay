package cn.bootx.platform.iam.core.security.password.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.iam.core.security.password.entity.PasswordSecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 密码安全策略
 * @author xxm
 * @since 2023-09-20
 */
@Repository
@RequiredArgsConstructor
public class PasswordSecurityConfigManager extends BaseManager<PasswordSecurityConfigMapper, PasswordSecurityConfig> {

    @Override
    public List<PasswordSecurityConfig> findAll() {
        return lambdaQuery()
                .orderByDesc(PasswordSecurityConfig::getId)
                .list();
    }

    /**
     * 表中是否有数据
     */
    public boolean existsAll(){
       return lambdaQuery().exists();
    }

    /**
     * 删除除指定的id外的所有密码安全策略
     */
    public void deleteAllNotId(Long id){
        this.lambdaUpdate().ne(PasswordSecurityConfig::getId,id).remove();
    }
}
