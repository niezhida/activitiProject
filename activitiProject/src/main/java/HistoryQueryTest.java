import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.junit.Test;

public class HistoryQueryTest {
	
	ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	
	/**��ѯ��������ʵ��*/
	@Test
	public void findHistoryProcessInstance(){
		String processInstanceId="22501";
		HistoricProcessInstance hpi=processEngine.getHistoryService()
				.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId)
				.orderByProcessInstanceStartTime().asc()
				.singleResult();
		System.out.println(hpi.getId()+","+hpi.getProcessDefinitionId()+
				","+hpi.getStartActivityId()+","+hpi.getStartTime()+","+hpi.getEndTime()+","+hpi.getDurationInMillis());
	}
	
	/**��ѯ��ʷ�act*/
	@Test
	public void findHistoryActiviti(){
		String processInstanceId="32501";
		List<HistoricActivityInstance> l=
				processEngine.getHistoryService()
				.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId)
				.orderByHistoricActivityInstanceStartTime().asc()
				.list();
		if(l!=null&l.size()>0){
			for(HistoricActivityInstance h:l){
				System.out.println(h.getTaskId());
				System.out.println(h.getActivityName());
				System.out.println(h.getActivityType());
			}
		}
	}
	
	/**��ѯ��ʷ����task*/
	@Test
	public void findHistoryTask(){
		String processInstanceId="32501";
		List<HistoricTaskInstance> list=
				processEngine.getHistoryService()
				.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstanceId)
				.orderByHistoricTaskInstanceStartTime().asc()
				.list();
		if(list!=null&&list.size()>0){
			for(HistoricTaskInstance h:list){
				System.out.println(h.getId());
				System.out.println(h.getName());
				System.out.println(h.getProcessInstanceId());
				System.out.println(h.getStartTime());
				System.out.println(h.getEndTime());
				System.out.println(h.getWorkTimeInMillis());
			}
		}
	}
	/**��ѯ��ʷ���̱���*/
	@Test
	public void findHistoryProcessVariables(){
		String processInstanceId="32501";
		List<HistoricVariableInstance> l=processEngine.getHistoryService()
				.createHistoricVariableInstanceQuery()
				.processInstanceId(processInstanceId)
				.list();
		if(l!=null&&l.size()>0){
			for(HistoricVariableInstance h:l){
				System.out.println(h.getId());
				System.out.println(h.getProcessInstanceId());
				System.out.println(h.getVariableName());
				System.out.println(h.getVariableTypeName());
			}
		}
	}
}
