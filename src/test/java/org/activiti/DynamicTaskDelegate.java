package org.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

public class DynamicTaskDelegate implements JavaDelegate {
	


	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		MyUnitTest.dynaTaskService.createOneTask((ExecutionEntity) execution);
	}

}
