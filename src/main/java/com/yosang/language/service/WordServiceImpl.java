package com.yosang.language.service;

import com.alibaba.fastjson.JSONObject;
import com.yosang.language.config.LanguageConfig;
import com.yosang.language.dao.ChineseWordDao;
import com.yosang.language.dao.WordDao;
import com.yosang.language.enumunation.WORDTYPE;
import com.yosang.language.exception.LanguageException;
import com.yosang.language.pojo.Word;
import com.yosang.language.utils.JsonUtils;
import com.yosang.language.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * @AUTHOR YoSang
 * @DATE 11/7/2019
 */
@Service
public class WordServiceImpl implements WordService {

    @Autowired
    private WordDao wordDao;
    @Autowired
    private ChineseWordDao chineseWordDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addWord(Word word) {
        try {
            int insert=0;
            String wordOriginal = word.getWordOriginal();
            LanguageConfig.filterDuplicateWord(wordOriginal,wordDao);
            WORDTYPE wordType = LanguageConfig.getWordType(wordOriginal);
            word.setWordMistakeNum(0).setWordRightNum(0).setWordCreateTime(TimeUtils.now())
                    .setWordType(wordType.getValue());
            switch (wordType){
                case WAGO:
                case LOANWORD:
                    word.setWordPronunciation(wordOriginal);
                    insert = wordDao.insert(word);
                    break;
                case CHINESE:
                case LOANWORD_CHINESE:
                    word.setWordPronunciation(wordOriginal);
                    word.setWordChineseIds(LanguageConfig.getChineseIds(wordOriginal,chineseWordDao));
                    insert = wordDao.insert(word);
                    break;
            }
            if(insert!=1){
                throw new LanguageException("fail to insert word to the database");
            }
            return JsonUtils.success("");
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JsonUtils.fail(1001, "添加单词失败");
        }
    }

    @Override
    public JSONObject getWordAndRelation(Integer wordId) {
        List<Word> words = LanguageConfig.getWordAndRelation(wordId,wordDao);
        return JsonUtils.success(words);
    }

    @Override
    public JSONObject getWordBySingleWord(String singleWord) {
        if(singleWord.length()>1){
            return JsonUtils.fail(1001,"不是单个汉字");
        }
        List<Word> words = LanguageConfig.getWordsBySingleWord(singleWord, chineseWordDao, wordDao);
        return JsonUtils.success(words);
    }


}
