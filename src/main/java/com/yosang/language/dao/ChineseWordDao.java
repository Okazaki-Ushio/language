package com.yosang.language.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yosang.language.pojo.ChineseWord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ChineseWordDao extends BaseMapper<ChineseWord> {

}
