import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;
public class HelloWorld {
	ProcessEngine pes=ProcessEngines.getDefaultProcessEngine();
	
	/*
	 * 部署流程定义
	 * select * from act_re_procdef;
	 * */
	@Test
	public void deploymentProcessDefinition(){
		//插件bug，把helloworld.bpmn关闭 activiti.cfg.xml
		System.out.println(pes);
		RepositoryService rps=pes.getRepositoryService();//与流程定义和部署对象相关的Service
		DeploymentBuilder dlmb=rps.createDeployment();//创建一个部署对象
		dlmb.name("helloWorld入门程序");//添加部署的名称
		dlmb.addClasspathResource("diagrams/helloworld1.bpmn");//从classpath的资源中加载，一次只能加载一个文件
		dlmb.addClasspathResource("diagrams/helloworld1.png");
		Deployment dpl=dlmb.deploy();//完成部署
		System.out.println("部署ID："+dpl.getId());
		System.out.println("部署名称:"+dpl.getName());
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
		//RuntimeService rts=pes.getRuntimeService();//正在执行的流程实例和执行对象相关的Service
		//使用流程定义的key启动流程实例，key对应helloWorld.bpmn 文件中id的属性值,使用key值启动，默认是按照最新版本的流程定义启动
		//ProcessInstance pinst=rts.startProcessInstanceByKey("helloworld");
		
		//System.out.println("流程实例ID："+pinst.getId());//流程实例ID 2501
		//System.out.println("流程定义ID:"+pinst.getProcessDefinitionId());//流程定义id  helloworld:1:4
		
		ProcessInstance pi = pes.getRuntimeService()// 与正在执行的流程实例和执行对象相关的Service  
	            .startProcessInstanceByKey("helloworld");// 使用流程定义的key启动流程实例，key对应HelloWorld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
		System.out.println("流程实例ID："+pi.getId());//流程实例ID 2501
		System.out.println("流程定义ID:"+pi.getProcessDefinitionId());
	}
	/** 
	 * 查询历史流程实例 
	 */  
	@Test  
	public void findHistoryProcessInstance(){  
	    String processInstanceId="7501";  
	    HistoricProcessInstance hpi = pes.getHistoryService()  
	            .createHistoricProcessInstanceQuery()  
	            .processInstanceId(processInstanceId)  
	            .singleResult();  
	    System.out.println(hpi.getId() +"    "+hpi.getProcessDefinitionId()+"   "+ hpi.getStartTime()+"   "+hpi.getDurationInMillis());  
	}
	
	/**
	 * 查询当前人的个人任务
	 */
	@Test
	public void findMyPersonaltask(){
		TaskService ts=pes.getTaskService();//正在执行的任务管理相关的Service
		TaskQuery tq=ts.createTaskQuery();//创建任务查询对象
		TaskQuery tqA=tq.taskAssignee("李四");//张三指定个人任务查询，指定办理人
		List<Task> list=tqA.list();
		System.out.println(list);
		if(list!=null&&list.size()>0){
			for(Task t:list){
				System.out.println("任务id:"+t.getId());
				System.out.println("任务名称:"+t.getName());
				System.out.println("任务的创建时间:"+t.getCreateTime());
				System.out.println("任务办理人:"+t.getAssignee());
				System.out.println("流程实例Id:"+t.getProcessInstanceId());
				System.out.println("执行对象Id:"+t.getExecutionId());
				System.out.println("流程定义Id:"+t.getProcessDefinitionId());
			}
			
		}
		 /*String assignee = "张三"; // TODO  
		    List<Task> list = pes.getTaskService()// 与正在执行的任务管理相关的service  
		            .createTaskQuery()// 创建任务查询对象  
		            // 查询条件  
		            .taskAssignee(assignee)// 指定个人任务查询，指定办理人  
		            // .taskCandidateGroup("")//组任务的办理人查询  
		            // .processDefinitionId("")//使用流程定义ID查询  
		            // .processInstanceId("")//使用流程实例ID查询  
		            // .executionId(executionId)//使用执行对象ID查询  
		            *//** 排序 *//*  
		            .orderByTaskCreateTime().asc()// 使用创建时间的升序排列  
		            // 返回结果集  
		            // .singleResult() //返回唯一的结果集  
		            // .count()//返回结果集的数量  
		            // .listPage(firstResult, maxResults)//分页查询  
		            .list();// 返回列表  
		    if (list != null && list.size() > 0) {  
		        for (Task task : list) {  
		            System.out.println("任务ID：" + task.getId());  
		            System.out.println("任务名称:" + task.getName());  
		            System.out.println("任务的创建时间:" + task.getCreateTime());  
		            System.out.println("任务的办理人:" + task.getAssignee());  
		            System.out.println("流程实例ID:" + task.getProcessInstanceId());  
		            System.out.println("执行对象ID:" + task.getExecutionId());  
		            System.out.println("流程定义ID:" + task.getProcessDefinitionId());  
		            System.out  
		                    .println("##################################################");  
		        }
		    }*/
	}
	
	/**
	 * 完成我的任务
	 *
	 **/
	@Test 
	public void completeMyPersonalTask(){
		//任务id
		String taskId="7504";
		TaskService ts=pes.getTaskService();//执行任务管理相关的service
		ts.complete(taskId);
		System.out.println("完成任务：任务id:"+taskId);
	}
	
	/** 
	 * 查询历史任务 
	 */  
	@Test  
	public void findHistoryTask(){  
	    String processInstanceId="7501";  
	    List<HistoricTaskInstance> list = pes.getHistoryService()//与历史数据（历史表）相关的service  
	            .createHistoricTaskInstanceQuery()//创建历史任务实例查询  
	            .processInstanceId(processInstanceId)  
//	              .taskAssignee(taskAssignee)//指定历史任务的办理人  
	            .list();  
	    if(list!=null && list.size()>0){  
	        for(HistoricTaskInstance hti:list){  
	            System.out.println(hti.getId()+"    "+hti.getName()+"    "+hti.getProcessInstanceId()+"   "+hti.getStartTime()+"   "+hti.getEndTime()+"   "+hti.getDurationInMillis());  
	            System.out.println("################################");  
	        }  
	    }     
	  
	}
	
	/** 
	 * 查询流程状态（判断流程正在执行，还是结束） 
	 */  
	@Test  
	public void isProcessEnd(){  
	    String processInstanceId =  "7501";  
	    ProcessInstance pi = pes.getRuntimeService()//表示正在执行的流程实例和执行对象  
	            .createProcessInstanceQuery()//创建流程实例查询  
	            .processInstanceId(processInstanceId)//使用流程实例ID查询  
	            .singleResult();  
	      
	    if(pi==null){  
	        System.out.println("流程已经结束");  
	    }  
	    else{  
	        System.out.println("流程没有结束");  
	    }  
	      
	} 
}
