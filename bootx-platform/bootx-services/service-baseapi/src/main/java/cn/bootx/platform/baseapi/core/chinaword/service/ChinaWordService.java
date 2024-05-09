package cn.bootx.platform.baseapi.core.chinaword.service;

import cn.bootx.platform.baseapi.core.chinaword.dao.ChinaWordManager;
import cn.bootx.platform.baseapi.core.chinaword.entity.ChinaWord;
import cn.bootx.platform.baseapi.core.chinaword.wordfilter.WordContext;
import cn.bootx.platform.baseapi.core.chinaword.wordfilter.WordFilter;
import cn.bootx.platform.baseapi.core.chinaword.wordfilter.WordType;
import cn.bootx.platform.baseapi.dto.chinaword.ChinaWordDto;
import cn.bootx.platform.baseapi.dto.chinaword.ChinaWordVerifyResult;
import cn.bootx.platform.baseapi.param.chinaword.ChinaWordImportParam;
import cn.bootx.platform.baseapi.param.chinaword.ChinaWordParam;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.event.SyncReadListener;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 敏感词服务
 * @author xxm
 * @since 2023/8/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChinaWordService {
    private final WordContext wordContext;
    private final WordFilter wordFilter;

    private final ChinaWordManager chinaWordManager;

    /**
     * 添加
     */
    public void add(ChinaWordParam param){
        ChinaWord chinaWord = ChinaWord.init(param);
        this.updateWord(chinaWord);
        chinaWordManager.save(chinaWord);

    }

    /**
     * 批量导入
     */
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public void importBatch(MultipartFile file, String type){
        InputStream inputStream = file.getInputStream();
        //同步读取文件内容
        SyncReadListener syncReadListener = new SyncReadListener();
        EasyExcel.read(inputStream, syncReadListener)
                .head(ChinaWordImportParam.class)
                .sheet().doRead();
        syncReadListener.getList();
    }

    /**
     * 修改
     */
    public void update(ChinaWordParam param){
        ChinaWord ChinaWord = chinaWordManager.findById(param.getId()).orElseThrow(DataNotExistException::new);
        BeanUtil.copyProperties(param,ChinaWord, CopyOptions.create().ignoreNullValue());
        refresh();
        chinaWordManager.updateById(ChinaWord);
    }

    /**
     * 分页
     */
    public PageResult<ChinaWordDto> page(PageParam pageParam, ChinaWordParam query){
        return MpUtil.convert2DtoPageResult(chinaWordManager.page(pageParam,query));
    }

    /**
     * 获取单条
     */
    public ChinaWordDto findById(Long id){
        return chinaWordManager.findById(id).map(ChinaWord::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 获取全部
     */
    public List<ChinaWordDto> findAll(){
        return ResultConvertUtil.dtoListConvert(chinaWordManager.findAll());
    }

    /**
     * 删除
     */
    public void delete(Long id){
        refresh();
        chinaWordManager.deleteById(id);
    }

    /**
     * 测试敏感词效果
     */
    public ChinaWordVerifyResult verify(String text, int skip, char symbol){
        ChinaWordVerifyResult result = new ChinaWordVerifyResult();
        if (wordFilter.include(text,skip)) {
            String replaceText = wordFilter.replace(text, skip, symbol);
            int count = wordFilter.wordCount(text, skip);
            Set<String> wordList = wordFilter.wordList(text, skip);
            result.setText(replaceText)
                    .setCount(count)
                    .setSensitiveWords(wordList)
                    .setSensitive(true);
        }
        return result;
    }

    /**
     * 刷新缓存
     */
    public void refresh(){
        initData();
    }

    /**
     * 更新敏感词库
     */
    public void updateWord(ChinaWord chinaWord){
        if (Objects.equals(chinaWord.getEnable(),true)){
            if (Objects.equals(chinaWord.getWhite(),true)){
                wordContext.addWord(Collections.singleton(chinaWord.getWord()), WordType.WHITE);
            } else {
                wordContext.addWord(Collections.singleton(chinaWord.getWord()), WordType.BLACK);
            }
        }
    }

    /**
     * 初始化数据
     */
    @EventListener(WebServerInitializedEvent.class)
    public void initData(){
        List<ChinaWord> chinaWords = chinaWordManager.findAllByEnable(true);
        // 黑名单
        Set<String> black = chinaWords.stream()
                .filter(o -> Objects.equals(o.getWhite(), false))
                .map(ChinaWord::getWord)
                .collect(Collectors.toSet());
        // 白名单
        Set<String> white = chinaWords.stream()
                .filter(o -> Objects.equals(o.getWhite(), true))
                .map(ChinaWord::getWord)
                .collect(Collectors.toSet());

        wordContext.initKeyWord(black,white);
    }
}
