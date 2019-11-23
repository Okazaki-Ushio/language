package com.yosang.language.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yosang.language.config.LanguageConfig;
import com.yosang.language.dao.ChineseWordDao;
import com.yosang.language.dao.WordDao;
import com.yosang.language.enumunation.WORDTYPE;
import com.yosang.language.exception.LanguageException;
import com.yosang.language.pojo.ChineseWord;
import com.yosang.language.pojo.Word;
import com.yosang.language.utils.JsonUtils;
import com.yosang.language.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                    .setWordType(wordType.getValue()).setWordViewCount(0);
            switch (wordType){
                case WAGO:
                case LOANWORD:
                    insert = wordDao.insert(word);
                    break;
                case CHINESE:
                case LOANWORD_CHINESE:
                    word.setWordChineseIds(LanguageConfig.getChineseIds(wordOriginal,chineseWordDao));
                    insert = wordDao.insert(word);
                    break;
            }
            if(insert!=1){
                throw new LanguageException("fail to insert "+word.getWordOriginal()+" to the database");
            }
            return JsonUtils.success("");
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JsonUtils.fail(1001, "fail to add word");
        }
    }

    @Override
    public JSONObject getWordAndRelation(Integer wordId) {
        Map<String, Object> map = LanguageConfig.getWordAndRelation(wordId, wordDao);
        return JsonUtils.success(map);
    }

    @Override
    public JSONObject getWordBySingleWord(String singleWord) {
        if(singleWord==null||singleWord.trim().equals("")){
            JSONObject jsonObject = randomStart(1);
            HashMap map = jsonObject.getObject("data", HashMap.class);
            return JsonUtils.success(map.get("list"));
        }
        List<Word> wordList;
        if(LanguageConfig.getWordType(singleWord)==WORDTYPE.CHINESE||LanguageConfig.getWordType(singleWord)==WORDTYPE.LOANWORD_CHINESE){
            List<String> chineseWords = LanguageConfig.filterChineseWords(singleWord);
            QueryWrapper<Word> wordCon=new QueryWrapper<>();
            for (String chineseWord : chineseWords) {
                wordCon.or().like("WORD_ORIGINAL",chineseWord);
            }
            wordList = wordDao.selectList(wordCon);
        }else {
            QueryWrapper<Word> con=new QueryWrapper<>();
            con.like("WORD_ORIGINAL",singleWord);
            wordList = wordDao.selectList(con);
        }
        LanguageConfig.updateListViewCount(wordList,wordDao);
        return JsonUtils.success(wordList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject checkAndUpdateWordNum(List<Word> words) {
        try {
            for (Word word : words) {
                word.setWordUpdateTime(TimeUtils.now());
                Word selectWord = wordDao.selectById(word.getWordId());
                selectWord.setWordMistakeNum(selectWord.getWordMistakeNum()+word.getWordMistakeNum());
                selectWord.setWordRightNum(selectWord.getWordRightNum()+selectWord.getWordRightNum());
                int update = wordDao.updateById(word);
                if(update!=1){
                    throw new Exception("fail to update word num");
                }
            }
            return JsonUtils.success("");
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JsonUtils.fail(1001, "fail to update word num");
        }
    }

    @Override
    public JSONObject randomStart(Integer start) {
        Word word=wordDao.randomStart(start);
        WORDTYPE wordType = LanguageConfig.getWordType(word.getWordOriginal());
        if(wordType==WORDTYPE.LOANWORD_CHINESE||wordType==WORDTYPE.CHINESE){
            return getWordAndRelation(word.getWordId());
        }else {
            return randomStart(start+1);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject deleteWordByWordId(Integer wordId) {
        try {
            Word word = wordDao.selectById(wordId);
            if(word.getWordType().equals(WORDTYPE.LOANWORD_CHINESE.getValue()) || word.getWordType().equals(WORDTYPE.CHINESE.getValue())){
                clearChinese(word);
            }
            int delete = wordDao.deleteById(wordId);
            if(delete==1){
                return JsonUtils.success("");
            }else {
                return JsonUtils.fail(1001,"fail to delete word");
            }
        } catch (LanguageException e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JsonUtils.fail(1001, "fail to delete word by wordId");
        }
    }

    private void clearChinese(Word word) throws LanguageException {
        String[] chineseIds = word.getWordChineseIds().substring(1, word.getWordChineseIds().length() - 1).split("\\|");
        for (String chineseId : chineseIds) {
            ChineseWord chineseWord = chineseWordDao.selectById(chineseId);
            int change;
            if(chineseWord.getChineseWordLinkNum()==1){
                change=chineseWordDao.deleteById(chineseId);
            }else {
                chineseWord.setChineseWordLinkNum(chineseWord.getChineseWordLinkNum()-1);
                chineseWord.setChineseWordUpdateTime(TimeUtils.now());
                change = chineseWordDao.updateById(chineseWord);
            }
            if(change<=0){
                throw new LanguageException("fail to clear chinese word");
            }
        }
    }

    @Override
    public JSONObject updateWordByWordId(Word word) {
        JSONObject deleteJson = deleteWordByWordId(word.getWordId());
        if(deleteJson.getInteger("errcode")!=0){
            return JsonUtils.fail(1001,"fail to delete original word");
        }
        return addWord(word);
    }

    @Override
    public JSONObject getWordByWordId(Integer wordId) {
        return JsonUtils.success(wordDao.selectById(wordId));
    }

    @Override
    public JSONObject getAllWords() {
        QueryWrapper<Word> con=new QueryWrapper<>();
        con.orderByDesc("WORD_MISTAKE_NUM").orderByAsc("WORD_RIGHT_NUM");
        return JsonUtils.success(wordDao.selectList(con));
    }

    @Override
    public JSONObject getWordsByWordType(Page<Word> page, Word word) {
        QueryWrapper<Word> wordCon=new QueryWrapper<>();
        wordCon.eq("WORD_TYPE",word.getWordType()).orderByDesc("WORD_VIEW_COUNT");
        IPage<Word> wordIPage = wordDao.selectPage(page, wordCon);
        return JsonUtils.successPage(wordIPage);
    }


}
