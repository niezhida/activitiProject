package cn.itcast.b_processDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class ProcessDefinitionTest {
	
	ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	
	/**部署流程定义(从classpath)*/
	@Test
	public void deploymentProcessDefinition_classpath(){
		Deployment deployment=processEngine.getRepositoryService()//与流程定义和朴树对象相关的Service
				.createDeployment()//创建一个部署对象
				.name("流程定义")//添加部署名称
				.addClasspathResource("diagrams/helloworld.bpmn")//从classpath的资源中加载，一次只能加载一个文件
				.addClasspathResource("diagrams/helloworld.png")//从classpath的资源中加载，一次只能加载一个文件
				.deploy();
		System.out.println("部署ID："+deployment.getId());
		System.out.println("部署名称："+deployment.getName());
	}
	
	/**部署流程定义zip*/
	@Test
	public void deploymentProcessDefinition_zip(){
		InputStream in =this.getClass().getClassLoader().getResourceAsStream("diagrams/hellowordWF.zip");
		ZipInputStream zis=new ZipInputStream(in);
		Deployment dp=processEngine.getRepositoryService()
				.createDeployment()
				.name("")
				.addZipInputStream(zis)
				.deploy();
		System.out.println("部署Id："+dp.getId());
		System.out.println("部署名称："+dp.getName());
	}
	/**查询流程定义*/
	@Test
	public void findProcessDefinition(){
		List<ProcessDefinition> list=processEngine.getRepositoryService()
				.createProcessDefinitionQuery()//流程定义查询
				//.deploymentId("")//使用部署对象id查询
				//.processDefinitionId("")//使用流程定义id查询条件
				//.processDefinitionName("")//使用流程定义名称查询
				//.processDefinitionKey("")//key
				//.processDefinitionNameLike("")//名称模糊查询
				
				//.orderByDeploymentId().asc()//升序
				.orderByProcessDefinitionVersion().asc()//排序
				
				.list()//返回列表，封装流程定义
				//.singleResult()//返回唯一结果集
				//.count()//返回记录数
				//.listPage(1, 4)//分页
				;
		if(list!=null&&list.size()>0){
			for(ProcessDefinition p:list){
				System.out.println("流程定义id："+p.getId());//流程定义key+版本+随机生成数
				System.out.println("流程定义名称："+p.getName());//。bpmn文件的name属性值
				System.out.println("流程定义key："+p.getKey());//对应.bpmn文件的di属性值
				System.out.println("流程定义版本："+p.getVersion());
				System.out.println("资源名称bpmn文件："+p.getResourceName());
				System.out.println("资源名称png文件："+p.getDiagramResourceName());
				System.out.println("部署对象id："+p.getDeploymentId());
			}
		}
	}
	private void SysPrintlnProcessD(List<ProcessDefinition> list){
		if(list!=null&&list.size()>0){
			for(ProcessDefinition p:list){
				System.out.println("流程定义id："+p.getId());//流程定义key+版本+随机生成数
				System.out.println("流程定义名称："+p.getName());//。bpmn文件的name属性值
				System.out.println("流程定义key："+p.getKey());//对应.bpmn文件的di属性值
				System.out.println("流程定义版本："+p.getVersion());
				System.out.println("资源名称bpmn文件："+p.getResourceName());
				System.out.println("资源名称png文件："+p.getDiagramResourceName());
				System.out.println("部署对象id："+p.getDeploymentId());
			}
		}
	}
	/**删除流程定义*/
	@Test
	public void deleteProcessDefinition(){
		//使用部署id删除
		//不带级联删除，只能删除没有启动的流程,如果删除流程启动的就会抛出异常，外键约束
		//processEngine.getRepositoryService().deleteDeployment("1");
		
		//级联删除 不管流程师傅启动都可以删除
		processEngine.getRepositoryService().deleteDeployment("1", true);;
	}
	/**查看流程图png&bpmn
	 * @throws IOException */
	@Test
	public void viwPic() throws IOException{
		//将生产的图片放在文件夹下
		//部署id
		String deploymentId="5001"; 
		//获取图片资源名称
		List<String> l=processEngine.getRepositoryService()
				.getDeploymentResourceNames(deploymentId);
		String resourceName="";
		if(l!=null&&l.size()>0){
			for(String name:l){
				if(name.indexOf(".png")>=0){
					resourceName=name;
				}
			}
		}
		System.out.println(resourceName);
		//获取图片输入了
		InputStream in=processEngine.getRepositoryService()
			.getResourceAsStream(deploymentId, resourceName);
		//将图片生成到
		File f=new File("D:/"+resourceName);
		//将输入流图片写到d盘上commons-io-2.0.1.jar
		FileUtils.copyInputStreamToFile(in,f);
	}
	
	/**查询最新版本的流程定义*/
	@Test
	public void findLastVersionProcessDefinition(){
		List<ProcessDefinition> l=processEngine.getRepositoryService()
			.createProcessDefinitionQuery()
			.orderByProcessDefinitionVersion().asc()
			.list();
		Map<String ,ProcessDefinition> m=new LinkedHashMap<String,ProcessDefinition>();
		if(l!=null&&l.size()>0){
			for(ProcessDefinition p:l){
				m.put(p.getKey(),p);
			}
		}
		List<ProcessDefinition> pl=new ArrayList<ProcessDefinition>(m.values());
		SysPrintlnProcessD(pl);
	}
	
	/**删除流程定义，(删除key相同的所有不同版本的流程定义)*/
	@Test
	public void deleteProcessDefinitionByKey(){
		List<ProcessDefinition> l=processEngine.getRepositoryService()
			.createProcessDefinitionQuery()
			.processDefinitionKey("helloworld")
			.list();
		for(ProcessDefinition p:l){
			String deId=p.getDeploymentId();
			processEngine.getRepositoryService()
				.deleteDeployment(deId,true);
		}
	}
	
}
