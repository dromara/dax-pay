package cn.bootx.platform.starter.file.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.starter.file.entity.UploadFileInfo;
import cn.bootx.platform.starter.file.param.UploadFileQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author xxm
 * @since 2022/1/12
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class UploadFileManager extends BaseManager<UploadFileMapper, UploadFileInfo> {


    /**
     * 根据URL查询
     */
    public Optional<UploadFileInfo> findByUrl(String url){
        return findByField(UploadFileInfo::getUrl, url);
    }

    /**
     * 根据URL删除
     */
    public boolean deleteByUrl(String url){
        return deleteByField(UploadFileInfo::getUrl, url);
    }

    /**
     * 分页
     */
    public Page<UploadFileInfo> page(PageParam pageParam, UploadFileQuery query) {
        Page<UploadFileInfo> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<UploadFileInfo> generator = QueryGenerator.generator(query);
        return this.page(mpPage,generator);
    }

}
