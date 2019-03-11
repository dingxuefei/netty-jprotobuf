package com.iscas.base.biz.common.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: DataDong
 * @Descrition:
 * @Date: Create in 2018/9/13 15:25
 * @Modified By:
 */
@Component
@Data
public class TableDefinitionConfig {
	@Value("${iscas.table.table-definition-table}")
	private String tableDefinitionTableName;
	@Value("${iscas.table.header-definition-table}")
	private  String headerDefinitionTableName;
	@Value("${iscas.table.primary-key}")
	private String primaryKey = null;
}
