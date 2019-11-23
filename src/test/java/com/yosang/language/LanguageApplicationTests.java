package com.yosang.language;

import com.alibaba.fastjson.JSONObject;
import com.yosang.language.config.LanguageConfig;
import com.yosang.language.dao.ChineseWordDao;
import com.yosang.language.dao.WordDao;
import com.yosang.language.enumunation.WORDTYPE;
import com.yosang.language.pojo.DuplicateWords;
import com.yosang.language.pojo.Word;
import com.yosang.language.service.WordService;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.net.www.http.HttpClient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class LanguageApplicationTests {

    @Autowired
    private WordDao wordDao;
    @Autowired
    private ChineseWordDao chineseWordDao;
    @Autowired
    private WordService wordService;

    private static final CloseableHttpClient httpclient = HttpClients.createDefault();

    @Test
    public void contextLoads() throws IOException {


    }





    /*@Test
    public void trimDuplicateWords(){
        List<Word> words = wordDao.selectList(null);
        List<DuplicateWords> result=new ArrayList<>();
        for (int i = 0; i < words.size(); i++) {
            for (int j = i+1; j < words.size(); j++) {
                if(words.get(i).getWordOriginal().equals(words.get(j).getWordOriginal())){
                    wordDao.deleteById(words.get(j).getWordId());
                }
            }
        }
        System.out.println("ok");
    }*/

    /*@Test
    public void testTrimEnd(){
        List<Word> result=new ArrayList<>();
        List<Word> words = wordDao.selectList(null);
        for (int i = 0; i < words.size(); i++) {
            if(words.get(i).getWordOriginal().contains("ます")){
                result.add(words.get(i));
            }
        }
        for (Word word : result) {
            System.out.println(word.getWordOriginal());
        }
        System.out.println("ok");
    }*/

    /*@Test
    public void trimLikeWords(){
        List<DuplicateWords> result=new ArrayList<>();
        List<Word> words = wordDao.selectList(null);
        for (int i = 0; i < words.size(); i++) {
            for (int j = i+1; j < words.size(); j++) {
                String wordOriginal = words.get(i).getWordOriginal();
                String compareWord = words.get(j).getWordOriginal();
                if(wordOriginal.endsWith("る")&&compareWord.endsWith("ます")){
                    if(wordOriginal.substring(0,wordOriginal.length()-1).equals(compareWord.substring(0,compareWord.length()-2))){
                        wordDao.deleteById(words.get(j).getWordId());
                        //result.add(new DuplicateWords(words.get(i),words.get(j)));
                    }
                }else if(wordOriginal.endsWith("ます")&&compareWord.endsWith("る")){
                    if(wordOriginal.substring(0,wordOriginal.length()-2).equals(compareWord.substring(0,compareWord.length()-1))){
                        wordDao.deleteById(words.get(i).getWordId());
                        //result.add(new DuplicateWords(words.get(i),words.get(j)));
                    }
                }
            }
        }
        for (DuplicateWords duplicateWords : result) {
            System.out.println(duplicateWords.getFirstWord().getWordOriginal()+"-"+duplicateWords.getSecondWord().getWordOriginal());
        }
        System.out.println("ok");
    }*/

    /*@Test
    public void testPushExcel() throws IOException, InvalidFormatException {
        File excel=new File("D:\\project\\language\\src\\main\\resources\\data\\日语1-4级词汇表.xls");
        List<List<String>> lists = ExcelUtils.readExcel(excel,1);
        for (int i = 1; i < lists.size(); i++) {
            List<String> data = lists.get(i);
            Word word=new Word();
            word.setWordOriginal(data.get(0)).setWordMeaning(data.get(3)).setWordProperty(data.get(2));
            WORDTYPE wordType = LanguageConfig.getWordType(data.get(0));
            if(wordType==WORDTYPE.CHINESE||wordType==WORDTYPE.LOANWORD_CHINESE){
                word.setWordPronunciation(data.get(1));
            }
            wordService.addWord(word);
        }
        System.out.println("ok");
    }*/

    /*@Test
    public void readMiddleText() throws IOException {
        File file = new File("D:\\project\\language\\src\\main\\resources\\data\\中级上下.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        List<String> tempList = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            tempList.add(line);
        }
        String now = TimeUtils.now();
        for (String temp : tempList) {
            int bStart= !temp.contains("(") ?Integer.MAX_VALUE:temp.indexOf("(");
            int mStart= !temp.contains("[") ?Integer.MAX_VALUE:temp.indexOf("[");
            int wordEndIndex=bStart>mStart?mStart:bStart;
            String originalWord=temp.substring(0,wordEndIndex);
            String wordPronunciation="";
            WORDTYPE wordType = LanguageConfig.getWordType(originalWord);
            if(wordType==WORDTYPE.CHINESE||wordType==WORDTYPE.LOANWORD_CHINESE){
                wordPronunciation=temp.substring(wordEndIndex+1,temp.indexOf(")"));
            }
            String wordMeaning=temp.substring(temp.lastIndexOf("]")>temp.lastIndexOf(")")
                    ?temp.lastIndexOf("]")+1:temp.lastIndexOf(")")+1);
            Word word=new Word();
            word.setWordOriginal(originalWord).setWordPronunciation(wordPronunciation).setWordMeaning(wordMeaning);
            wordService.addWord(word);
        }
    }*/

    /*@Test
    public void readJuniorText() throws IOException {
        wordDao.delete(null);
        chineseWordDao.delete(null);
        File file = new File("D:\\project\\language\\src\\main\\resources\\data\\初级上下.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        List<String> tempList = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            tempList.add(line);
        }
        String now = TimeUtils.now();
        for (String temp : tempList) {
            int bEnd=temp.lastIndexOf(")");
            int mEnd=temp.lastIndexOf("]");
            int meaningEndIndex=bEnd>mEnd?bEnd:mEnd;
            String meaningIndex = temp.substring(meaningEndIndex+1);
            if(temp.contains("(")){
                int start = temp.indexOf("(");
                String chineseWord = temp.substring(start+1, temp.indexOf(")")).trim();
                int tempIndex = temp.indexOf("[");
                start=tempIndex<start&&tempIndex!=-1?tempIndex:start;
                String pronunciation = temp.substring(0, start);
                Word word=new Word();
                WORDTYPE wordType = LanguageConfig.getWordType(chineseWord);
                if(wordType==WORDTYPE.CHINESE||wordType==WORDTYPE.LOANWORD_CHINESE){
                    word.setWordPronunciation(pronunciation);
                }
                word.setWordType(wordType.getValue()).setWordOriginal(chineseWord).setWordMeaning(meaningIndex);
                wordService.addWord(word);
            }else{
                int start = !temp.contains("[") ?Integer.MAX_VALUE:temp.indexOf("[");
                int tempStart = !temp.contains("(") ?Integer.MAX_VALUE:temp.indexOf("(");
                start=start<tempStart?start:tempStart;
                String notChineseWord = temp.substring(0, start).trim();
                Word word=new Word();
                word.setWordType(LanguageConfig.getWordType(notChineseWord).getValue())
                        .setWordOriginal(notChineseWord).setWordMeaning(meaningIndex);
                wordService.addWord(word);
            }
        }
    }*/

    /*@Test
    public void getJuniorText() throws IOException {
        File file = new File("C:\\Users\\Administrator\\Desktop\\练习打字.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        List<String> tempList = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            tempList.add(line);
        }
        List<String> flatList = new ArrayList<>();
        List<String> kaList = new ArrayList<>();
        for (int i = 0; i < tempList.size() - 2; i = i + 2) {
            String[] splits = tempList.get(i).split(" ");
            if (splits.length == 2) {
                flatList.add(splits[0]);
                kaList.add(splits[1]);
            }
        }
        writeFile("flatList",flatList);
        writeFile("kaList",kaList);
        System.out.println("ok");
    }*/

    private void writeFile(String name, List<String> data) throws IOException {
        File flatFile = new File("C:\\Users\\Administrator\\Desktop\\"+name+".txt");
        BufferedWriter writer=new BufferedWriter(new FileWriter(flatFile));
        int i=0;
        for (String flat : data) {
            writer.write("\""+flat+"\",");
            if(++i%10==0){
                writer.write("\n");
            }
        }
        writer.flush();
        writer.close();
    }
}
