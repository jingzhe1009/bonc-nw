package com.ljz.service;


import java.util.List;

import com.ljz.entity.PageRequest;
import com.ljz.entity.PageResult;
import com.ljz.model.TableList;
import com.ljz.model.TableListKey;
import com.ljz.model.tbWarn;


public interface IWarnService {

	List<tbWarn> selectAll();

	PageResult findPage(PageRequest pageRequest);



}
