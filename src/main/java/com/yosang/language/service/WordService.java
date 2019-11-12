package com.yosang.language.service;

import com.alibaba.fastjson.JSONObject;
import com.yosang.language.pojo.Word;

import java.util.List;

/**
 * @AUTHOR YoSang
 * @DATE 11/7/2019
 */
public interface WordService {

    JSONObject addWord(Word word);

    JSONObject getWordAndRelation(Integer wordId);

    JSONObject getWordBySingleWord(String singleWord);

    JSONObject checkAndUpdateWordNum(List<Word> words);

    JSONObject randomStart(Integer start);

    JSONObject deleteWordByWordId(Integer wordId);

    JSONObject updateWordByWordId(Word word);

    JSONObject getWordByWordId(Integer wordId);

    JSONObject getAllWords();
}
