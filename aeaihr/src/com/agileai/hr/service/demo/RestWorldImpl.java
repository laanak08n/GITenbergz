package com.agileai.hr.service.demo;

import java.util.ArrayList;
import java.util.List;

import com.agileai.hotweb.ws.BaseRestService;
import com.agileai.hr.service.model.DataModel;
import com.agileai.hr.service.model.ResultStatus;


public class RestWorldImpl extends BaseRestService implements RestWorld {

	@Override
	public DataModel retrieveDataModel(String type, String id) {
		DataModel result = new DataModel();
		result.setId(id);
		result.setType(type);
		result.setCode("test"+id);
		result.setName("测试数据");
		return result;
	}

	@Override
	public List<DataModel> retrieveDataModels(String type) {
		List<DataModel> result = new ArrayList<DataModel>();
		for (int i=0;i < 5;i++){
			DataModel model = new DataModel();
			model.setId(String.valueOf(i));
			model.setCode("test"+i);
			model.setName("测试数据"+i);
			model.setType(type);
			
			result.add(model);
			
		}
		return result;
	}

	@Override
	public ResultStatus storeDataModels(List<DataModel> models) {
		ResultStatus resultStatus = new ResultStatus();
		for (int i=0;i < models.size();i++){
			DataModel model = models.get(i);
			System.out.println("model " + i + " name is " + model.getName());
		}
		resultStatus.setSuccess(true);
		return resultStatus;
	}

	@Override
	public ResultStatus modifyDataModel(DataModel model) {
		ResultStatus resultStatus = new ResultStatus();
		System.out.println("model name is " + model.getName());
		return resultStatus;
	}
}
