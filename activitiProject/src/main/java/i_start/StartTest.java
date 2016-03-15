package i_start;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

public class StartTest {
	ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	 
	/**部署流程定义inputStream*/
	@Test
	public void deploymentProcessDefinition_inputStream(){
		InputStream inBpmn =this.getClass().getResourceAsStream("start.bpmn");
		InputStream inPng =this.getClass().getResourceAsStream("start.png");
		Deployment dp=processEngine.getRepositoryService()
				.createDeployment()
				.name("开始活动")
				.addInputStream("start.bpmn", inBpmn)
				.addInputStream("start.png", inPng)
				.deploy();
		System.out.println("部署Id："+dp.getId());
		System.out.println("部署名称："+dp.getName());
		//部署Id：90001 部署名称：排他网关
	}
	
	/*
	 * 启动流程实例
	 * 1)在数据库的act_ru_execution正在执行的执行对象表中插入一条记录
    2)在数据库的act_hi_procinst程实例的历史表中插入一条记录
    3)在数据库的act_hi_actinst活动节点的历史表中插入一条记录
    4)我们图中节点都是任务节点，所以同时也会在act_ru_task流程实例的历史表添加一条记录
    5)在数据库的act_hi_taskinst任务历史表中也插入一条记录。
	 * */
	@Test 
	public void statProcessInstance(){
		ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey("startProcess")// 与正在执行的流程实例和执行对象相关的Service  
	            //.startProcessInstanceByKey("myProcess")
	            ;// 使用流程定义的key启动流程实例，key对应HelloWorld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
		System.out.println("流程实例ID："+pi.getId());//流程实例ID 92501 流程实例ID：exclusiveGateWay:1:90004
		System.out.println("流程定义ID:"+pi.getProcessDefinitionId());
		/***判断流程是否结束，查询正在执行的执行对象表*/
		ProcessInstance rpi=processEngine.getRuntimeService()
			.createProcessInstanceQuery()
			.processInstanceId(pi.getId())
			.singleResult();
		//说明流程实例结束了
		if(rpi==null){
			//查询历史，获取流程相关信息
			HistoricProcessInstance hpi=processEngine.getHistoryService()
				.createHistoricProcessInstanceQuery()
				.processInstanceId(pi.getId())
				.singleResult();
			System.out.println(hpi.getId());
		}
	}
	
}
