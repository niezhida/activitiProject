--create database activiti;

--RepositoryService �������̶���
--RuntimeService ִ�й��������������ƽ���ɾ������ʵ����
--TaskService �������
--HistoryService ��ʷ����  ִ��������ݵĹ���
--IdentityService ��֯��������
--FormService һ����ѡ�������������
--ManagerService 

--show tables;
--show databases;
--use [sys;

--use activiti;
show tables;
--use activiti;
select * from act_re_procdef;
select * from act_ru_execution;
select * from act_ru_task;
select * from act_hi_taskinst;
drop database activiti;
create database activiti;

#--�����������̶�����ر�
#------------------------------------------------------------------------------
select * from act_re_deployment #--��������

select * from act_re_procdef #--���̶����

select * from act_ge_bytearray #--��Դ�ļ���

select * from act_ge_property  #--�������ɲ��Ա�

#--��������������ʵ��ʵ����ִ�ж�������
#------------------------------------------------------------------------------
#--����ǵ�������(û�з�֧�;ۺ�)����ô����ʵ��ID��ִ�ж���Id����ͬ��
#--����ִ�е�ִ�ж���� ID_��ִ�ж���id����PROC_INST_ID_(������ʵ��id)
#--PROC_DEF_ID_�����̶���id����ACT_ID_����ǰ�id��
select * from act_ru_execution 

#--����ʵ������ʷ��  ID_����  ��PROC_INST_ID_����
#--һ������������ʵ��ֻ��һ����ִ�ж�����Դ��ڶ��(������ڷ�֧�;ۺ�)
select * from act_hi_procinst

#--����ִ�е������,ID_(����ID),EXECUTION_ID_(ִ��id),PROC_INST_ID_(����ʵ��id)
#--ֻ�нڵ���UserTask��ͼԪ��ʱ�򣬸ñ��д�������
select * from act_ru_task 

#--������ʷ��(ֻ�нڵ���UserTask��ͼԪ��ʱ�򣬸ñ��д�������)
select * from act_hi_taskinst 

#--���л�ڵ����ʷ��(��������)
select * from act_hi_actinst

#--���̱�����ر�
#------------------------------------------------------------------------------
select * from act_ru_variable #--����ִ�е����̱�����  BYTEARRAY_ID_  ��Դ�ļ���id 45001

select * from act_hi_variable #--��ʷ���̱����� ���ɰ汾(5.13)
select * from ACT_HI_VARINST #--��ʷ���̱����� �°汾����ǰʹ�ð汾��5.19.0��



