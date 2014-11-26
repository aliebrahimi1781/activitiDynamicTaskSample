
package org.activiti;

import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;
import org.activiti.engine.impl.task.TaskDefinition;

public class MyCustomActivitiBehaviorFactory extends DefaultActivityBehaviorFactory
{
	
      @Override	
	  public UserTaskActivityBehavior createUserTaskActivityBehavior(UserTask userTask, TaskDefinition taskDefinition) {
		    return new MyUserTaskActiviyBehavior(taskDefinition);
		  }

	  public DynamicTaskActivityBehavior createDynamicTaskActivitiBehavior(TaskDefinition taskDefinition) {
	    return new DynamicTaskActivityBehavior(taskDefinition);
	  }
	  
	  


}
