package org.activiti;

import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.runtime.AtomicOperation;
import org.activiti.engine.impl.task.TaskDefinition;

public class MyUserTaskActiviyBehavior extends UserTaskActivityBehavior {

	public MyUserTaskActiviyBehavior(TaskDefinition taskDefinition) {
		super(taskDefinition);
		// TODO Auto-generated constructor stub
	}
	
	  public void signal(ActivityExecution execution, String signalName, Object signalData) throws Exception {
		  
		  RuntimeService runtimeService = execution.getEngineServices().getRuntimeService();
 		    System.out.println("Ha ha my signal at last. Task name =" + execution.getActivity().getProperty("testProperty") + " isDynamic=" + execution.getVariable("isDynamic"));
		   System.out.println("execution id =" + execution.getId());
		   
		   List<PvmTransition> outgoingTransitions = execution.getActivity().getOutgoingTransitions();
		   if (outgoingTransitions.size() == 0) {
			   ActivityExecution parentExecution = execution.getParent();
			   System.out.println("this is an end task. Do Nothing");
			   ((ExecutionEntity) execution).setScope(false);
			   ((ExecutionEntity) execution).performOperation(AtomicOperation.ACTIVITY_END);
			   runtimeService.signal(parentExecution.getId());   
			   }
		   else
		    if (!((ExecutionEntity)execution).getTasks().isEmpty())
		      throw new ActivitiException("UserTask should not be signalled before complete");
		    leave(execution);
		  }


}
