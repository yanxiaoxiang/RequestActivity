package com.yan.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
public class ActivitiController {

	@Autowired
	RepositoryService repositoryService;
	
	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	TaskService taskService;

	private List<Task> tasks;
	
	
	@RequestMapping("start")
	public void start() {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("employeeName", "Kermit");
		variables.put("numberOfDays", new Integer(4));
		variables.put("vacationMotivation", "I'm really tired!");

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("vacationRequest", variables);

		// Verify that we started a new process instance
		System.out.println("Number of process instances: " + runtimeService.createProcessInstanceQuery().count()+"----"+processInstance.getBusinessKey());
	}
	
	@RequestMapping("query")
	public String query() {
		
		tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
		for (Task task : tasks) {
		  System.out.println("Task available: " + task.getName());
		}
		
		return "query";
	}
	
	@RequestMapping("complete")
	public String complete() {
		
		Task task = tasks.get(0);

		Map<String, Object> taskVariables = new HashMap<String, Object>();
		taskVariables.put("vacationApproved", "false");
		taskVariables.put("managerMotivation", "We have a tight deadline!");
		taskService.complete(task.getId(), taskVariables);
		return "complete";
	}
	
	public String suspendOrActive() {
		repositoryService.suspendProcessDefinitionByKey("vacationRequest");
		try {
		  runtimeService.startProcessInstanceByKey("vacationRequest");
		} catch (ActivitiException e) {
		  e.printStackTrace();
		}
		return "suspendOrActive";
		
		/**
		 * To reactivate a process definition, simply call one of the repositoryService.activateProcessDefinitionXXX methods.
		 * It’s also possible to suspend a process instance. When suspended, the process cannot be 
		 * continued (e.g. completing a task throws an exception) and no jobs (such as timers) will executed. 
		 * Suspending a process instance can be done by calling the runtimeService.suspendProcessInstance method. 
		 * Activating the process instance again is done by calling the runtimeService.activateProcessInstanceXXX methods.
		 */
	}
	
    @RequestMapping(value ="/getUserName", method= RequestMethod.GET)
    @ApiOperation(value="根据用户编号获取用户姓名", notes="test: 仅1和2有正确返回")
    @ApiImplicitParam(paramType="query", name = "userNumber", value = "用户编号", required = true, dataType = "Integer")
    public String getUserName(@RequestParam Integer userNumber){
        if(userNumber == 1){
            return "张三丰";
        }
        else if(userNumber == 2){
            return "慕容复";
        }
        else{
            return "未知";
        }
    }
	
}
