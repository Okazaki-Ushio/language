package com.yosang.language.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yosang.language.pojo.Word;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface WordDao extends BaseMapper<Word> {

    Word randomStart(Integer start);

    void updateBatch(List<Word> list);
}
