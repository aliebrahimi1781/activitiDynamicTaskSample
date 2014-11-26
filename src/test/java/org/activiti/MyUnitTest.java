package org.activiti;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class MyUnitTest {
	


	private void showInfo(ProcessEngine processEngine) {
		TaskService taskService = processEngine.getTaskService();
		System.out.println();
		for (Task task: taskService.createTaskQuery().list())
			System.out.println("task id=" + task.getId());
		List<Execution> executions = processEngine.getRuntimeService().createExecutionQuery().list();
		for (Execution execution: executions)
			System.out.println("execution id =" + execution.getId() + " activity=" + execution.getActivityId());
		
	}
	
	void completeOneTask(ProcessEngine processEngine, String taskid) {
		TaskService taskService = processEngine.getTaskService();

		System.out.println("**********************************");
		System.out.println("comleting task " + taskid);
		taskService.complete(	taskid);

		System.out.println("tasks after completing ");
		showInfo(processEngine);

	}
	@Test
	public void test() {
		
		
		ProcessEngine processEngine = ((ProcessEngineConfigurationImpl)ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration())
				  .setActivityBehaviorFactory(new MyCustomActivitiBehaviorFactory())
				  .buildProcessEngine();
		
		RepositoryService repositoryService = processEngine.getRepositoryService();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		TaskService taskService = processEngine.getTaskService();

		repositoryService
				  .createDeployment()
				  .addClasspathResource("org/activiti/test/my-process.bpmn20.xml")
				  .deploy()
				  .getId();
		
		
		System.out.println("at start");
		showInfo(processEngine);
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
		assertNotNull(processInstance);
		
		
		System.out.println("just after starting");
		showInfo(processEngine);
		
		completeOneTask(processEngine, "dynTsk001");
		completeOneTask(processEngine, "13");

		
		/*
		Task oldTask = taskService.createTaskQuery().taskId("11").singleResult();
		assertNotNull(oldTask);
		
		System.out.println("no completing the new task");
		taskService.complete(newTask.getId());
		*/
//		assertEquals("Activiti is awesome!", task.getName());
	}

}
