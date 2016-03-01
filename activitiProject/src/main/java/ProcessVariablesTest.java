
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class ProcessVariablesTest {
	
	@Test
	public void test(/*String[] args*/) {//initializationError runner:junit4
		//从classpath根目录下加载指定名称的文件
		this.getClass().getClassLoader()
			.getResourceAsStream("testVariables.bpmn");
		
		//从当前包下加载指定名称的文件
		this.getClass().getResourceAsStream("testVariables.bpmn");
		
		//从calsspath根目录下加载指定名称的文件
		this.getClass().getResourceAsStream("/testVariables.bpmn");
		
	}
	
	ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	
	/**部署流程定义(InputStream)*/
	@Test
	public void deploymentProcessDefinition_inputStream(){
		InputStream inputStreambpmn=this.getClass().getResourceAsStream("/diagrams/processVariables.bpmn");
		
		InputStream inputStreampng=this.getClass().getResourceAsStream("/diagrams/processVariables.png");
		Deployment deployment=processEngine.getRepositoryService()
				.createDeployment()
				.name("流程定义1")
				.addInputStream("processVariables.bpmn", inputStreambpmn)//要求与资源文件的名称要一致
				.addInputStream("processVariables.png", inputStreampng)//要求与资源文件的名称要一致
				.deploy();
		System.out.println("Id "+deployment.getId());
		System.out.println("名称 "+deployment.getName());
		/*Id 30001
		名称 流程定义1*/
	}
	
	/**启动流程实例*/
	@Test
	public void startProcessInstance(){
		//流程定义key
		String processDefinitionKey="processVariables";
		ProcessInstance pi=processEngine.getRuntimeService()
				.startProcessInstanceByKey(processDefinitionKey);
		System.out.println("实例Id "+pi.getId());
		System.out.println("定义Id "+pi.getProcessDefinitionId());
		/*实例Id 32501
		定义Id processVariables:1:30004*/
	}
	
	/**设置流程变量*/
	@Test
	public void setVariables(){
		TaskService ts=processEngine.getTaskService();
		//ts.setVariableLocal("", "请假天数", 3);//Local 局部变量与任务id绑定
		//String taskId="32504";
		String taskId="40002";
		ts.setVariable(taskId, "请假天数-", 4);//
		ts.setVariable(taskId, "请假原因-", "回家探亲");
		//ts.setVariableLocal(taskId, "请假日期", new Date());
		//javabean类型
		//当一个javabean实现序列化，放置在流程变量中，要求javabean的属性不能再发生变化，如果放生变化，获取流程变量对象的时候抛出异常
		//解决方案：在Persion对象中添加 版本serialVersionUID
		
		Person p=new Person();
		p.setId(1);
		p.setName("persion1111哈哈");
		//ts.setVariable(taskId,"java对象",p);
		//ts.setVariable(taskId,"java对象版本1",p);
		//ts.setVariable(taskId,"java对象版本2",p);
		
		p.setTest("fff");
		ts.setVariable(taskId,"java对象版本3",p);
		System.out.println("设置成功");
	}
	
	/**获取流程变量*/
	@Test
	public void getVariables(){
		TaskService ts=processEngine.getTaskService();
		//String tId="32504";
		String tId="40002";
		Integer l=(Integer) ts.getVariable(tId, "请假天数");
		Date d=(Date) ts.getVariable(tId, "请假日期");
		String str=(String) ts.getVariable(tId, "请假原因");
		System.out.println(l);
		System.out.println(d);
		System.out.println(str);
		
		//Person p=(Person) ts.getVariable(tId, "java对象");
		//Person p=(Person) ts.getVariable(tId, "java对象版本1");
		//Person p=(Person) ts.getVariable(tId, "java对象版本2");
		Person p=(Person) ts.getVariable(tId, "java对象版本3");
		System.out.println(p.toString());
	}
	
	/**模拟设置和获取流程变量的场景*/
	@Test
	public void setAndGetVariables(){
		/**与流程实例，执行对象相关*/
		RuntimeService rs=processEngine.getRuntimeService();
		//与任务相关 （正在执行）
		TaskService taskService=processEngine.getTaskService();
		
		//设置流程变量
		//表示使用执行对象id，合流程变量的名称，设置流程变量的值，一次只能设置一个值
		//rs.setVariable(executionId, variableName, value);;
		//使用执行对象id，Map集合，map中的key值就是流程变量的名称,设置多个变量
		//rs.setVariables(executionId, variables);
		
		//
		//taskService.setVariable(taskId, variableName, value);
		//taskService.setVariables(taskId, variables);
		
		//启动流程实例的同事，设置流程变量 Map
		//rs.startProcessInstanceByKey(processDefinitionKey, variables)
		
		//完成任务的同事设置流程变量
		//taskService.complete(taskId, variables);
		
		//rs.getVariable(executionId, variableName);//执行对象id+流程变量名称，获取流程变量的值
		//rs.getVariables(executionId)//获取所有流程变量，返回map
		//rs.getVariables(executionId, variableNames)//执行对象id，获取流程变量集合，通过设置流程变量的名称集合存放到map中
		
		//taskService.getVariable(taskId, variableName);//任务id，流程变量名称
		//taskService.getVariables(taskId);//获取所有
		//taskService.getVariables("",variableNames);//任务id 获取流程变量集合，通过设置流程变量的名称集合存放到map中
	}
	
	/**完成我的任务*/
	@Test
	public void completeMyPersonalTask(){
		String taskid="32504";
		processEngine.getTaskService().complete(taskid);
		System.out.println("完成  任务id："+taskid);
	}
	
	/**查询流程变量的历史表*/
	@Test
	public void findHistoryProcessVariables(){
		List<HistoricVariableInstance> list=processEngine.getHistoryService()
				.createHistoricVariableInstanceQuery()
				.variableName("请假天数")
				.list();
		if(list!=null&&list.size()>0){
			for(HistoricVariableInstance h:list){
				System.out.println(h.getId()+","+h.getProcessInstanceId()+","+h.getVariableName());
			}
		}
	}
}
