package com.iscas.templet.view.table;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author: zhuquanwen
 * @Description:
 * @Date: 2017/12/25 16:40
 * @Modified:
 **/
@Getter
@Setter
public class TableSearchRequest<T> implements Serializable{
    /*当前页码，默认为1*/
    protected Integer pageNumber = 1;
    /*每页显示条目，默认为10*/
    protected Integer pageSize = Integer.MAX_VALUE;
    /*排序的列*/
    protected String sortField;
    /*升序或者降序*/
    protected TableSortType sortOrder = TableSortType.asc;
    /*查询条件(扩展用)*/
    protected T filter;
    /*查询方式，为了与原有方式兼容，扩展一个查询方式*/
    protected Map<String,TableSearchType> searchType;


}
