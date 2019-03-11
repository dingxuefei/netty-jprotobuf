package com.iscas.base.biz.common.model;

import lombok.Data;

/**
 * @Author: DataDong
 * @Descrition:
 * @Date: Create in 2018/9/11 8:59
 * @Modified By:
 */
@Data
public class TableDefinition {
	private Integer id;
	private String tableIdentity;
	private String title;
	private String databaseName;
	private String tableName;
	private String sql;
	private Boolean checkbox = false;
	private String backInfo;
	private String frontInfo;
	private String viewType;
	private String cellEditable;
}
