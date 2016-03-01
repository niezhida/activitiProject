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
	
	/**�������̶���(��classpath)*/
	@Test
	public void deploymentProcessDefinition_classpath(){
		Deployment deployment=processEngine.getRepositoryService()//�����̶��������������ص�Service
				.createDeployment()//����һ���������
				.name("���̶���")//��Ӳ�������
				.addClasspathResource("diagrams/helloworld.bpmn")//��classpath����Դ�м��أ�һ��ֻ�ܼ���һ���ļ�
				.addClasspathResource("diagrams/helloworld.png")//��classpath����Դ�м��أ�һ��ֻ�ܼ���һ���ļ�
				.deploy();
		System.out.println("����ID��"+deployment.getId());
		System.out.println("�������ƣ�"+deployment.getName());
	}
	
	/**�������̶���zip*/
	@Test
	public void deploymentProcessDefinition_zip(){
		InputStream in =this.getClass().getClassLoader().getResourceAsStream("diagrams/hellowordWF.zip");
		ZipInputStream zis=new ZipInputStream(in);
		Deployment dp=processEngine.getRepositoryService()
				.createDeployment()
				.name("")
				.addZipInputStream(zis)
				.deploy();
		System.out.println("����Id��"+dp.getId());
		System.out.println("�������ƣ�"+dp.getName());
	}
	/**��ѯ���̶���*/
	@Test
	public void findProcessDefinition(){
		List<ProcessDefinition> list=processEngine.getRepositoryService()
				.createProcessDefinitionQuery()//���̶����ѯ
				//.deploymentId("")//ʹ�ò������id��ѯ
				//.processDefinitionId("")//ʹ�����̶���id��ѯ����
				//.processDefinitionName("")//ʹ�����̶������Ʋ�ѯ
				//.processDefinitionKey("")//key
				//.processDefinitionNameLike("")//����ģ����ѯ
				
				//.orderByDeploymentId().asc()//����
				.orderByProcessDefinitionVersion().asc()//����
				
				.list()//�����б���װ���̶���
				//.singleResult()//����Ψһ�����
				//.count()//���ؼ�¼��
				//.listPage(1, 4)//��ҳ
				;
		if(list!=null&&list.size()>0){
			for(ProcessDefinition p:list){
				System.out.println("���̶���id��"+p.getId());//���̶���key+�汾+���������
				System.out.println("���̶������ƣ�"+p.getName());//��bpmn�ļ���name����ֵ
				System.out.println("���̶���key��"+p.getKey());//��Ӧ.bpmn�ļ���di����ֵ
				System.out.println("���̶���汾��"+p.getVersion());
				System.out.println("��Դ����bpmn�ļ���"+p.getResourceName());
				System.out.println("��Դ����png�ļ���"+p.getDiagramResourceName());
				System.out.println("�������id��"+p.getDeploymentId());
			}
		}
	}
	private void SysPrintlnProcessD(List<ProcessDefinition> list){
		if(list!=null&&list.size()>0){
			for(ProcessDefinition p:list){
				System.out.println("���̶���id��"+p.getId());//���̶���key+�汾+���������
				System.out.println("���̶������ƣ�"+p.getName());//��bpmn�ļ���name����ֵ
				System.out.println("���̶���key��"+p.getKey());//��Ӧ.bpmn�ļ���di����ֵ
				System.out.println("���̶���汾��"+p.getVersion());
				System.out.println("��Դ����bpmn�ļ���"+p.getResourceName());
				System.out.println("��Դ����png�ļ���"+p.getDiagramResourceName());
				System.out.println("�������id��"+p.getDeploymentId());
			}
		}
	}
	/**ɾ�����̶���*/
	@Test
	public void deleteProcessDefinition(){
		//ʹ�ò���idɾ��
		//��������ɾ����ֻ��ɾ��û������������,���ɾ�����������ľͻ��׳��쳣�����Լ��
		//processEngine.getRepositoryService().deleteDeployment("1");
		
		//����ɾ�� ��������ʦ������������ɾ��
		processEngine.getRepositoryService().deleteDeployment("1", true);;
	}
	/**�鿴����ͼpng&bpmn
	 * @throws IOException */
	@Test
	public void viwPic() throws IOException{
		//��������ͼƬ�����ļ�����
		//����id
		String deploymentId="5001"; 
		//��ȡͼƬ��Դ����
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
		//��ȡͼƬ������
		InputStream in=processEngine.getRepositoryService()
			.getResourceAsStream(deploymentId, resourceName);
		//��ͼƬ���ɵ�
		File f=new File("D:/"+resourceName);
		//��������ͼƬд��d����commons-io-2.0.1.jar
		FileUtils.copyInputStreamToFile(in,f);
	}
	
	/**��ѯ���°汾�����̶���*/
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
	
	/**ɾ�����̶��壬(ɾ��key��ͬ�����в�ͬ�汾�����̶���)*/
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
