package com.ljz.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ljz.config.InfoConfig;
import com.ljz.constant.BoncConstant;
import com.ljz.entity.ImportInfo;
import com.ljz.entity.ParamEntity;
import com.ljz.mapper.DataInterface2procMapper;
import com.ljz.mapper.DataInterfaceColumnsMapper;
import com.ljz.mapper.DataInterfaceHistoryMapper;
import com.ljz.mapper.DataInterfaceMapper;
import com.ljz.mapper.DataInterfaceRecordsMapper;
import com.ljz.model.DataInterface;
import com.ljz.model.DataInterface2proc;
import com.ljz.model.DataInterface2procHistory;
import com.ljz.model.DataInterface2procTmp;
import com.ljz.model.DataInterfaceColumnsHistory;
import com.ljz.model.DataInterfaceColumnsTmp;
import com.ljz.model.DataInterfaceHistory;
import com.ljz.model.DataInterfaceRecords;
import com.ljz.model.DataInterfaceRecordsDetail;
import com.ljz.model.DataInterfaceTmp;
import com.ljz.model.DataRvsdRecordTmp;
import com.ljz.model.Order;
import com.ljz.service.IDataInterfaceService;
import com.ljz.util.ExcelUtil;
import com.ljz.util.FileUtil;
import com.ljz.util.InsertDbProduceReturn;
import com.ljz.util.TestProduceReturn;
import com.ljz.util.TimeUtil;

@Service
public class DataInterfaceServiceImpl implements IDataInterfaceService{

	@Autowired
	DataInterfaceMapper mapper;

	@Autowired
	DataInterface2procMapper procMapper;

	@Autowired
	DataInterfaceHistoryMapper hisMapper;
	
	@Autowired
	DataInterfaceRecordsMapper recordsMapper;
	
	@Autowired
	DataInterfaceColumnsMapper colMapper;

	@Autowired
	JdbcTemplate jdbc;

	@Autowired
	TestProduceReturn testProduce;

	@Autowired
	InsertDbProduceReturn dbProduce;

	@Autowired
	InfoConfig config;
	
	@Autowired
    InfoImportService infoImportService;

	private static final Logger logger = LoggerFactory.getLogger(DataInterfaceServiceImpl.class);

	public String date = TimeUtil.getDateToString(TimeUtil.getTy());//?????????
	public String time = FileUtil.formatDate(new Date());//????????????
	

    @Override
    public List<DataInterface> queryDsAndInfaceName(String dataSrc) {
        return  mapper.queryDsAndInfaceName(dataSrc);
    }

	@Override
	public List<DataInterface> queryAll(DataInterface record) {
		// TODO Auto-generated method stub
		return mapper.queryAll(record);
	}

	@Override
	public List<DataInterface> queryByList(String ds, String [] array) {
		// TODO Auto-generated method stub
		return mapper.queryByList(ds, array);
	}

	@Override
	public int insert(DataInterface record) {
		// TODO Auto-generated method stub
		return mapper.insertSelective(record);
	}

	@Override
	public int update(DataInterface record) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int delete(DataInterfaceTmp record) {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(record);
	}

	@Override
	public List<DataInterface2proc> queryProc(DataInterface2proc record) {
		// TODO Auto-generated method stub
		return procMapper.queryAll(record);
	}

	@Override
	public int insertProc(DataInterface2proc record) {
		// TODO Auto-generated method stub
		return procMapper.insertSelective(record);
	}

	@Override
	public int updateProc(DataInterface2proc record) {
		// TODO Auto-generated method stub
		return procMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int deleteProc(DataInterface2procTmp record) {
		// TODO Auto-generated method stub
		return procMapper.deleteByPrimaryKey(record);
	}

	@Override
	public List<DataInterface> queryAllVersion(DataInterface record) {
		// TODO Auto-generated method stub
		List<DataInterface> list = mapper.queryAllVersion(record);
		List<DataInterface> returnList = new ArrayList<DataInterface>();
		for(int i=0;i<list.size();i++) {
			DataInterface data = list.get(i);
			data.setNum("v1."+(i));
			returnList.add(data);
		}
		return returnList;
	}

	/**
	 * ?????????????????????
	 */
	@Override
	public List<DataInterfaceTmp> queryAllTmp(DataInterfaceTmp record) {
		// TODO Auto-generated method stub
		int intUpdateNum=0;
		int intInsertNum=0;
		int intAllNum=0;
		String batchNo = record.getBatchNo();
		String dataSrcAbbr = record.getDataSrcAbbr();
		ExcelUtil obj =ExcelUtil.getInstance();
		Map<String,Object> cache = obj.getEntityMap();
		ImportInfo info = (ImportInfo) cache.get(dataSrcAbbr+batchNo);
		List<DataInterfaceTmp> resultList = new ArrayList<DataInterfaceTmp>();
		List<DataInterfaceTmp> addList = new ArrayList<DataInterfaceTmp>();
		List<DataInterfaceTmp> editList = new ArrayList<DataInterfaceTmp>();
		List<DataInterfaceTmp> sameList = new ArrayList<DataInterfaceTmp>();
		if(batchNo!=null&&!"".equals(batchNo)){
			logger.info("?????????????????????????????????"+record.getDataSrcAbbr());
			//List<DataInterfaceTmp> queryAllTmp = mapper.queryAllTmp(record);
			List<DataInterfaceTmp> queryAllTmp =info.getDataInterfaceTmpList();
			DataInterface data = new DataInterface();
			data.seteDate(TimeUtil.getTw());
			data.setDataSrcAbbr(record.getDataSrcAbbr());
			obj.initInterface(mapper.queryAll(data));
			Map<String,String> interfaceMap =obj.getInterfaceMap(record.getDataSrcAbbr());
			for(DataInterfaceTmp tmp:queryAllTmp){
				String key = tmp.getDataInterfaceName();
				if(interfaceMap!=null&&interfaceMap.containsKey(key)){
					if(tmp.toStr().equalsIgnoreCase(interfaceMap.get(key))){//?????????
						tmp.setImportType("3");
						sameList.add(tmp);
					}else{//??????
						tmp.setImportType("2");
						editList.add(tmp);
						intUpdateNum++;
					}
				}else{//??????
					tmp.setImportType("1");
					intInsertNum++;
					addList.add(tmp);
				}
				intAllNum++;
				resultList.add(tmp);
			}
			//??????????????????
			info.setIntAllNum(intAllNum);
			info.setIntInsertNum(intInsertNum);
			info.setIntUpdateNum(intUpdateNum);
			//????????????????????????????????????
			info.setAddList(addList);
			info.setEditList(editList);
			info.setSameList(sameList);
			cache.put(dataSrcAbbr+batchNo, info);
			
		}
		return resultList;
	}

	@Override
	public int tmpToSave(DataInterfaceTmp record) {
		// TODO Auto-generated method stub
		return mapper.tmpToSave(record);
	}

	@Override
	public List<DataInterface2proc> queryAllVersionProc(DataInterface2proc record) {
		// TODO Auto-generated method stub
		List<DataInterface2proc> list = procMapper.queryAllVersion(record);
		List<DataInterface2proc> returnList = new ArrayList<DataInterface2proc>();
		for(int i=0;i<list.size();i++) {
			DataInterface2proc data = list.get(i);
			data.setNum("v1."+(i));
			returnList.add(data);
		}
		return returnList;
	}

	/**
	 * ?????????????????????????????????
	 */
	@Override
	public List<DataInterface2procTmp> queryAllTmpProc(DataInterface2procTmp record) {
		// TODO Auto-generated method stub
		int procUpdateNum=0;
		int procInsertNum=0;
		int procAllNum=0;
		String dataSrcAbbr = record.getDataSrcAbbr();
		String batchNo = record.getBatchNo();
		//????????????
		ExcelUtil obj =ExcelUtil.getInstance();
		Map<String,Object> cache=obj.getEntityMap();
		ImportInfo info = (ImportInfo) cache.get(dataSrcAbbr+batchNo);
		List<DataInterface2procTmp> resultList = new ArrayList<DataInterface2procTmp>();
		List<DataInterface2procTmp> addList = new ArrayList<DataInterface2procTmp>();
		List<DataInterface2procTmp> editList = new ArrayList<DataInterface2procTmp>();
		if(dataSrcAbbr!=null&&!"".equals(dataSrcAbbr)){
			logger.info("?????????????????????????????????"+record.getDataSrcAbbr());
			List<DataInterface2procTmp> queryAllTmp =info.getDataInterface2procTmpList();
			DataInterface2proc data = new DataInterface2proc();
			data.seteDate(TimeUtil.getTw());
			data.setDataSrcAbbr(record.getDataSrcAbbr());
			obj.initProc(procMapper.queryAll(data));
			Map<String,String> procMap =obj.getProcMap(record.getDataSrcAbbr());
			for(DataInterface2procTmp tmp:queryAllTmp){
				if(procMap!=null&&procMap.containsKey(tmp.getDataSrcAbbr()+tmp.getDataInterfaceNo())){
					if(tmp.toStr().equals(procMap.get(tmp.getDataSrcAbbr()+tmp.getDataInterfaceNo()))){//?????????
						tmp.setImportType("3");
					}else{//??????
						tmp.setImportType("2");
						editList.add(tmp);
						procUpdateNum++;
					}
				}else{//??????
					tmp.setImportType("1");
					addList.add(tmp);
					procInsertNum++;
				}
				resultList.add(tmp);
				procAllNum++;
			}
			//??????????????????
			info.setProcAllNum(procAllNum);
			info.setProcInsertNum(procInsertNum);
			info.setProcUpdateNum(procUpdateNum);
			//????????????????????????????????????
			info.setAddProcList(addList);
			info.setEditProcList(editList);
			cache.put(dataSrcAbbr+batchNo, info);
		}
		return resultList;
	}

	@Override
	public int tmpToSaveProc(DataInterface2procTmp record) {
		// TODO Auto-generated method stub
		return procMapper.tmpToSave(record);
	}

	@Override
	public int tmpToSaveBatch(List<DataInterfaceTmp> list) {
		// TODO Auto-generated method stub
		return mapper.tmpToSaveBatch(list);
	}

	@Override
	public int deleteBatch(List<DataInterfaceTmp> list) {
		// TODO Auto-generated method stub
		return mapper.deleteBatch(list);
	}

	@Override
	public int updateBatch(List<DataInterface> list) {
		// TODO Auto-generated method stub
		return mapper.updateBatch(list);
	}

	/**
	 * ??????????????????
	 */
	@Override
	public List<DataInterface> queryModel(String dataSrcAbbr,String batchNo,DataInterface record) {

		ExcelUtil obj =ExcelUtil.getInstance();
		Map<String,Object> cache=obj.getEntityMap();
		ImportInfo info = (ImportInfo) cache.get(dataSrcAbbr+batchNo);
		List<String> needFileList = info.getNeedFileList();
		// TODO Auto-generated method stub
		List<DataInterface> queryAll = mapper.queryAll(record);
		//?????????????????????????????????

		List<Map<String, Object>> queryIntNum = jdbc.queryForList("select data_interface_name,count(*) as bucket_number from data_interface_columns "
				+"where data_src_abbr='"+record.getDataSrcAbbr()+"' and e_date='"+BoncConstant.CON_E_DATE+"'  group by data_interface_name");
		//????????????sdata????????????
		List<Map<String, Object>> sdataList = new ArrayList<Map<String, Object>>();
		//List<Map<String, Object>> sdataList = jdbc.queryForList("SELECT table_name FROM SYSTEM.tables_v WHERE database_name='"+BoncConstant.SDATA+"'");
		//????????????odata????????????
		List<Map<String, Object>> odataList = new ArrayList<Map<String, Object>>();
		//List<Map<String, Object>> odataList= jdbc.queryForList("SELECT table_name FROM SYSTEM.tables_v WHERE database_name='"+BoncConstant.ODATA+"'");
		List<DataInterface> resultList = new ArrayList<DataInterface>();
		for(DataInterface data:queryAll){
			for(Map<String,Object> map:queryIntNum){
				String data_interface_name = (String)map.get("data_interface_name");
				long bucket_number = (long)map.get("bucket_number");
				if(data_interface_name.equalsIgnoreCase(data.getDataInterfaceName())){
				data.setNum(bucket_number+"");
				}
			}
			boolean isContinue = true;
			for(String din:needFileList) {
				if(din.equals(data.getDataInterfaceName())) {
					isContinue = false;
				}
			}
			if(isContinue)
				continue;
			//????????????0????????????1?????????
			String flag1 = "0";
			for(int i=0;i<sdataList.size();i++){
				Map<String, Object> map = sdataList.get(i);
				String tb = (String) map.get("table_name");
				if(tb.equalsIgnoreCase(data.getExtrnlTableName())){
					data.setCondition("???????????????");
					flag1 = "1";
					break;
				}else{
					if(i==sdataList.size()-1&&"0".equals(flag1)){
						data.setCondition("???????????????");
					}
				}
			}
			String flag2 = "0";
			for(int i=0;i<odataList.size();i++){
				Map<String,Object> map = odataList.get(i);
				String tb = (String) map.get("table_name");
				if(tb.equalsIgnoreCase(data.getIntrnlTableName())){
					data.setCondition(data.getCondition()+",???????????????");
					flag2 = "1";
					break;
				}else{
					if(i==odataList.size()-1&&"0".equals(flag2)){
						data.setCondition(data.getCondition()+",???????????????");
					}
				}
			}
			resultList.add(data);

		}
		return resultList;
	}
	
	public String createRollBackFile(String dataSrc,String batchNo) {
		
		ExcelUtil obj =ExcelUtil.getInstance();
		Map<String,Object> cache=obj.getEntityMap();
		ImportInfo info = (ImportInfo) cache.get(dataSrc+batchNo);

        DataInterface record = new DataInterface();
		String rollBackFilePath = config.getFilePath()+dataSrc+batchNo+"/"+dataSrc+"_ROLLBACK_"+date+".sql";
		StringBuffer rollBackSb = new StringBuffer();

		//record??????????????????????????????????????????
        record.setDataSrcAbbr(dataSrc);
        record.seteDate(TimeUtil.getTw());

        // TODO Auto-generated method stub
        List<DataInterface> queryAll = mapper.queryAll(record);
        //?????????????????????????????????
        List<Map<String, Object>> queryIntNum = info.getQueryIntNum();
        //????????????sdata????????????
        List<Map<String, Object>> sdataList = info.getSdataList();
        //????????????odata????????????
        List<Map<String, Object>> odataList = info.getOdataList();
        List<DataInterface> resultList = new ArrayList<DataInterface>();

//        String rollBackFilePath = config.getFilePath()+record.getDataSrcAbbr()+"_ROLLBACK_"+infoImportService.date+".sql";
//        List<String> rollBackList = new ArrayList<String>();
//        StringBuffer rollBackSb = null;

        for(DataInterface data:queryAll){
            for(Map<String,Object> map:queryIntNum){
                String data_interface_name = (String)map.get("data_interface_name");
                long bucket_number = (long)map.get("bucket_number");
                if(data_interface_name.equalsIgnoreCase(data.getDataInterfaceName())){
                    data.setNum(bucket_number+"");
                }
            }
            //????????????0????????????1?????????
            String flag1 = "0";
            for(int i=0;i<sdataList.size();i++){
                Map<String, Object> map = sdataList.get(i);
                String tb = (String) map.get("table_name");
                if(tb.equalsIgnoreCase(data.getExtrnlTableName())){
                    //?????????????????????

                    data.setCondition("???????????????");
                    flag1 = "1";
                    String rollBackExternalSql = "--"+tb+"--\n"
                            +"CREATE EXTERNAL TABLE SDATA_OLTP."+tb+"_"+"_"+infoImportService.date+" LIKE SDATA_OLTP."+tb+";\n"
                            +"INSERT INTO SDATA_OLTP."+tb+"_"+"_"+infoImportService.date+ " SELECT * FROM SDATA_OLTP."+tb+";\n"
                            +"DROP TABLE IF EXISTS SDATA_OLTP."+tb+";\n"
                            +"CREATE EXTERNAL TABLE SDATA_OLTP."+tb+"\n"
                            +//?????????

                            ";\n"
                            +"INSERT INTO SDATA_OLTP."+tb+" SELECT * FROM SDATA_OLTP."+tb+"_"+"_"+infoImportService.date+";\n\n"
                            ;
//                    rollBackList.add(rollBackExternalSql);
                    rollBackSb.append(rollBackExternalSql);
//                    try {
//                        FileUtil.write(rollBackFilePath, rollBackExternalSql,config.getFileEncode());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    break;
                }else{
                    if(i==sdataList.size()-1&&"0".equals(flag1)){
                        data.setCondition("???????????????");
                    }
                }
            }
            String flag2 = "0";
            for(int i=0;i<odataList.size();i++){
                Map<String,Object> map = odataList.get(i);
                String tb = (String) map.get("table_name");
                if(tb.equalsIgnoreCase(data.getIntrnlTableName())){
                    data.setCondition(data.getCondition()+",???????????????");
                    flag2 = "1";
                    String rollBackSql = "--"+data.getDataInterfaceName()+"--\n"
                            +"CREATE TABLE IF NOT EXISTS ODATA."+tb+"_"+"_"+infoImportService.date+" LIKE ODATA."+tb+";\n"
                            +"INSERT INTO ODATA."+tb+"_"+"_"+infoImportService.date+ " SELECT * FROM ODATA."+tb+";\n"
                            +"DROP TABLE IF EXISTS ODATA."+tb+";\n"
                            +"CREATE TABLE ODATA."+tb+"\n"
                            //?????????

                            +";\n"
                            +"INSERT INTO ODATA."+tb+" SELECT * FROM ODATA."+tb+"_"+"_"+infoImportService.date+";\n\n"
                            ;
                    rollBackSb.append(rollBackSql);
//                    try {
//                        FileUtil.write(rollBackFilePath, rollBackSql,config.getFileEncode());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    break;
                }else{
                    if(i==odataList.size()-1 && "0".equals(flag2)){
                        data.setCondition(data.getCondition()+",???????????????");
                    }
                }
            }
            resultList.add(data);
        }
        if (rollBackSb != null){
            try {
                FileUtil.write(rollBackFilePath, rollBackSb.toString(),config.getFileEncode());
                info.setRollBackFilePath(rollBackFilePath);
                info.setRollBackSb(rollBackSb.toString());
                cache.put(dataSrc+batchNo, info);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return rollBackFilePath;
    }

	@Override
	public void batchImport(ParamEntity param) throws Exception{
		// TODO Auto-generated method stub
	}

	/**
	 * ??????????????????/????????????
	 */
	@Override
	@Transactional
	public String batchImportFinal(ParamEntity param) throws Exception {return null;}

	/**
	 * ??????????????????????????????/????????????
	 */
	@Override
	@Transactional
	public String batchImportProcFinal(ParamEntity param) throws Exception {return null;}

	/**
	 * ????????????????????????
	 */
	@Override
	public Map<String,String> createFile(List<String> list,ParamEntity param) throws Exception {
		String dataSrcAbbr = param.getDataSrcAbbr();
		String batchNo = param.getBatchNo();
		ExcelUtil obj =ExcelUtil.getInstance();
		Map<String,Object> cache=obj.getEntityMap();
		ImportInfo info = (ImportInfo) cache.get(dataSrcAbbr+batchNo);
		Map<String,String> map = new HashMap<String,String>();
		List<String> sucList = new ArrayList<String>();
		List<String> failList = new ArrayList<String>();
		//????????????????????????????????????
		ExecutorService es = Executors.newFixedThreadPool(100);
		// TODO Auto-generated method stub
		for(String table:list) {
			//??????lambda??????runnable??????
			Runnable task = ()->{
				long start = new Date().getTime();
				logger.info(Thread.currentThread().getName()+"?????????????????????????????????,?????????");
				try {
					Order order = testProduce.getSql(table,param.getDataSrcAbbr());
					sucList.add(order.getSql1()+"\n"+order.getSql2()+"\n");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					failList.add(table);
					e.printStackTrace();
				}
				long end = new Date().getTime();
				logger.info("??????????????????:"+(end-start)+"??????");
			};
			es.submit(task);
			logger.info("?????????????????????");
		}
		String DDLFilePath="";
		String DMLFilePath="";
		String filePath = config.getFilePath();

		try {
//			DMLFilePath = config.getFilePath()+"test"+time+"/"+param.getDataSrcAbbr()+"_DML_"+date+".sql";
			DDLFilePath = config.getFilePath()+batchNo+"/"+param.getDataSrcAbbr()+"_DDL_"+date+".sql";
			DMLFilePath = config.getFilePath()+batchNo+"/"+param.getDataSrcAbbr()+"_DML_"+date+".sql";

//			logger.info("??????????????????:"+filePath);
			logger.info("??????????????????:"+DMLFilePath);

			int i =0;
			reWriteFile(list, sucList,failList,filePath, i);
			FileUtil.write(DDLFilePath, info.getDMLInsert(),config.getFileEncode());
			FileUtil.write(DMLFilePath, info.getDMLDeclare(),config.getFileEncode());

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			map.put("msgCode", "1111");
			map.put("msgData", "??????????????????,????????????:\n"/*+filePath+"\n"*/+DMLFilePath+"\n????????????:"+config.getFileEncode());
			return map;
		} finally {
			es.shutdown();
		}
		if(failList.size()>0){
			String str = "??????"+failList.toString()+"????????????????????????,??????????????????"+failList.size();
			map.put("msgCode", "1111");
			map.put("msgData", str);
			return map;
		}
		//??????
		String str = "??????????????????????????????\n??????:"+DMLFilePath+"\n??????:"+list.size()+"?????????:"+list.toString()+"\n";
		map.put("msgCode", "0000");
		map.put("idx", param.getDataSrcAbbr());
		map.put("msgData", str);
		map.put("filePath", filePath);
		map.put("DMLFilePath",DMLFilePath);
		return map;
	}
	//????????????????????????????????????????????????????????????????????????????????????
	private void reWriteFile(List<String> list,List<String> sucList,List<String> failList,String filePath,int i) throws Exception{
		i++;
		Thread.sleep(500);
		if(list.size()==sucList.size()+failList.size()){
			String totalSql="";
			for(String sql :sucList){
				totalSql = totalSql+sql;
			}
			if(!"".equals(totalSql)){
				FileUtil.write(filePath, totalSql,config.getFileEncode());
				logger.info("write file success");
			}
		}else{
			if(i>120){
				//???????????????,????????????
			}else{
				logger.info("waiting for proc running finish"+i);
				reWriteFile(list, sucList,failList,filePath, i);
			}
		}
	}

	/**
	 * ????????????
	 */
	@Override
	public Map<String, String> insertDb(List<String> list,List<String> hasList,ParamEntity param)
			throws Exception {
		// TODO Auto-generated method stub
		String dataSrcAbbr = param.getDataSrcAbbr();
		String batchNo = param.getBatchNo();
		ExcelUtil obj =ExcelUtil.getInstance();
		Map<String,Object> cache=obj.getEntityMap();
		ImportInfo info = (ImportInfo) cache.get(dataSrcAbbr+batchNo);
		Map<String,String> map = new HashMap<String,String>();
		List<String> sucTable = new ArrayList<String>();
		List<String> failTable = new ArrayList<String>();
		List<String> sucList = new ArrayList<String>();
		//????????????????????????????????????
		ExecutorService es = Executors.newFixedThreadPool(100);
		//?????????
		for(String table:list) {
			//??????lambda??????runnable??????
			Runnable task = ()->{
				logger.info(Thread.currentThread().getName()+"?????????????????????????????????,?????????");
				Order order = new Order();
				try {
					long start2 = new Date().getTime();
					//order = dbProduce.getSql(table,param.getDataSrcAbbr());
					order.setSql1("SELECT NB");
					order.setSql2("SELECT WB");
					if(order.getSql1()==null||"".equals(order.getSql1())) {
						System.out.println(order.getSql1());
					}
					if(order.getSql2()==null||"".equals(order.getSql2())) {
						System.out.println(order.getSql2());
					}
					sucList.add(order.getSql1()+"\n"+order.getSql2()+"\n");
					long end2 = new Date().getTime();
					logger.info("??????????????????:"+(end2-start2)+"??????");
					logger.info(table+"????????????");
					sucTable.add(table);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					//logger.info(table+"????????????,1.????????????????????????????????????\n2.?????????????????????\n3.?????????????????????????????????\n4.???????????????????????????????????????????????????");
					logger.info(table+"????????????"+e.getMessage());
					logger.info(order.getSql1());
					logger.info(order.getSql2());
					failTable.add(table);
				}
				//??????submit?????????????????????????????????
			};
			es.submit(task);
			logger.info("?????????????????????");
		}
		//System.out.println("cacheMap="+cacheMap.keySet());
		int i =0;
		complete(list,sucTable,failTable,i);
		es.shutdown();
		
		
		String str = "";
		if(list.size()>0) {
			str = "????????????????????????,???????????????????????????!?????????????????????:"+list.toString()+",??????:"+list.size()+"?????????\n";
		}else {
			str = "??????????????????????????????????????????";
		}
		
		if(failTable.size()>0){
			str =str +"????????????"+failTable.toString()+"????????????,??????:"+failTable.size()+"\n1.????????????????????????????????????\n2.?????????????????????\n3.?????????????????????????????????\n4.?????????????????????????????????????????????????????????\n";
		}
		if(hasList.size()>0){
			str = str +"??????"+hasList.toString()+"?????????,???????????????????????????????????????????????????";
		}
		
		//?????????
		String DDLFilePath ="";
		String DMLFilePath = "";
		//String filePath =  config.getFilePath()+batchNo+"/"+param.getDataSrcAbbr()+"_CREATE_"+date+".sql";
		try {
			DDLFilePath = config.getFilePath()+dataSrcAbbr+batchNo+"/"+dataSrcAbbr+"_DDL_"+date+".sql";
			DMLFilePath = config.getFilePath()+dataSrcAbbr+batchNo+"/"+dataSrcAbbr+"_DML_"+date+".sql";
			logger.info("??????????????????:"+DMLFilePath);
			String totalSql="";
			for(String sql :sucList){
				totalSql = totalSql+sql;
			}
			if(!"".equals(totalSql)){
				//FileUtil.write(filePath, totalSql,config.getFileEncode());
				//logger.info("write file success");
			}
			//insert??????
			FileUtil.write(DDLFilePath, totalSql+info.getDMLInsert(),config.getFileEncode());
			//??????
			FileUtil.write(DMLFilePath,info.getDMLDeclare(),config.getFileEncode());
			info.setDMLInsertPath(DDLFilePath);
			info.setDMLFilePath(DMLFilePath);
			cache.put(dataSrcAbbr+batchNo, info);
			//??????
			createRollBackFile(param.getDataSrcAbbr(),param.getBatchNo());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			map.put("msgCode", "1111");
			map.put("msgData", "??????????????????,????????????:\n"/*+filePath+"\n"*/+DMLFilePath+"\n????????????:"+config.getFileEncode());
			return map;
		}
		map.put("msgData", str);
		map.put("msgCode", "0000");
		map.put("idx", param.getDataSrcAbbr());
		return map;
	}

	//????????????????????????
	private void complete(List<String> list,List<String> sucTable,List<String> failTable,int i) throws Exception{
		i++;
		Thread.sleep(500);
		if(list.size()==sucTable.size()+failTable.size()){
			//???????????????????????????=????????????+????????????????????????????????????????????????
		}else{
			if(i>120){
				//???????????????,????????????
			}else{
				logger.info("waiting for proc running finish"+i);
				complete(list,sucTable,failTable,i);
			}
		}
	}
	
	@Override
	public List<DataInterfaceHistory> queryInterfaceCompare(DataInterfaceHistory record) {
		String dataSrcAbbr = record.getDataSrcAbbr();
		String batchNo = record.getExptSeqNbr();
		ExcelUtil obj = ExcelUtil.getInstance();
		Map<String,Object> cache=obj.getEntityMap();
		ImportInfo info = (ImportInfo) cache.get(dataSrcAbbr+batchNo);
		List<String> needFileList = new ArrayList<String>();
		//?????????????????????
		List<String> needCreateList = new ArrayList<String>();
		List<DataInterfaceHistory> resultList = new ArrayList<DataInterfaceHistory>();
		Map<String,Object> msgMap = new HashMap<String,Object>();
		List<DataInterfaceTmp> queryAllTmp =info.getDataInterfaceTmpList();
		DataInterface data = new DataInterface();
		data.seteDate(TimeUtil.getTw());
		data.setDataSrcAbbr(dataSrcAbbr);
		List<DataInterface> queryAll = mapper.queryAll(data);
		int addCount =0;
		try {
			for(DataInterfaceTmp tmp : queryAllTmp) {
				List<String> msgList = new ArrayList<String>();//?????????????????????
				String red = "";//????????????
				boolean isAdd = true;
				for(DataInterface pro:queryAll) {
					if(tmp.getDataInterfaceName().equals(pro.getDataInterfaceName())) {
						isAdd = false;
						if(tmp.toStr().equals(pro.toStr())) {//?????????
							
							List<DataInterfaceColumnsTmp> queryAllTmpCol =info.getDataInterfaceColumnsTmpList();
							Map<String,String> columnMap =obj.getColumnMap(record.getDataSrcAbbr());
							DataInterfaceHistory history = new DataInterfaceHistory();
							for(DataInterfaceColumnsTmp colTmp:queryAllTmpCol){
								if(!colTmp.getDataInterfaceName().equals(tmp.getDataInterfaceName()))
									continue;
								String colKey = colTmp.getDataInterfaceName()+colTmp.getColumnNo();
								if(columnMap!=null&&columnMap.containsKey(colKey)){
									if(colTmp.toStr().equalsIgnoreCase(columnMap.get(colKey))){//?????????
									}else{//????????????
										history.setFlag("2");//??????
				                		msgList.add("????????????["+colTmp.getColumnComment()+"]");
				                		//break;
									}
								}else{//????????????
									history.setFlag("2");//??????
			                		msgList.add("????????????["+colTmp.getColumnComment()+"]");
			                		//break;
								}
							}
							if("2".equals(history.getFlag())) {
								DataInterfaceHistory history2 = new DataInterfaceHistory();
								BeanUtils.copyProperties(pro, history2);
								history2.setFlag("0");
								history2.setRed("");
								resultList.add(history2);
								BeanUtils.copyProperties(tmp, history);
								history.setFlag("2");
								history.setRed("");
								resultList.add(history);
								needFileList.add(tmp.getDataInterfaceName());
							}
						}else {//??????
							Field[] fields = pro.getClass().getDeclaredFields();
							Field[] fields2 = tmp.getClass().getDeclaredFields();
							for(int i=0; i<fields.length; i++){  
					            Field f = fields[i];  
					            f.setAccessible(true);  
					            //System.out.println("?????????:" + f.getName() + " ?????????:" + f.get(pro));  
					            if("serialVersionUID".equals(f.getName())||"sDate".equals(f.getName())||"eDate".equals(f.getName()))
				                	continue;
					            for(int j=0; j<fields2.length; j++){  
					                Field f2 = fields2[j];  
					                f2.setAccessible(true);  
					                //System.out.println("tmp?????????:" + f2.getName() + " tmp?????????:" + f2.get(tmp)); 
					                if(f.getName()==null||f2.getName()==null||f.get(pro)==null||f2.get(tmp)==null)
					                	continue;
					                if(f.getName().equals(f2.getName())) {
					                	if(f.get(pro)!=f2.get(tmp)&&!f.get(pro).equals(f2.get(tmp))) {
					                		red +="'"+f2.get(tmp)+"',";
					                		msgList.add("??????????????????["+f.getName()+"]???"+f2.get(tmp));
					                	}
					                }
					            }
					        }
							DataInterfaceHistory history = new DataInterfaceHistory();
							BeanUtils.copyProperties(pro, history);
							history.setFlag("0");
							history.setRed("");
							resultList.add(history);
							DataInterfaceHistory history2 = new DataInterfaceHistory();
							BeanUtils.copyProperties(tmp, history2);
							history2.setFlag("2");
							history2.setRed(red);
							resultList.add(history2);
							needFileList.add(tmp.getDataInterfaceName());
						}
					}
				}
				if(isAdd) {//??????
					DataInterfaceHistory history = new DataInterfaceHistory();
					history.setFlag("0");
					history.setRed("");
					resultList.add(history);
					DataInterfaceHistory history2 = new DataInterfaceHistory();
					BeanUtils.copyProperties(tmp, history2);
					history2.setFlag("3");
					history2.setRed("");
					resultList.add(history2);
					msgList.add("????????????"+tmp.getDataInterfaceDesc());
					addCount++;
					needCreateList.add(tmp.getDataInterfaceName());
					needFileList.add(tmp.getDataInterfaceName());
				}
				msgMap.put(tmp.getDataInterfaceName(), msgList);
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(queryAllTmp.size()-addCount<queryAll.size()) {
			for(DataInterface pro:queryAll) {
				List<String> msgList = new ArrayList<String>();//?????????????????????
				boolean isLack = true;
				for(DataInterfaceTmp tmp:queryAllTmp) {
					if(tmp.getDataInterfaceName().equals(pro.getDataInterfaceName())) {
						isLack = false;
					}
				}
				if(isLack) {
					DataInterfaceHistory history = new DataInterfaceHistory();
					BeanUtils.copyProperties(pro, history);
					history.setFlag("0");
					history.setRed("");
					resultList.add(history);
					DataInterfaceHistory history2 = new DataInterfaceHistory();
					history2.setFlag("4");
					history2.setRed("");
					resultList.add(history2);
					msgList.add("????????????"+pro.getDataInterfaceDesc());
					//needFileList.add(pro.getDataInterfaceName());
				}
				msgMap.put(pro.getDataInterfaceName(), msgList);
			}
		}
		info.setMsgMap(msgMap);
		info.setNeedCreateList(needCreateList);
		info.setNeedFileList(needFileList);
		cache.put(dataSrcAbbr+batchNo, info);
		return resultList;
	}
	
	
	@SuppressWarnings("unchecked")
	/**
	 * ????????????
	 */
	@Override
	@Transactional
	public String saveAll(ParamEntity param) throws Exception {
		String dataSrcAbbr = param.getDataSrcAbbr();
		String batchNo = param.getBatchNo();//???????????????
		String needVrsnNbr = "";//?????????
        String exptSeqNbr = ""; //?????????
		
		//?????????????????????????????????????????????map??????????????????
		ExcelUtil obj = ExcelUtil.getInstance();
		Map<String, Object> cache = obj.getEntityMap();
		ImportInfo info = (ImportInfo) cache.get(dataSrcAbbr+batchNo);

		//???????????????????????????
		DataRvsdRecordTmp dataRvsdRecordTmp=info.getDataRvsdRecordTmp();
		needVrsnNbr = dataRvsdRecordTmp.getNeedVrsnNbr();
		exptSeqNbr = dataRvsdRecordTmp.getExptSeqNbr();
		//??????????????????
		initHistory(needVrsnNbr,exptSeqNbr);
		
		logger.info("??????????????????...?????????=["+dataSrcAbbr+"],?????????=["+batchNo+"],?????????=["+needVrsnNbr+"],?????????=["+exptSeqNbr+"]");
		List<DataInterfaceTmp> queryAllTmpInt = info.getDataInterfaceTmpList();
		List<DataInterfaceTmp> addList = info.getAddList();
		List<DataInterfaceTmp> editList = info.getEditList();
		List<DataInterfaceColumnsTmp> addColList = info.getAddColList();
		List<DataInterfaceColumnsTmp> editColList = info.getEditColList();
		List<DataInterface2procTmp> addProcList = info.getAddProcList();
		List<DataInterface2procTmp> editProcList = info.getEditProcList();
		Map<String,Object> map =info.getMsgMap();
		/**
		 * ??????
		 */
		StringBuffer DMLDeclare = new StringBuffer();
		StringBuffer DMLInsert = new StringBuffer();
		//??????
        if(addList.size()>0){
        	//??????
			logger.info("batch insert interface success:"+mapper.batchInsertProFromTmp(addList));
			//??????
			List<DataInterfaceHistory> addHistoryList = new ArrayList<DataInterfaceHistory>();
			for(DataInterfaceTmp tmp:addList) {
				DataInterfaceHistory hisData = new DataInterfaceHistory();
				BeanUtils.copyProperties(tmp, hisData);
				hisData.setNeedVrsnNbr(needVrsnNbr);
				hisData.setExptSeqNbr(exptSeqNbr);
				addHistoryList.add(hisData);
				//??????sql
				DMLInsert.append("INSERT INTO SDATA_OLTP_CFG.DATA_INTERFACE VALUES('"+tmp.getDataSrcAbbr()+"','"+tmp.getDataInterfaceNo()+"','"+tmp.getDataInterfaceName()
				+"','"+tmp.getDataInterfaceDesc()+"','"+tmp.getDataLoadFreq()+"','"+tmp.getDataLoadMthd()+"','"+tmp.getFiledDelim()
				+"','"+tmp.getLineDelim()+"','"+tmp.getExtrnlDatabaseName()+"','"+tmp.getIntrnlDatabaseName()+"','"+tmp.getExtrnlTableName()
				+"','"+tmp.getIntrnlTableName()+"','"+tmp.getTableType()+"',"+tmp.getBucketNumber()+",'"+TimeUtil.getDate(tmp.getsDate())
				+"','"+TimeUtil.getDate(tmp.geteDate())+"');"
				+"\n\n");
				DMLDeclare.append("DECLARE \n"
						+"o_extrnl_table_ddl STRING\n"
						+"o_intrnl_table_ddl STRING\n"
						+"BEGIN\n"
						+"pkg_ruku_ddl.pro_sp_ddl('"+tmp.getDataSrcAbbr()+"','"+tmp.getDataInterfaceName()+"',o_extrnl_table_ddl,o_intrnl_table_ddl)\n"
						+"DBMS_OUTPUT.PUT_LINE(o_extrnl_table_ddl)\n"
						+"DBMS_OUTPUT.PUT_LINE(o_intrnl_table_ddl)\n"
						+"END"
						+"\n\n");
			}
			logger.info("all batch insert interface history success:"+mapper.batchInsertHis(addHistoryList));
		}
        //??????
        if(editList.size()>0){
        	List<Object[]> updateList=new ArrayList<Object[]>();
        	List<DataInterfaceHistory> editHistoryList = new ArrayList<DataInterfaceHistory>();
        	for(DataInterfaceTmp tmp:editList){
        		//??????
        		updateList.add(new Object[] {new Date(),tmp.getDataInterfaceName()});
        		//??????
        		DataInterfaceHistory hisData = new DataInterfaceHistory();
				BeanUtils.copyProperties(tmp, hisData);
				hisData.setNeedVrsnNbr(needVrsnNbr);
				hisData.setExptSeqNbr(exptSeqNbr);
				editHistoryList.add(hisData);
        		//??????sql
        		DMLInsert.append("UPDATE SDATA_OLTP_CFG.DATA_INTERFACE SET e_date='"+TimeUtil.getDateToString(TimeUtil.getToday())
                +"' where data_interface_name = '"+tmp.getDataInterfaceName()+"' and e_date ='3000-12-31';\n"
                +"INSERT INTO SDATA_OLTP_CFG.DATA_INTERFACE VALUES('"+tmp.getDataSrcAbbr()+"','"+tmp.getDataInterfaceNo()+"','"+tmp.getDataInterfaceName()
                +"','"+tmp.getDataInterfaceDesc()+"','"+tmp.getDataLoadFreq()+"','"+tmp.getDataLoadMthd()+"','"+tmp.getFiledDelim()
                +"','"+tmp.getLineDelim()+"','"+tmp.getExtrnlDatabaseName()+"','"+tmp.getIntrnlDatabaseName()+"','"+tmp.getExtrnlTableName()
                +"','"+tmp.getIntrnlTableName()+"','"+tmp.getTableType()+"',"+tmp.getBucketNumber()+",'"+TimeUtil.getDate(tmp.getsDate())
                +"','"+TimeUtil.getDate(tmp.geteDate())+"');"
                +"\n\n");
		        DMLDeclare.append("DECLARE \n"
		                +"o_extrnl_table_ddl STRING\n"
		                +"o_intrnl_table_ddl STRING\n"
		                +"BEGIN\n"
		                +"pkg_ruku_ddl.pro_sp_ddl('"+tmp.getDataSrcAbbr()+"','"+tmp.getDataInterfaceName()+"',o_extrnl_table_ddl,o_intrnl_table_ddl)\n"
		                +"DBMS_OUTPUT.PUT_LINE(o_extrnl_table_ddl)\n"
		                +"DBMS_OUTPUT.PUT_LINE(o_intrnl_table_ddl)\n"
		                +"END"
		                +"\n\n");
        	}
        	//??????
        	String updateSql = "update data_interface set e_date = ? where data_interface_name = ? and e_date ='3000-12-31' ";
        	logger.info("all batch update interface success:"+jdbc.batchUpdate(updateSql, updateList).length);
			logger.info("all batch updateinsert interface success:"+mapper.batchInsertProFromTmp(editList));
			//??????
			String updateSqlHis = "update data_interface_history set e_date = ? where data_interface_name = ? and e_date ='3000-12-31' and need_vrsn_nbr='"+needVrsnNbr+"' and expt_seq_nbr='"+exptSeqNbr+"'";
			logger.info("all batch update interface history success:"+jdbc.batchUpdate(updateSqlHis, updateList).length);
			logger.info("all batch updateinsert interface history success:"+mapper.batchInsertHis(editHistoryList));
        }
        /**
         * ??????
         */
        //??????
        if(addColList.size()>0){
        	//??????
			logger.info("all batch insert col success:"+colMapper.batchInsertProFromTmp(addColList));
			//??????
			List<DataInterfaceColumnsHistory> addHistoryList = new ArrayList<DataInterfaceColumnsHistory>();
			for(DataInterfaceColumnsTmp tmp:addColList) {
				DataInterfaceColumnsHistory hisData = new DataInterfaceColumnsHistory();
				BeanUtils.copyProperties(tmp, hisData);
				hisData.setNeedVrsnNbr(needVrsnNbr);
				hisData.setExptSeqNbr(exptSeqNbr);
				addHistoryList.add(hisData);
				DMLInsert.append("INSERT INTO SDATA_OLTP_CFG.DATA_INTERFACE_COLUMNS VALUES('"+tmp.getDataSrcAbbr()+"','"+tmp.getDataInterfaceNo()
				+"','"+tmp.getDataInterfaceName()+"','"+tmp.getColumnNo()+"','"+tmp.getColumnName()+"','"+tmp.getDataType()
				+"','"+tmp.getDataFormat()+"','"+tmp.getNullable()+"','"+tmp.getReplacenull()+"','"+tmp.getComma()
				+"','"+tmp.getColumnComment()+"','"+tmp.getIsbucket()+"','"+tmp.getIskey()+"','"+tmp.getIsvalid()
				+"','"+tmp.getIncrementfield()+"','"+TimeUtil.getDate(tmp.getsDate())+"','"+TimeUtil.getDate(tmp.geteDate())+"');"
				+"\n\n");
				
			}
			logger.info("all batch insert col history success:"+colMapper.batchInsertHis(addHistoryList));
		}
        //??????
        if(editColList.size()>0){
        	List<Object[]> colUpdateList=new ArrayList<Object[]>();
        	List<DataInterfaceColumnsHistory> editHistoryList = new ArrayList<DataInterfaceColumnsHistory>();
        	for(DataInterfaceColumnsTmp tmp:editColList){
        		//??????
        		colUpdateList.add(new Object[] {new Date(),tmp.getDataInterfaceName(),tmp.getColumnNo()});
        		//??????
        		DataInterfaceColumnsHistory hisData = new DataInterfaceColumnsHistory();
				BeanUtils.copyProperties(tmp, hisData);
				hisData.setNeedVrsnNbr(needVrsnNbr);
				hisData.setExptSeqNbr(exptSeqNbr);
				editHistoryList.add(hisData);
				//??????
        		DMLInsert.append("UPDATE SDATA_OLTP_CFG.DATA_INTERFACE_COLUMNS SET e_date='"+TimeUtil.getDateToString(TimeUtil.getToday())
                +"' where data_interface_name = '"+tmp.getDataInterfaceName()+"' and e_date ='3000-12-31';\n"
                +"INSERT INTO SDATA_OLTP_CFG.DATA_INTERFACE_COLUMNS VALUES('"+tmp.getDataSrcAbbr()+"','"+tmp.getDataInterfaceNo()
                +"','"+tmp.getDataInterfaceName()+"','"+tmp.getColumnNo()+"','"+tmp.getColumnName()+"','"+tmp.getDataType()
                +"','"+tmp.getDataFormat()+"','"+tmp.getNullable()+"','"+tmp.getReplacenull()+"','"+tmp.getComma()
                +"','"+tmp.getColumnComment()+"','"+tmp.getIsbucket()+"','"+tmp.getIskey()+"','"+tmp.getIsvalid()
                +"','"+tmp.getIncrementfield()+"','"+TimeUtil.getDate(tmp.getsDate())+"','"+TimeUtil.getDate(tmp.geteDate())+"');"
                +"\n\n");
        	}
        	//??????
        	String colUpdateSql = "update data_interface_columns set e_date = ? where data_interface_name = ? and column_no = ? and e_date='3000-12-31' ";
        	logger.info("all batch update col success:"+jdbc.batchUpdate(colUpdateSql, colUpdateList).length);
			logger.info("all batch updateinsert col success:"+colMapper.batchInsertProFromTmp(editColList));
			//??????
			String updateSqlHis = "update data_interface_columns_history set e_date = ? where data_interface_name = ? and column_no = ? and e_date ='3000-12-31' and need_vrsn_nbr='"+needVrsnNbr+"' and expt_seq_nbr='"+exptSeqNbr+"'";
			logger.info("all batch update col history success:"+jdbc.batchUpdate(updateSqlHis, colUpdateList).length);
			logger.info("all batch updateinsert col history success:"+colMapper.batchInsertHis(editHistoryList));
        }
        /**
		 * ??????
		 */
		//??????
        if(addProcList.size()>0){
        	//??????
			logger.info("all batch insert proc success:"+procMapper.batchInsertProFromTmp(addProcList));
			//??????
			List<DataInterface2procHistory> addHistoryList = new ArrayList<DataInterface2procHistory>();
			for(DataInterface2procTmp tmp:addProcList) {
				DataInterface2procHistory hisData = new DataInterface2procHistory();
				BeanUtils.copyProperties(tmp, hisData);
				hisData.setNeedVrsnNbr(needVrsnNbr);
				hisData.setExptSeqNbr(exptSeqNbr);
				addHistoryList.add(hisData);
				//??????
				DMLInsert.append("INSERT INTO SDATA_OLTP_CFG.data_interface2proc VALUES('"+tmp.getDataSrcAbbr()+"','"+tmp.getDataInterfaceNo()
				+"','"+tmp.getProcDatabaseName()+"','"+tmp.getProcName()+"','"+TimeUtil.getDate(tmp.getsDate())
				+"','"+TimeUtil.getDate(tmp.geteDate())+"');"
				+"\n\n");
			}
			logger.info("all batch insert proc history success:"+procMapper.batchInsertHis(addHistoryList));
		}
        //??????
        if(editProcList.size()>0){
        	List<Object[]> procUpdateList=new ArrayList<Object[]>();
        	List<DataInterface2procHistory> editHistoryList = new ArrayList<DataInterface2procHistory>();
        	for(DataInterface2procTmp tmp:editProcList){
        		//??????
        		procUpdateList.add(new Object[] {new Date(),tmp.getDataSrcAbbr(),tmp.getDataInterfaceNo()});
        		//??????
        		DataInterface2procHistory hisData = new DataInterface2procHistory();
				BeanUtils.copyProperties(tmp, hisData);
				hisData.setNeedVrsnNbr(needVrsnNbr);
				hisData.setExptSeqNbr(exptSeqNbr);
				editHistoryList.add(hisData);
				//??????
        		DMLInsert.append("UPDATE SDATA_OLTP_CFG.data_interface2proc SET e_date='"+TimeUtil.getDateToString(TimeUtil.getToday())
				+"' where data_src_abbr = "+tmp.getDataSrcAbbr()+"and data_interface_no = '"+tmp.getDataInterfaceNo()+"' and e_date ='3000-12-31';\n"
				+"INSERT INTO SDATA_OLTP_CFG.data_interface2proc VALUES('"+tmp.getDataSrcAbbr()+"','"+tmp.getDataInterfaceNo()
				+"','"+tmp.getProcDatabaseName()+"','"+tmp.getProcName()+"','"+TimeUtil.getDate(tmp.getsDate())
				+"','"+TimeUtil.getDate(tmp.geteDate())+"');"
				+"\n\n");
        	}
        	//??????
        	String procUpdateSql = "update data_interface2proc set e_date = ? where data_src_abbr = ? and data_interface_no = ? and e_date ='3000-12-31' ";
        	logger.info("all batch update proc success:"+jdbc.batchUpdate(procUpdateSql, procUpdateList).length);
			logger.info("all batch updateinsert proc success:"+procMapper.batchInsertProFromTmp(editProcList));
			//??????
			String updateSqlHis = "update data_interface2proc_history set e_date = ? where data_src_abbr = ? and data_interface_no = ? and e_date ='3000-12-31' and need_vrsn_nbr='"+needVrsnNbr+"' and expt_seq_nbr='"+exptSeqNbr+"'";
			logger.info("all batch update proc history success:"+jdbc.batchUpdate(updateSqlHis, procUpdateList).length);
			logger.info("all batch updateinsert proc history success:"+procMapper.batchInsertHis(editHistoryList));
        }
        
        /**
		 * ?????????
		 */
		DataInterfaceRecords records = new DataInterfaceRecords();
		records.setNeedVrsnNbr(needVrsnNbr);
		records.setExptSeqNbr(exptSeqNbr);
		records.setDataSrcAbbr(dataSrcAbbr);
		records.setExptFileName(dataRvsdRecordTmp.getFileName());
		records.setIntfTot(queryAllTmpInt.size()+"");
		records.setIntfNew(addList.size()+"");
		records.setIntfAlt(editList.size()+"");
		records.setCreateDate(dataRvsdRecordTmp.getsDate());
		records.setAltDate(dataRvsdRecordTmp.getsDate());
		records.setExctPsn(dataRvsdRecordTmp.getExctPsn());
		records.setExptDate(dataRvsdRecordTmp.getsDate());
		records.setIntfDscr(dataRvsdRecordTmp.getIntfDscr());
		recordsMapper.insertSelective(records);
		/**
		 * ???????????????
		 */
		for(Map.Entry<String, Object> entry:map.entrySet()) {
			String key = entry.getKey();
			List<String> list = (List<String>) entry.getValue();
			for(Object o:list) {
				String msg = (String) o;
				jdbc.update(" insert into data_interface_records_detail (need_vrsn_nbr,expt_seq_nbr,data_src_abbr,data_interface_name,data_change,expt_date) values ('"+needVrsnNbr+"','"+exptSeqNbr+"','"+dataSrcAbbr+"','"+key+"','"+msg+"','"+dataRvsdRecordTmp.getsDate()+"')");
			}
		}
		obj.clearColumn(dataSrcAbbr);
		obj.clearInterface(dataSrcAbbr);
		obj.clearProc(dataSrcAbbr);
		info.setDMLDeclare(DMLDeclare.toString());
		info.setDMLInsert(DMLInsert.toString());
		cache.put(dataSrcAbbr+batchNo, info);
		return "success";
		
	}
	
	private void initHistory(String needVrsnNbr,String exptSeqNbr) {
		/**
		 * ?????????????????????
		 */
		List<Map<String, Object>> intHistoryList = jdbc.queryForList("select * from  data_interface_history where e_date = '3000-12-31' ");
		if(intHistoryList.size()<1) {//????????????????????????
			String sql = "insert into data_interface_history  (need_vrsn_nbr, expt_seq_nbr, data_src_abbr, \r\n"
					+ "      data_interface_no, data_interface_name, data_interface_desc, \r\n"
					+ "      data_load_freq, data_load_mthd, filed_delim, \r\n"
					+ "      line_delim, extrnl_database_name, intrnl_database_name, \r\n"
					+ "      extrnl_table_name, intrnl_table_name, table_type, \r\n"
					+ "      bucket_number, s_date, e_date\r\n"
					+ "      ) select '1.0','1.0.0', b.data_src_abbr, b.data_interface_no, b.data_interface_name, \r\n"
					+ "    b.data_interface_desc, b.data_load_freq, b.data_load_mthd, b.filed_delim, b.line_delim, b.extrnl_database_name, \r\n"
					+ "    b.intrnl_database_name, b.extrnl_table_name, b.intrnl_table_name, b.table_type, b.bucket_number, \r\n"
					+ "    b.s_date, b.e_date from data_interface b where b.e_date='3000-12-31'";
			jdbc.batchUpdate(sql);
		}else {
			List<Map<String, Object>> intHistoryList2 = jdbc.queryForList("select * from  data_interface_history where e_date = '3000-12-31' and need_vrsn_nbr='"+needVrsnNbr+"' and expt_seq_nbr='"+exptSeqNbr+"' ");
			if(intHistoryList2.size()<1) {//????????????????????????
				String sql = "insert into data_interface_history  (need_vrsn_nbr, expt_seq_nbr, data_src_abbr, \r\n"
						+ "      data_interface_no, data_interface_name, data_interface_desc, \r\n"
						+ "      data_load_freq, data_load_mthd, filed_delim, \r\n"
						+ "      line_delim, extrnl_database_name, intrnl_database_name, \r\n"
						+ "      extrnl_table_name, intrnl_table_name, table_type, \r\n"
						+ "      bucket_number, s_date, e_date\r\n"
						+ "      ) select '"+needVrsnNbr+"','"+exptSeqNbr+"', b.data_src_abbr, b.data_interface_no, b.data_interface_name, \r\n"
						+ "    b.data_interface_desc, b.data_load_freq, b.data_load_mthd, b.filed_delim, b.line_delim, b.extrnl_database_name, \r\n"
						+ "    b.intrnl_database_name, b.extrnl_table_name, b.intrnl_table_name, b.table_type, b.bucket_number, \r\n"
						+ "    b.s_date, b.e_date from data_interface b where b.e_date='3000-12-31' ";
				jdbc.batchUpdate(sql);
			}
		}
		List<Map<String, Object>> colHistoryList = jdbc.queryForList("select *  from data_interface_columns_history where e_date = '3000-12-31' ");
		if(colHistoryList.size()<1) {
			String sql = "insert into data_interface_columns_history  (need_vrsn_nbr, expt_seq_nbr, data_src_abbr, data_interface_no, data_interface_name, \r\n"
					+ "    column_no, column_name, data_type, data_format, nullable, replacenull, comma, column_comment, \r\n"
					+ "    isbucket, s_date, e_date"
					+ "      ) select '1.0','1.0.0', b.data_src_abbr,b.data_interface_no,b.data_interface_name,b.\r\n"
					+ "    column_no,b.column_name,b.data_type,b.data_format,b.nullable,b.replacenull,b.comma,b.column_comment,b.\r\n"
					+ "    isbucket,b.s_date,b.e_date from data_interface_columns b where b.e_date='3000-12-31'";
			jdbc.batchUpdate(sql);
		}else {
			List<Map<String, Object>> colHistoryList2 = jdbc.queryForList("select *  from data_interface_columns_history where e_date = '3000-12-31' and need_vrsn_nbr='"+needVrsnNbr+"' and expt_seq_nbr='"+exptSeqNbr+"' ");
			if(colHistoryList2.size()<1) {
				String sql = "insert into data_interface_columns_history  (need_vrsn_nbr, expt_seq_nbr, data_src_abbr, data_interface_no, data_interface_name, \r\n"
						+ "    column_no, column_name, data_type, data_format, nullable, replacenull, comma, column_comment, \r\n"
						+ "    isbucket, s_date, e_date"
						+ "      ) select '"+needVrsnNbr+"','"+exptSeqNbr+"', b.data_src_abbr,b.data_interface_no,b.data_interface_name,b.\r\n"
						+ "    column_no,b.column_name,b.data_type,b.data_format,b.nullable,b.replacenull,b.comma,b.column_comment,b.\r\n"
						+ "    isbucket,b.s_date,b.e_date from data_interface_columns b where b.e_date='3000-12-31'";
				jdbc.batchUpdate(sql);
			}
		}
		List<Map<String, Object>> procHistoryList = jdbc.queryForList("select *  from data_interface2proc_history where e_date = '3000-12-31' ");
		if(procHistoryList.size()<1) {
			String sql = "insert into data_interface2proc_history  (need_vrsn_nbr, expt_seq_nbr, data_src_abbr, data_interface_no,proc_database_name,proc_name,s_date,e_date) "
					+ "  select '1.0','1.0.0', b.data_src_abbr,b.data_interface_no,b.proc_database_name,b.proc_name,b.s_date,b.e_date from data_interface2proc b where b.e_date='3000-12-31'";
			jdbc.batchUpdate(sql);
		}else {
			List<Map<String, Object>> procHistoryList2 = jdbc.queryForList("select *  from data_interface2proc_history where e_date = '3000-12-31' and need_vrsn_nbr='"+needVrsnNbr+"' and expt_seq_nbr='"+exptSeqNbr+"' ");
			if(procHistoryList2.size()<1) {
				String sql = "insert into data_interface2proc_history  (need_vrsn_nbr, expt_seq_nbr, data_src_abbr, data_interface_no,proc_database_name,proc_name,s_date,e_date) "
						+ "  select '"+needVrsnNbr+"','"+exptSeqNbr+"', b.data_src_abbr,b.data_interface_no,b.proc_database_name,b.proc_name,b.s_date,b.e_date from data_interface2proc b where b.e_date='3000-12-31'";
				jdbc.batchUpdate(sql);
			}
		}
	}
	
	
	@Override
	public List<DataInterfaceRecords> queryRecord(DataInterfaceRecords record) {
		return recordsMapper.queryAll(record);
	}

	@Override
	public List<DataInterfaceRecordsDetail> queryLastFive(DataInterfaceRecordsDetail record) {
		return recordsMapper.queryLastFive(record);
	}


}
