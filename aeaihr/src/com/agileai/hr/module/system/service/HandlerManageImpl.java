package com.agileai.hr.module.system.service;

import java.util.ArrayList;
import java.util.List;

import com.agileai.hotweb.bizmoduler.core.MasterSubServiceImpl;

public class HandlerManageImpl
        extends MasterSubServiceImpl
        implements HandlerManage {
    public HandlerManageImpl() {
        super();
    }

    public String[] getTableIds() {
        List<String> temp = new ArrayList<String>();
        temp.add("SysOperation");

        return temp.toArray(new String[] {  });
    }
}
