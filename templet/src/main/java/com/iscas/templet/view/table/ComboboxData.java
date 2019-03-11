package com.iscas.templet.view.table;



import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author: zhuquanwen
 * @Description:
 * @Date: 2017/12/28 10:19
 * @Modified:
 **/
@Getter
@Setter
public class ComboboxData<T> implements Serializable {
    /*label*/
    protected String label;
    /*id*/
    protected Object id;
    /*value*/
    protected T value;



}
