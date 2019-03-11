package com.iscas.base.biz.test.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Author: zhuquanwen
 * @Description:
 * @Date: 2018/7/16 14:44
 * @Modified:
 **/
//@Repository
@Mapper
public interface MapResultMapper {
    /*
     * 查询返回到一个List<MapResponseData>中
     * */
    @Select("SELECT * FROM user ")
    List<Map> select();

    /**查询单个数据返回到Map*/
    @Select("SELECT * FROM parent where id = #{id,jdbcType=INTEGER} ")
    Map selectById(Integer id);
}
