package com.iscas.base.biz.common.model;

import lombok.Data;

@Data
public class ColumnDefinition {
	private Integer id;
	private String	tableDefinition;
	private String field;
	private String	header;
	private Boolean editable = false;
	private Boolean sortable = false;
	private String type;
	private Boolean search = false;
	private String searchType;
	private Boolean link = false;
	private Boolean addable = false;
	private Boolean hidden = false;
	private String refTable;
	private String refColumn;
	private String refValue;
	private Boolean required = false;
	private String reg;
	private Integer minLength = -1;
	private Integer maxLength = -1;
	private Boolean distinct = false;
}
