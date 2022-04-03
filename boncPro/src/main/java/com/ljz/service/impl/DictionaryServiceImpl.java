package com.ljz.service.impl;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ljz.mapper.attrC2eMapper;
import com.ljz.mapper.entityC2eMapper;
import com.ljz.model.attrC2e;
import com.ljz.model.attrC2eKey;
import com.ljz.model.entityC2e;
import com.ljz.model.entityC2eKey;
import com.ljz.service.IDictionaryService;

@Service
public class DictionaryServiceImpl implements IDictionaryService{


	@Autowired
	attrC2eMapper aMapper;


	@Autowired
	entityC2eMapper eMapper;

	@Override
	public List<attrC2e> queryColumn(attrC2e key) {
		// TODO Auto-generated method stub
		return aMapper.queryAll(key);
	}

	@Override
	public List<entityC2e> queryTable(entityC2e key) {
		// TODO Auto-generated method stub
		return eMapper.queryAll(key);
	}

	@Override
	public String insertColumn(attrC2e record) {
		// TODO Auto-generated method stub
		String ename = record.getEname();
		String regex = "^\\w+$";
		boolean is = ename.matches(regex);
		if (ename.trim() != null || !ename.trim().equals("") || !ename.trim().isEmpty()){
			if (is == false){
				return "保存失败\n[英文缩写]["+ename+"]只能由数字、字母和下划线组成";
			}
		}
		aMapper.insertSelective(record);
		return "success";
	}

	@Override
	public String updateColumn(attrC2e record) {
		// TODO Auto-generated method stub
		String ename = record.getEname();
		String regex = "^\\w+$";
		boolean is = ename.matches(regex);
		if (ename.trim() != null || !ename.trim().equals("") || !ename.trim().isEmpty()){
			if (is == false){
				return "保存失败\n[英文缩写]["+ename+"]只能由数字、字母和下划线组成";
			}
		}
//		System.out.println(record.toString());
		aMapper.updateByPrimaryKeySelective(record);
		return "success";
	}

	@Override
	public void deleteColumn(attrC2e record) {
		// TODO Auto-generated method stub
		aMapper.deleteByPrimaryKey(record);
	}

	@Override
	public String insertTable(entityC2e record) {
		// TODO Auto-generated method stub
		String regex = "^[a-zA-Z_]+$";
		String ename = record.getEname();
		boolean is = ename.matches(regex);
		if (ename.trim() != null || !ename.trim().equals("") || !ename.trim().isEmpty()){
			if (is == false){
				return "保存失败\n[表名]["+ename+"]只能由字母和下划线组成";
			}
		}
		eMapper.insertSelective(record);
		return "success";
	}

	@Override
	public String updateTable(entityC2e record) {
		// TODO Auto-generated method stub
		String regex = "^[a-zA-Z_]+$";
		String ename = record.getEname();
		boolean is = ename.matches(regex);
		if (ename.trim() != null || !ename.trim().equals("") || !ename.trim().isEmpty()){
			if (is == false){
				return "保存失败\n[表名]["+ename+"]只能由字母和下划线组成";
			}
		}
		System.out.println(record.toString());
		eMapper.updateByPrimaryKeySelective(record);
		return "success";
	}

	@Override
	public void deleteTable(entityC2e record) {
		// TODO Auto-generated method stub
		eMapper.deleteByPrimaryKey(record);
	}



}
