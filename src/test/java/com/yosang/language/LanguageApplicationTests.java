package com.yosang.language;

import com.yosang.language.config.LanguageConfig;
import com.yosang.language.dao.ChineseWordDao;
import com.yosang.language.dao.WordDao;
import com.yosang.language.enumunation.WORDTYPE;
import com.yosang.language.pojo.ChineseWord;
import com.yosang.language.pojo.Word;
import com.yosang.language.service.WordService;
import com.yosang.language.utils.TimeUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class LanguageApplicationTests {

    @Autowired
    private WordDao wordDao;
    @Autowired
    private ChineseWordDao chineseWordDao;
    @Autowired
    private WordService wordService;

    @Test
    void contextLoads(){
        String chineseIds="|7822|7857|7867|";
        String substring = chineseIds.substring(1, chineseIds.length() - 2);
        String[] ids = substring.split("\\|");
        System.out.println("ok");
    }

    @Test
    public void readTxt() throws IOException {
        wordDao.delete(null);
        chineseWordDao.delete(null);
        File file = new File("D:\\My Projects\\language\\src\\main\\resources\\data\\初级上下.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        List<String> tempList = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            tempList.add(line);
        }
        String now = TimeUtils.now();
        for (String temp : tempList) {
            int bEnd=temp.indexOf(")");
            int mEnd=temp.indexOf("]");
            int end=bEnd>mEnd?bEnd:mEnd;
            String meaningIndex = temp.substring(end+1);

            if(temp.contains("(")){
                int start = temp.indexOf("(");
                String chineseWord = temp.substring(start+1, temp.indexOf(")")).trim();
                int tempIndex = temp.indexOf("[");
                start=tempIndex<start&&tempIndex!=-1?tempIndex:start;
                String pronunciation = temp.substring(0, start);
                Word word=new Word();
                word.setWordPronunciation(pronunciation).setWordType(WORDTYPE.CHINESE.getValue())
                        .setWordCreateTime(now).setWordRightNum(0).setWordMistakeNum(0)
                        .setWordOriginal(chineseWord).setWordMeaning(meaningIndex);
                wordService.addWord(word);
            }else{
                int start = !temp.contains("[") ?Integer.MAX_VALUE:temp.indexOf("[");
                int tempStart = !temp.contains("(") ?Integer.MAX_VALUE:temp.indexOf("(");
                start=start<tempStart?start:tempStart;
                String notChineseWord = temp.substring(0, start).trim();
                Word word=new Word();
                word.setWordPronunciation(notChineseWord).setWordType(LanguageConfig.getWordType(notChineseWord).getValue())
                        .setWordCreateTime(now).setWordRightNum(0).setWordMistakeNum(0)
                        .setWordOriginal(notChineseWord).setWordMeaning(meaningIndex);
                wordService.addWord(word);
            }
        }
    }

    @Test
    public void getText() throws IOException {
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
    }

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
