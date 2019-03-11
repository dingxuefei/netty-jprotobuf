package com.iscas.base.biz.common.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.iscas.base.biz.common.mapper.TableDefinitionMapper;
import com.iscas.base.biz.exception.BaseException;
import com.iscas.base.biz.util.JsonUtils;
import com.iscas.base.biz.util.StringUtils;
import com.iscas.base.biz.common.model.ColumnDefinition;
import com.iscas.base.biz.common.model.TableDefinition;
import com.iscas.base.biz.common.model.TableDefinitionConfig;
import com.iscas.base.biz.exception.ValidDataException;
import com.iscas.common.tools.exception.ExceptionUtils;
import com.iscas.templet.common.ResponseEntity;
import com.iscas.templet.view.table.*;
import com.iscas.templet.view.validator.Rule;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: DataDong
 * @Descrition: 表格自定义操作服务
 * @Date: Create in 2018/9/11 14:24
 * @Modified By:
 */
@Service
@Slf4j
public class TableDefinitionService {
//	@Value("${iscas.table.table-definition-table}")
//	private String tableDefinitionConfig.getTableDefinitionTableName();
//	@Value("${iscas.table.header-definition-table}")
//	private  String tableDefinitionConfig.getHeaderDefinitionTableName();
//	@Value("${iscas.table.primary-key}")
//	private String primaryKey = "id";

	@Autowired
	private TableDefinitionMapper tableDefinitionMapper;

	@Autowired
	private TableDefinitionConfig tableDefinitionConfig;
	
	/**
	 * 获取表格的表头定义，不带下拉列表选项
	 * @param tableIdentity
	 * @return
	 */
	private TableHeaderResponse getTableHeader(String tableIdentity, boolean withOption){
		TableHeaderResponse tableHeaderResponse = new TableHeaderResponse();
		long start = System.currentTimeMillis();
		try {
			if (StringUtils.isEmpty(tableIdentity)) {
				tableHeaderResponse.setMessage(String.format("parameter is empty or null"));
				tableHeaderResponse.setDesc("传入表格标识参数为空！");
//				tableHeaderResponse.setFlagType(FlagType.fail);
				return tableHeaderResponse;
			}

			List<ColumnDefinition> columnDefinitions =
				tableDefinitionMapper.getHeaderByIdentify(tableDefinitionConfig.getHeaderDefinitionTableName() , tableIdentity);
			if (columnDefinitions == null || columnDefinitions.size() == 0) {
				tableHeaderResponse.setMessage(String.format("column definition not exist for [%s]", tableIdentity));
				tableHeaderResponse.setDesc(String.format("[%s]的表头定义不存在！", tableIdentity));
//				tableHeaderResponse.setFlagType(FlagType.fail);
				return tableHeaderResponse;
			}

			TableHeaderResponseData tableHeaderResponseData = new TableHeaderResponseData();
			tableHeaderResponseData.setCols(analyzeTableField(columnDefinitions, withOption));
			tableHeaderResponse.setValue(tableHeaderResponseData);
		}catch (Exception e){
			e.printStackTrace();
			tableHeaderResponse.setDesc(ExceptionUtils.getExceptionInfo(e));
//			tableHeaderResponse.setFlagType(FlagType.fail);
		}finally {
			tableHeaderResponse.setTookInMillis(System.currentTimeMillis() - start);
		}
		return tableHeaderResponse;
	}

	/**
	 * 将数据表ColumnDefinition转换成控件模型TableField
	 * @param columnDefinitions
	 * @return
	 */
	private List<TableField> analyzeTableField(List<ColumnDefinition> columnDefinitions, boolean withOption)
		throws ValidDataException {
		List<TableField> tableFields = new ArrayList<>();
		for(ColumnDefinition columnDefinition : columnDefinitions){
			TableField tableField = new TableField();
			tableField.setAddable(columnDefinition.getAddable()==true);
			tableField.setEditable(columnDefinition.getEditable()==true);
			tableField.setField(columnDefinition.getField());
			tableField.setHeader(columnDefinition.getHeader());
			tableField.setHidden(columnDefinition.getHidden()==true);
			tableField.setLink(columnDefinition.getLink()==true);

			//rule
			Rule rule = new Rule();
			rule.setDistinct(columnDefinition.getDistinct());
			rule.setReg(columnDefinition.getReg());
			rule.setRequired(columnDefinition.getRequired()==true);
			if(columnDefinition.getMaxLength() != null || columnDefinition.getMinLength() != null) {
				Map<String, Integer> map = new HashMap<>();
				if(columnDefinition.getMaxLength() != null) {
					map.put("max",columnDefinition.getMaxLength());
				}
				if(columnDefinition.getMinLength() != null) {
					map.put("min",columnDefinition.getMinLength());
				}
				rule.setLength(map);
			}
			tableField.setRule(rule);

			tableField.setSearch(columnDefinition.getSearch());
			tableField.setSearchType(TableSearchType.analyzeSearchType(columnDefinition.getSearchType()));
//			tableField.setSearchWay(columnDefinition.getField());
//			tableField.setParent();
			tableField.setType(TableFieldType.analyzeFieldType(columnDefinition.getType()));

			//构建Option选项
			if(withOption){
				Map<Object, Object> valueMap = null;
				if(!StringUtils.isEmpty(columnDefinition.getRefValue())){
					valueMap = JsonUtils.fromJson(columnDefinition.getRefValue(),new TypeReference<HashMap<Integer, String>>(){});
				}else if(!StringUtils.isEmpty(columnDefinition.getRefTable()) &&
					!StringUtils.isEmpty(columnDefinition.getRefTable())){
					List<Map<Object,Object>> refValues = tableDefinitionMapper.getRefTable(columnDefinition.getRefTable(),
						"id", columnDefinition.getRefColumn());
					valueMap = new LinkedHashMap<>();
					for(Map<Object,Object> tmpItem : refValues){
						valueMap.put(tmpItem.get("id"),tmpItem.get("value"));
					}
				}

				if(valueMap != null) {
					List<ComboboxData> comboboxDataList = new ArrayList<>();
					Iterator<Map.Entry<Object, Object>> entries = valueMap.entrySet().iterator();
					while (entries.hasNext()) {
						Map.Entry<Object, Object> entry = entries.next();
						ComboboxData comboboxData = new ComboboxData();
						comboboxData.setLabel(entry.getValue().toString());
						comboboxData.setId(entry.getKey());
						comboboxData.setValue(entry.getKey());
						comboboxDataList.add(comboboxData);
					}
					tableField.setOption(comboboxDataList);
					//检查列的type和ref是否匹配
					if(!TableFieldType.select.name().equalsIgnoreCase(columnDefinition.getType())){
						ValidDataException validDataException = new ValidDataException(
							String.format("列[%s]的type配置不是[select],但是存在ref配置！", columnDefinition.getField()));
						validDataException.setMsgDetail(
							String.format("the type of field [%s] is not [select],but exist ref config", columnDefinition.getField()));
						throw  validDataException;
					}
				}
			}
			tableFields.add(tableField);
		}

		return tableFields;
	}


	/**
	 * 获取表头定义
	 * @param tableIdentity 前后台统一的表格标识
	 * @return
	 */
	public TableHeaderResponse getTableHeader(String tableIdentity)  {
		return getTableHeader(tableIdentity, false);
	}

	/**
	 * 获取表头定义，带下拉选项
	 * @param tableIdentity 前后台统一的表格标识
	 * @return
	 */
	public TableHeaderResponse getHeaderWithOption(String tableIdentity) {
		return getTableHeader(tableIdentity, true);
	}

	/**
	 * 获取表格数据，不带表头
	 * @param tableIdentity
	 * @param request 前台构造的查询条件
	 * @param dynamicParam 通过session传入的动态条件
	 * @return
	 */
	public TableResponse getData(String tableIdentity,TableSearchRequest request, Map<String,Object> dynamicParam)
		throws ValidDataException {
		TableResponse tableResponse = new TableResponse();
		long start = System.currentTimeMillis();
		try {
			//查询表格定义
			TableDefinition tableDefinition =
				tableDefinitionMapper.getTableByIdentify(tableDefinitionConfig.getTableDefinitionTableName(), tableIdentity);
			List<ColumnDefinition> columnDefinitions =
				tableDefinitionMapper.getHeaderByIdentify(tableDefinitionConfig.getHeaderDefinitionTableName(), tableIdentity);
			if (null == tableDefinition) {
//				tableResponse.setMessage(String.format("table definition not exist for [%s]", tableIdentity));
//				tableResponse.setDesc(String.format("[%s]的表格定义不存在！", tableIdentity));
//				tableResponse.setFlagType(FlagType.fail);
				ValidDataException validDataException = new ValidDataException(String.format("[%s]的表格定义不存在！", tableIdentity));
				validDataException.setMsgDetail(String.format("table definition not exist for [%s]", tableIdentity));
				throw  validDataException;
			}

			//解析前台参数
			if(request == null){
				request = new TableSearchRequest();
			}
			Map<String, Object> paramMap = (Map<String, Object>) request.getFilter();
			StringBuffer where = new StringBuffer();
			if (paramMap == null) {
				paramMap = new HashMap<>();
			} 
			Map<String, Object> tmpMap = new HashMap<>();
			for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
				if(entry.getValue() == null){//查询条件为空的忽略
					continue;
				}
				if (!(entry.getValue() instanceof List)) {//检查条件是否为List
					ValidDataException validDataException = new ValidDataException(String.format("[%s]列的搜索条件取值不正确！", entry.getKey()));
					validDataException.setMsgDetail(String.format("invalidate value [%s] for field [%s]", entry.getValue(), entry.getKey()));
					throw  validDataException;
				}
				List values = (List)entry.getValue();
				if(values.size() == 0){//参数为空忽略
					continue;
				}
				
				//查找对应的列定义
				ColumnDefinition columnDefinition = null;
				for (ColumnDefinition tmp : columnDefinitions) {
					if (tmp.getField().equalsIgnoreCase(entry.getKey())) {
						if( tmp.getSearch() != true) {
							ValidDataException validDataException = new ValidDataException(
								String.format("[%s]列的属性search!=true，不允许检索！", tmp.getField()));
							validDataException.setMsgDetail(String.format("search!=true for field [%s]", tmp.getField()));
							throw validDataException;
						}
						columnDefinition = tmp;
						break;
					}
				}
				
				//是否找到列定义
				if(null == columnDefinition){
					ValidDataException validDataException = new ValidDataException(
						String.format("找不到[%s]列的定义！", entry.getKey()));
					validDataException.setMsgDetail(String.format("can not find definition for field [%s]", entry.getKey()));
					throw validDataException;
				}

				//解析searchType
				TableSearchType searchType = TableSearchType.analyzeSearchType(columnDefinition.getSearchType());
				if (searchType == null) {
					ValidDataException validDataException = new ValidDataException(
						String.format("[%s]列的searchType属性配置信息错误！", columnDefinition.getField()));
					validDataException.setMsgDetail(String
						.format("invalidate search type config [%s] for field [%s]", columnDefinition.getSearchType(),
							columnDefinition.getField()));
					throw validDataException;
				}
				
				switch (searchType) {
					case exact:
						if( StringUtils.isEmpty(values.get(0)) ){
							continue;
						}
						tmpMap.put(entry.getKey(),values.get(0));
						if (where.length() > 0) {
							where.append(String.format(" AND "));
						}
						where.append(String.format(" %s=#{param.%s}", entry.getKey(), entry.getKey()));
						break;
					case like:
						if( StringUtils.isEmpty(values.get(0)) ){
							continue;
						}
						tmpMap.put(entry.getKey(), "%" + values.get(0) + "%");
						if (where.length() > 0) {
							where.append(String.format(" AND "));
						}
						where.append(String.format(" %s LIKE #{param.%s}", entry.getKey(), entry.getKey()));
						break;
					case prefix:
						if( StringUtils.isEmpty(values.get(0)) ){
							continue;
						}
						tmpMap.put(entry.getKey(), values.get(0) + "%");
						if (where.length() > 0) {
							where.append(String.format(" AND "));
						}
						where.append(String.format(" %s LIKE #{param.%s}", entry.getKey(), entry.getKey()));
						break;
					case range:
						if (values.get(0) != null && values.get(1) != null) {
							tmpMap.put(entry.getKey() + "Min", values.get(0));
							tmpMap.put(entry.getKey() + "Max", values.get(1));
							if (where.length() > 0) {
								where.append(String.format(" AND "));
							}
							where.append(String.format(" %s>=#{param.%s} AND %s<=#{param.%s}", entry.getKey(),
								entry.getKey() + "Min", entry.getKey(), entry.getKey() + "Max"));
						} else if (values.get(0) != null) {
							tmpMap.put(entry.getKey() + "Min", values.get(0));
							if (where.length() > 0) {
								where.append(String.format(" AND "));
							}
							where.append(String.format(" %s>=#{param.%s}", entry.getKey(), entry.getKey() + "Min"));
						} else if (values.get(1) != null) {
							tmpMap.put(entry.getKey() + "Max", values.get(1));
							if (where.length() > 0) {
								where.append(String.format(" AND "));
							}
							where.append(String.format(" %s<=#{param.%s}", entry.getKey(), entry.getKey() + "Max"));
						}else{
							continue;
						}
						break;
					default: break;
				}
			}
			//合并参数
			for (Map.Entry<String, Object> tmpEntry : tmpMap.entrySet()) {
				paramMap.put(tmpEntry.getKey(), tmpEntry.getValue());
			}

			//处理动态参数
			if (dynamicParam != null) {
				for (Map.Entry<String, Object> entry : dynamicParam.entrySet()) {
					paramMap.put(entry.getKey(), entry.getValue());
				}
			}

			//拼接不带分页sql
			String sql = tableDefinition.getSql().replace("{", "{param.");
			String countSql=null, dataSql = null;
			if (where.length() != 0) {
				countSql = "SELECT COUNT(1) FROM (" + sql+") t WHERE "+where;
				dataSql = "SELECT * FROM (" + sql+") t WHERE "+where;
			}else{
				countSql = "SELECT COUNT(1) FROM (" + sql+") t ";
				dataSql =  sql;
			}

			//查询count
			TableResponseData tableResponseData = new TableResponseData();
			log.debug("datasong count sql : " + countSql);
			int count = tableDefinitionMapper.getCountBySql(countSql, paramMap);
			tableResponseData.setRows((long) count);

			//查询数据
			if (count > (request.getPageSize() * (request.getPageNumber() - 1))) {
				if (!StringUtils.isEmpty(request.getSortField())) {
					dataSql = String.format(dataSql + " ORDER BY %s %s ", request.getSortField(), request.getSortOrder().name().toUpperCase());
				}
				dataSql = String.format(dataSql + " LIMIT %d,%d;", (request.getPageSize() * (request.getPageNumber() - 1)),
					request.getPageSize());
				log.debug("datasong get data sql : " + sql);
				List<Map<String, Object>> datas = tableDefinitionMapper.getDataBySql(dataSql, paramMap);
				tableResponseData.setData(datas);
			}
			tableResponse.setValue(tableResponseData);
		}catch (BaseException e){
			e.printStackTrace();
			throw e;
		}finally {
			tableResponse.setTookInMillis(System.currentTimeMillis() - start);
		}
		return tableResponse;
	}

	/**
	 * 删除表格记录
	 * @param tableIdentity
	 * @param id
	 * @return
	 */
	public ResponseEntity deleteData(String tableIdentity, Object id) throws ValidDataException {
		ResponseEntity responseEntity = new ResponseEntity();
		long start = System.currentTimeMillis();
		try {
			//查询表格定义
			TableDefinition tableDefinition = tableDefinitionMapper.getTableByIdentify(tableDefinitionConfig.getTableDefinitionTableName()  , tableIdentity);
			if( null == tableDefinition){
//				responseEntity.setMessage(String.format("table definition not exist for [%s]",tableIdentity));
//				responseEntity.setDesc(String.format("[%s]的表格定义不存在！",tableIdentity));
//				responseEntity.setFlagType(FlagType.fail);
//				return responseEntity;
				ValidDataException validDataException = new ValidDataException(String.format("[%s]的表格定义不存在！",tableIdentity));
				validDataException.setMsgDetail(String.format("table definition not exist for [%s]",tableIdentity));
				throw  validDataException;
			}

			//检查参数
			if(StringUtils.isEmpty(tableDefinition.getTableName())){
//				responseEntity.setMessage(String.format("column tableName [%s] is empty",tableIdentity));
//				responseEntity.setDesc(String.format("[%s]的表格定义中tableName为空！",tableIdentity));
//				responseEntity.setFlagType(FlagType.fail);
//				return responseEntity;
				ValidDataException validDataException = new ValidDataException(String.format("[%s]的表格定义中tableName为空！",tableIdentity));
				validDataException.setMsgDetail(String.format("column tableName [%s] is empty",tableIdentity));
				throw  validDataException;
			}

			int ret = tableDefinitionMapper.deleteData(tableDefinition.getTableName(), tableDefinitionConfig.getPrimaryKey(), id);
			responseEntity.setValue(ret);
		}catch (BaseException e){
			e.printStackTrace();
			throw e;
		}finally {
			responseEntity.setTookInMillis(System.currentTimeMillis() - start);
		}
		return responseEntity;
	}


	@SuppressWarnings("AlibabaUndefineMagicConstant")
    public ResponseEntity saveData(String tableIdentity, Map<String, Object> item) throws ValidDataException {
		ResponseEntity responseEntity = new ResponseEntity();
		long start = System.currentTimeMillis();
		try {
			//查询表格定义
			TableDefinition tableDefinition =
				tableDefinitionMapper.getTableByIdentify(tableDefinitionConfig.getTableDefinitionTableName(), tableIdentity);
			if (null == tableDefinition) {
				ValidDataException validDataException = new ValidDataException(String.format("[%s]的表格定义不存在！", tableIdentity));
				validDataException.setMsgDetail(String.format("table definition not exist for [%s]", tableIdentity));
				throw  validDataException;
			}

			//检查参数
			if (StringUtils.isEmpty(tableDefinition.getTableName())) {
				ValidDataException validDataException = new ValidDataException(String.format("[%s]的表格定义中tableName为空！", tableIdentity));
				validDataException.setMsgDetail(String.format("column tableName [%s] is empty", tableIdentity));
				throw  validDataException;
			}

			//构造sql
			List<ColumnDefinition> columnDefinitions = tableDefinitionMapper.getHeaderByIdentify(tableDefinitionConfig.getHeaderDefinitionTableName(), tableIdentity);
			SQL sql = new SQL(); //SQL语句对象，所在包：org.apache.ibatis.jdbc.SQL
			sql.INSERT_INTO(tableDefinition.getTableName());
			for (ColumnDefinition columnDefinition : columnDefinitions) {
				if(columnDefinition.getAddable() == true || columnDefinition.getEditable() == true){
					if (item.containsKey(columnDefinition.getField()) ) {
						//规则校验
						Object value = item.get(columnDefinition.getField());
						String strValue = (value == null? null:value.toString());
						if(!StringUtils.isEmpty(columnDefinition.getReg())){//正则检查
							if(!strValue.matches(columnDefinition.getReg())){
								ValidDataException validDataException = new ValidDataException(String.format("[%s]表格的列[%s]取值[%s]不满足正则[%s]要求！",
									tableIdentity, columnDefinition.getField(), strValue , columnDefinition.getReg()));
								validDataException.setMsgDetail(String.format("the value [%s] of column [%s] for tableName [%s] not meet reg [%s]",
									strValue, columnDefinition.getField(), tableIdentity, columnDefinition.getReg()));
								throw  validDataException;
							}
						}

						//长度检查
						if(columnDefinition.getMinLength() >0){
							if(!StringUtils.isEmpty(strValue) && strValue.length() >= columnDefinition.getMinLength()){
							}else{
								ValidDataException validDataException = new ValidDataException(String.format("[%s]表格的列[%s]取值[%s]不满足MinLength[%d]要求！",
									tableIdentity, columnDefinition.getField(), strValue , columnDefinition.getMinLength()));
								validDataException.setMsgDetail(String.format("the value [%s] of column [%s] for tableName [%s] not meet MinLength [%d]",
									strValue, columnDefinition.getField(), tableIdentity, columnDefinition.getMinLength()));
								throw  validDataException;
							}
						}
						if(columnDefinition.getMaxLength() >0){
							if(!StringUtils.isEmpty(strValue) && strValue.length() <= columnDefinition.getMaxLength()){
							}else{
								ValidDataException validDataException = new ValidDataException(String.format("[%s]表格的列[%s]取值[%s]不满足MaxLength[%d]要求！",
									tableIdentity, columnDefinition.getField(), strValue , columnDefinition.getMinLength()));
								validDataException.setMsgDetail(String.format("the value [%s] of column [%s] for tableName [%s] not meet MaxLength [%d]",
									strValue, columnDefinition.getField(), tableIdentity, columnDefinition.getMinLength()));
								throw  validDataException;
							}
						}

						sql.VALUES(columnDefinition.getField(), String.format("#{param.%s}", columnDefinition.getField()));// 设置键值
					}else if(columnDefinition.getRequired() == true){//必填而没填
						ValidDataException validDataException = new ValidDataException(String.format("[%s]的表格列[%s]必填！", tableIdentity, columnDefinition.getField()));
						validDataException.setMsgDetail(String.format("column [%s] for tableName [%s] is empty", columnDefinition.getField(), tableIdentity));
						throw  validDataException;
					}
				}
			}
			//之前的方式，通过系统表插入
			/*List<String> columns = tableDefinitionMapper.getTableColumns(tableDefinition.getTableName());
			SQL sql = new SQL(); //SQL语句对象，所在包：org.apache.ibatis.jdbc.SQL
			sql.INSERT_INTO(tableDefinition.getTableName());
			for (String column : columns) {
				if (item.containsKey(column)) {
					sql.VALUES(column, String.format("#{param.%s}", column));// 设置键值
				}
			}*/
			String sqlString = sql.toString();
			sqlString = sqlString.replace("INSERT INTO", "REPLACE INTO");

			//执行sql
			if(!item.containsKey("id")){
				item.put("id",null);
			}
			tableDefinitionMapper.saveData(sqlString, item);
			responseEntity.setValue(item.get("id"));
		}catch (BaseException e){
			e.printStackTrace();
			throw e;
		}finally {
			responseEntity.setTookInMillis(System.currentTimeMillis() - start);
		}
		return responseEntity;
	}
}
