package org.activiti;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;

public class DynamicTaskDelegate implements JavaDelegate {
	
	MyCustomActivitiBehaviorFactory myFactory = new MyCustomActivitiBehaviorFactory();
	
	final String DYNATASK_NUMBER_STRING = "currentTaskNumber";
	final String DYNATASK_PREFIX = "dynTsk";
	final String DYNATASK_CreateOne = "createOne";
	final String DYNATASK_CreateTwo = "createTwo";
	final String DYNATASK_FINISH = "finish";
	final String DYNATASK_COMMAND_VAR = "command";
	

	
	
	private Integer getAndIncreaseTaskNumber(DelegateExecution execution) {
		Integer current_num = (Integer) execution.getVariable(DYNATASK_NUMBER_STRING);
		if (current_num == null)
			current_num = 1;
		execution.setVariable(DYNATASK_NUMBER_STRING, current_num + 1);
		return current_num;
	}
	

	private Task createNewTask(String activiyName, DelegateExecution execution, String taskId) {
		
		ExecutionEntity executionEntity = (ExecutionEntity) execution;
		Execution newExecution = executionEntity.createExecution();
		ExecutionEntity newExectionEntity = (ExecutionEntity) newExecution;
		
		newExectionEntity.setVariable("isDynamic", "salam");
	    
		ActivityImpl newActivty = new ActivityImpl(activiyName, null);
		
		
		
		newActivty.setActivityBehavior(myFactory.createDynamicTaskActivitiBehavior(null));
		newExectionEntity.setActivity(newActivty);

		
		TaskService taskService = execution.getEngineServices().getTaskService();

		
		Task newTask = taskService.newTask(DYNATASK_PREFIX+ taskId);
		
		TaskEntity newTaskEntity = (TaskEntity) newTask;
		newTaskEntity.setExecution((DelegateExecution) newExecution);
		taskService.saveTask(newTask);
		Context
	      .getCommandContext()
	      .getDbSqlSession()
	      .update(newExectionEntity);
		
		return newTask;
		
	}

	private Task createOneTask(DelegateExecution execution) {
		return createNewTask("oneStepTask", null, execution);
		
	}
	
	private Task createTwoTask(DelegateExecution execution) {
		return createNewTask("twoStepTaskHead", null, execution);
		
	}

	
	private Task createNewTask(String activitiName, Task prevTask, DelegateExecution execution) {
 
		Integer taskNum = getAndIncreaseTaskNumber(execution);
		String taskNumStr = String.format("%03d", taskNum);
		
		String prevId = "";
		if (prevTask != null) {
			prevId = prevTask.getId();
			if (!prevId.startsWith(DYNATASK_PREFIX))
				return null;
			prevId = prevId.substring(DYNATASK_PREFIX.length(), DYNATASK_PREFIX.length()+3);
			
		}
		return createNewTask(activitiName, execution,  taskNumStr+prevId);
		
	}
	
	

	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		String command = (String) execution.getVariable(DYNATASK_COMMAND_VAR);
		
		if (command == null)
			command = DYNATASK_CreateTwo;
		
		
		if (command.equals(DYNATASK_CreateOne))
			createOneTask(execution);
		else if (command.equals(DYNATASK_CreateTwo))
			createTwoTask(execution);
			else if (!command.equals(DYNATASK_FINISH))
				System.out.println("unkown command :" + command);
				
		
		
	}

}
