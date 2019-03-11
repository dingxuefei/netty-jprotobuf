package com.iscas.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 执行SQL语句的Mapper
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/9/27 10:23
 * @since jdk1.8
 */
@Repository
public interface DynamicMapper extends BaseMapper {
    @Select("${sql}" )
    List<Map> dynamicSelect(@Param("sql") String sql);
}
