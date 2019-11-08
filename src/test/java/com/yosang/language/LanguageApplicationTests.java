package com.yosang.language;

import com.yosang.language.dao.WordDao;
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

    @Test
    void contextLoads(){

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
