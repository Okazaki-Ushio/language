package com.yosang.language.controller;

import com.alibaba.fastjson.JSONObject;
import com.yosang.language.annotation.NotNullProps;
import com.yosang.language.pojo.Word;
import com.yosang.language.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @AUTHOR YoSang
 * @DATE 11/7/2019
 */
@RestController
@RequestMapping("language")
public class WordController {

    @Autowired
    private WordService wordService;

    @NotNullProps({"wordOriginal","wordPronunciation"})
    @RequestMapping("addWord")
    public JSONObject addWord(Word word){
        return wordService.addWord(word);
    }

    @NotNullProps({"wordId"})
    @RequestMapping("getWordAndRelation")
    public JSONObject getWordAndRelation(Integer wordId){
        return wordService.getWordAndRelation(wordId);
    }

    @NotNullProps({"singleWord"})
    @RequestMapping("getWordBySingleWord")
    public JSONObject getWordBySingleWord(String singleWord){
        return wordService.getWordBySingleWord(singleWord);
    }

    @RequestMapping("checkAndUpdateWordNum")
    public JSONObject checkAndUpdateWordNum(@RequestBody List<Word> words){
        return wordService.checkAndUpdateWordNum(words);
    }

    @RequestMapping("randomStart")
    public JSONObject randomStart(){
        return wordService.randomStart();
    }
}
