package com.ljz.service;


import java.util.List;

import com.ljz.entity.PageRequest;
import com.ljz.entity.PageResult;
import com.ljz.model.TableList;
import com.ljz.model.TableListKey;

public interface ITableListService {


	/**
     * 分页查询接口
     * 这里统一封装了分页请求和结果，避免直接引入具体框架的分页对象, 如MyBatis或JPA的分页对象
     * 从而避免因为替换ORM框架而导致服务层、控制层的分页接口也需要变动的情况，替换ORM框架也不会
     * 影响服务层以上的分页接口，起到了解耦的作用
     * @param pageRequest 自定义，统一分页查询请求
     * @return PageResult 自定义，统一分页查询结果
     */
    PageResult findPage(PageRequest pageRequest);

    PageResult findDetailPage(PageRequest pageRequest);

    void save(TableList tableList);

    List<TableList> getTableList(String [] tableName);

    TableList selectByPrimaryKey(TableListKey key);


}
