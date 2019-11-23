package com.yosang.language.forkjoin;

import com.yosang.language.dao.WordDao;
import com.yosang.language.pojo.Word;

import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

/**
 * @AUTHOR YoSang
 * @DATE 11/23/2019
 */
public class UpdateWordsForkJoin extends RecursiveTask<Boolean> {

    private static final int MAX_SHRESHOULD=1000;

    private final int start;

    private final int end;

    private final List<Word> data;

    private final WordDao wordDao;


    public UpdateWordsForkJoin(int start,int end,List<Word> data, WordDao wordDao) {
        this.start=start;
        this.end = end;
        this.data = data;
        this.wordDao=wordDao;
    }

    @Override
    protected Boolean compute() {
        if(end-start<=MAX_SHRESHOULD){
            for (int i = start; i < end; i++) {
                List<Word> wordList = data.stream().filter(word -> {
                    return data.indexOf(word) < end && data.indexOf(word) >= start;
                }).collect(Collectors.toList());
                wordDao.updateBatch(wordList);
                return true;
            }
        }else{
            int middle=(start+end)/2;
            UpdateWordsForkJoin leftUpdate=new UpdateWordsForkJoin(start,middle,data,wordDao);
            UpdateWordsForkJoin rightUpdate=new UpdateWordsForkJoin(middle,end,data,wordDao);
            leftUpdate.fork();
            rightUpdate.fork();
            return leftUpdate.join()&&rightUpdate.join();
        }
        return null;
    }
}
