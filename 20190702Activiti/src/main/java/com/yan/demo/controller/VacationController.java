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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
public class VacationController {

	@Autowired
	RepositoryService repositoryService;
	
	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	TaskService taskService;

	private List<Task> tasks;
	
	@RequestMapping(value ="/startVacation", method= RequestMethod.GET)
    @ApiOperation(value="启动流程实例", notes="testtest")
    @ApiImplicitParam(paramType="query", name = "", value = "用", required = false, dataType = "")
	public void startVacation() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("requestUser", "yan");
		map.put("numberOfDays", new Integer(4));
		map.put("vacationMotivation", "I'm really tired!");

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("vacationRequest", map);

		// Verify that we started a new process instance
		System.out.println("Number of process instances: " + runtimeService.createProcessInstanceQuery().count()+"----"+processInstance.getBusinessKey());
	}
	
//	@RequestMapping("queryVacation/{name}")
	@RequestMapping(value ="/queryVacation/{name}", method= RequestMethod.GET)
    @ApiOperation(value="根据用户编号获取用户姓名", notes="输入正确的任务处理人")
    @ApiImplicitParam(paramType="query", name = "name", value = "用户处理人", required = true, dataType = "String")
	public String queryVacation(@PathVariable(value="name" ) String name) {
		System.out.println("queryVacation--"+name);
		tasks = taskService.createTaskQuery().taskAssignee(name).list();
		for (Task task : tasks) {
		  System.out.println("Task available: " + task.getName()+"--"+task.getAssignee()+"--"+task.getId());
		}
		
		return "query";
	}
	
//	@RequestMapping("completeVacation/{taskId}")
    @RequestMapping(value ="/completeVacation/{taskId}", method= RequestMethod.GET)
    @ApiOperation(value="根据用户编号获取用户姓名", notes="输入正确的任务id")
    @ApiImplicitParam(paramType="query", name = "taskId", value = "任务id", required = true, dataType = "Integer")
	public String complete(@PathVariable(value="taskId" ) String taskId) {
		System.out.println("completeVacation--"+taskId);
//		Task task = tasks.get(0);

//		Map<String, Object> taskVariables = new HashMap<String, Object>();
//		taskVariables.put("vacationApproved", "false");
//		taskVariables.put("managerMotivation", "We have a tight deadline!");
//		taskService.complete(task.getId(), taskVariables);
		taskService.complete(taskId);
		return "complete--"+taskId;
	}
	
	
	
}
