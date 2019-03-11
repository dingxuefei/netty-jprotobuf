package com.iscas.biz.service.sqlite;

import com.iscas.biz.mapper.DynamicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * //TODO
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2019/3/1 13:57
 * @since jdk1.8
 */
@Service
public class DynamicSqlliteService {
    @Autowired
    private DynamicMapper dynamicMapper;

    public List<Map> dynamicExecute(String sql){
        return dynamicMapper.dynamicSelect(sql);
    }
}
