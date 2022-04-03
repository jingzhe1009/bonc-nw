package com.ljz.service;


import java.util.List;

import com.ljz.entity.PageRequest;
import com.ljz.entity.PageResult;
import com.ljz.model.DataLog;
import com.ljz.model.TableList;
import com.ljz.model.TableListKey;

public interface ILogService {


	PageResult findPage(PageRequest pageRequest);


	List<DataLog> queryAll(DataLog record);
}
