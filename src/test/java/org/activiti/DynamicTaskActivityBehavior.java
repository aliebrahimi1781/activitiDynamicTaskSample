package org.activiti;

import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.task.TaskDefinition;

public class DynamicTaskActivityBehavior extends UserTaskActivityBehavior{

	public DynamicTaskActivityBehavior(TaskDefinition taskDefinition) {
		super(taskDefinition);
		
	}
	
	  @Override
	  public void signal(ActivityExecution execution, String signalName, Object signalData) throws Exception {
		  System.out.println("Here comes custom signal behavior");
	  }


}
