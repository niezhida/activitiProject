--create database activiti;

--RepositoryService 管理流程定义
--RuntimeService 执行管理，包括启动，推进，删除流程实例等
--TaskService 任务管理
--HistoryService 历史管理  执行完的数据的管理
--IdentityService 组织机构管理
--FormService 一个可选服务，任务表单管理
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

#--部署对象合流程定义相关表
#------------------------------------------------------------------------------
select * from act_re_deployment #--部署对象表

select * from act_re_procdef #--流程定义表

select * from act_ge_bytearray #--资源文件表

select * from act_ge_property  #--主键生成策略表

#--流程启动，启动实例实例（执行对象，任务）
#------------------------------------------------------------------------------
#--如果是单例流程(没有分支和聚合)，那么流程实例ID合执行对象Id是相同的
#--正在执行的执行对象表 ID_（执行对象id），PROC_INST_ID_(流程是实例id)
#--PROC_DEF_ID_（流程定义id），ACT_ID_（当前活动id）
select * from act_ru_execution 

#--流程实例的历史表  ID_（）  ，PROC_INST_ID_（）
#--一个流程中流程实例只有一个，执行对象可以存在多个(如果存在分支和聚合)
select * from act_hi_procinst

#--正在执行的任务表,ID_(任务ID),EXECUTION_ID_(执行id),PROC_INST_ID_(流程实例id)
#--只有节点是UserTask的图元的时候，该表中存在数据
select * from act_ru_task 

#--任务历史表(只有节点是UserTask的图元的时候，该表中存在数据)
select * from act_hi_taskinst 

#--所有活动节点的历史表(包括任务)
select * from act_hi_actinst

#--流程变量相关表
#------------------------------------------------------------------------------
select * from act_ru_variable #--正在执行的流程变量表  BYTEARRAY_ID_  资源文件表id 45001

select * from act_hi_variable #--历史流程变量表 ，旧版本(5.13)
select * from ACT_HI_VARINST #--历史流程变量表 新版本，当前使用版本（5.19.0）



