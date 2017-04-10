CREATE OR REPLACE PROCEDURE REPORT_REPAY_COLLECT_M1 (BATCH_DATE IN VARCHAR2, FLAG OUT NUMBER) IS
  REPORT_DATE DATE; --数据统计日期
  D_BEGIN_TIME DATE;--任务开始时间
BEGIN
  D_BEGIN_TIME := SYSDATE;

  --获取当期统计日期
  REPORT_DATE := TO_DATE(NVL(BATCH_DATE, TO_CHAR(SYSDATE - 1, 'yyyy-MM-dd')), 'yyyy-MM-dd');

  --记录存储过程执行日志
  PRC_ADD_BATCH_TASK_LOG(TO_CHAR(SYSDATE,'YYYY/MM/DD'),'REPORT_PAYDETAIL_COLLECT_M1','催收M1还款统计表',D_BEGIN_TIME,NULL,'0',NULL);

  --清除当前统计日期的统计数据，以便重新统计
  DELETE FROM REPORT_REPAYMENT_COLLECT_M1 WHERE REPAYMENT_DATE = REPORT_DATE;

  --插入催收统计基数出数据
  INSERT INTO REPORT_REPAYMENT_COLLECT_M1(ID, REPAYMENT_DATE, PERIOD_OF_TIME, GROUP_NAME, CURRENT_COLLECTOR, COLLECTOR_ID, CASE_AMOUNT, CASE_NUMBER, PTP_RECEIVED_PAYMENTS, NATURAL_REPAYMENT, TOTAL_REPAYMENT_AMOUNT, TARGETED_VALUE, RECOVER_RATE)
  SELECT
    SYS_GUID()
	, REPORT_DATE REPAYMENT_DATE
    ,RP.PERIOD_OF_TIME,RP.GROUP_NAME
    ,RP.CURRENT_COLLECTOR,RP.COLLECTOR_ID
    ,NULL --SUM(NVL(S.TOTAL_O_D_AMT,0)) CASE_AMOUNT--分案金额
    ,COUNT(RP.SYSKEY) CASE_NUMBER--分案数量
    ,SUM(CASE WHEN RP.CALL_RESULT IN ('YHK','PTP') THEN NVL(RP.REPAYMENT_AMOUNT,0) ELSE 0 END ) PTP_RECEIVED_PAYMENTS --PTP回款
    ,SUM(CASE WHEN RP.CALL_RESULT NOT IN ('YHK','PTP') THEN NVL(RP.REPAYMENT_AMOUNT,0) ELSE 0 END ) NATURAL_REPAYMENT --自然还款
    ,SUM(NVL(RP.REPAYMENT_AMOUNT,0)) TOTAL_REPAYMENT_AMOUNT --总还款额
    ,NULL TARGETED_VALUE,NULL RECOVER_RATE
  FROM REPORT_PAYDETAIL_COLLECT_M1 RP--,CS_ACCTSUM S
  WHERE RP.RECEIPT_DATE=REPORT_DATE-- AND RP.CUSTOMER_ID=S.CUSTOMER_ID
  GROUP BY RP.COLLECTOR_ID,RP.CURRENT_COLLECTOR,RP.PERIOD_OF_TIME,RP.GROUP_NAME;
  COMMIT;


  --添加完成日志
  PRC_ADD_BATCH_TASK_LOG(TO_CHAR(SYSDATE,'YYYY/MM/DD'),'REPORT_PAYDETAIL_COLLECT_M1','催收M1还款统计表',D_BEGIN_TIME,SYSDATE,'1',NULL);
  EXCEPTION
      WHEN OTHERS THEN
        ROLLBACK;
        --插入任务出错日志
        PRC_ADD_BATCH_TASK_LOG(TO_CHAR(SYSDATE,'YYYY/MM/DD'),'REPORT_PAYDETAIL_COLLECT_M1','催收M1还款统计表',D_BEGIN_TIME,SYSDATE,'2',SQLERRM);
END REPORT_REPAY_COLLECT_M1;
/


CREATE OR REPLACE PROCEDURE REPORT_REPAYDETAIL_M1(BATCH_DATE IN VARCHAR2,FLAG OUT NUMBER) IS
  /*=========================
     催收还款统计明细报表
  ===========================*/
  REPORT_DATE  DATE; --数据统计日期
  D_BEGIN_TIME DATE; --任务开始时间
BEGIN
  D_BEGIN_TIME := SYSDATE;

  --获取当期统计日期
  REPORT_DATE := TO_DATE(NVL(BATCH_DATE, TO_CHAR(SYSDATE - 1, 'yyyy-MM-dd')),'yyyy-MM-dd');

  --记录存储过程执行日志
  PRC_ADD_BATCH_TASK_LOG(TO_CHAR(SYSDATE, 'YYYY/MM/DD'),
                         'REPORT_REPAYDETAIL_M1',
                         '催收M1还款统计明细报表',
                         D_BEGIN_TIME,
                         NULL,
                         '0',
                         NULL);

  --清除当前统计日期的统计数据，以便重新统计
  DELETE FROM REPORT_PAYDETAIL_COLLECT_M1 WHERE RECEIPT_DATE = REPORT_DATE;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE R_TEMP_ACTION_M1';
  EXECUTE IMMEDIATE 'TRUNCATE TABLE R_TEMP_ACTION_M1_1';
  EXECUTE IMMEDIATE 'TRUNCATE TABLE R_TEMP_ACTION_M1_2';
  EXECUTE IMMEDIATE 'TRUNCATE TABLE R_TEMP_ACTION_M1_3';
  EXECUTE IMMEDIATE 'TRUNCATE TABLE R_TEMP_ACTION_M1_4';

  --查出M1催收昨天已还款的行动记录
  INSERT INTO R_TEMP_ACTION_M1_1(SYSKEY,ID, ACTIONDATE, ACTIONRESULT, ACTIONUSER, APPUSER, ACTION_LOGINNAME)
  SELECT SYSKEY,ID, ACTIONDATE, ACTIONRESULT, ACTIONUSER, APPUSER, ACTION_LOGINNAME
  FROM EXT_ACTION A
  WHERE  A.ACTIONRESULT='YHK' AND A.ACTIONDATE BETWEEN REPORT_DATE AND REPORT_DATE+0.999999
    AND A.ACTION_LOGINNAME IN
    (SELECT CA.USER_ID FROM EDA_VIRTUAL_GROUP VG,EDA_COLLECTION_ALLOT CA
    WHERE VG.GROUP_ID=CA.TACTICS_ID AND VG.PARENT_GROUP_ID='TDC02');
  COMMIT;

  --查出M1催收PTP承诺期内行动记录
  INSERT INTO R_TEMP_ACTION_M1_1(SYSKEY,ID, ACTIONDATE, ACTIONRESULT, ACTIONUSER, APPUSER, ACTION_LOGINNAME)
  SELECT A.SYSKEY,A.ID, A.ACTIONDATE, A.ACTIONRESULT, A.ACTIONUSER, A.APPUSER, A.ACTION_LOGINNAME
  FROM EXT_ACTION A ,EXT_PTP P
  WHERE P.ACTIONID=A.ID 
    AND REPORT_DATE BETWEEN P.PTPGETPTPDATE AND P.PTPDATE
    AND A.ACTION_LOGINNAME IN
    (SELECT CA.USER_ID FROM EDA_VIRTUAL_GROUP VG,EDA_COLLECTION_ALLOT CA
    WHERE VG.GROUP_ID=CA.TACTICS_ID AND VG.PARENT_GROUP_ID='TDC02');
  COMMIT;

  --获取每个案件最早下PTP的行动记录
  INSERT INTO R_TEMP_ACTION_M1_2(SYSKEY,ID, ACTIONDATE, ACTIONRESULT, ACTIONUSER, APPUSER, ACTION_LOGINNAME)
  SELECT SYSKEY,ID, ACTIONDATE, ACTIONRESULT, ACTIONUSER, APPUSER, ACTION_LOGINNAME
  FROM (SELECT SYSKEY,ID, ACTIONDATE, ACTIONRESULT, ACTIONUSER, APPUSER, ACTION_LOGINNAME
          ,ROW_NUMBER()OVER(PARTITION BY SYSKEY ORDER BY ACTIONDATE ASC ) AS RN
        FROM R_TEMP_ACTION_M1_1 A ) T WHERE RN=1;
  COMMIT;


  --获取M1自然还款的行动记录，需要剔除已下PTP/YHK的案件
  INSERT INTO R_TEMP_ACTION_M1_3(SYSKEY,ID, ACTIONDATE, ACTIONRESULT, ACTIONUSER, APPUSER, ACTION_LOGINNAME)
  SELECT SYSKEY,ID, ACTIONDATE, ACTIONRESULT, ACTIONUSER, APPUSER, ACTION_LOGINNAME
  FROM EXT_ACTION A
  WHERE  A.ACTIONRESULT NOT IN ('YHK','PTP') AND A.ACTIONDATE BETWEEN REPORT_DATE AND REPORT_DATE+0.999999
    AND A.ACTION_LOGINNAME IN
      (SELECT CA.USER_ID FROM EDA_VIRTUAL_GROUP VG,EDA_COLLECTION_ALLOT CA
      WHERE VG.GROUP_ID=CA.TACTICS_ID AND VG.PARENT_GROUP_ID='TDC02')
    AND A.SYSKEY NOT IN (SELECT SYSKEY FROM R_TEMP_ACTION_M1_2);
  COMMIT;

  --获取每个案件最晚下的自然还款的行动记录
  INSERT INTO R_TEMP_ACTION_M1_4(SYSKEY,ID, ACTIONDATE, ACTIONRESULT, ACTIONUSER, APPUSER, ACTION_LOGINNAME)
  SELECT SYSKEY,ID, ACTIONDATE, ACTIONRESULT, ACTIONUSER, APPUSER, ACTION_LOGINNAME
  FROM (SELECT SYSKEY,ID, ACTIONDATE, ACTIONRESULT, ACTIONUSER, APPUSER, ACTION_LOGINNAME
          ,ROW_NUMBER()OVER(PARTITION BY SYSKEY ORDER BY ACTIONDATE DESC ) AS RN
        FROM R_TEMP_ACTION_M1_3 A ) T WHERE RN=1;
  COMMIT;

  --把承诺还款行动记录和自然还款行动记录合并到R_TEMP_ACTION_M1表中
  INSERT INTO R_TEMP_ACTION_M1(SYSKEY, ID, ACCOUNTNUMBER, ACTION, ACTIONDATE, ACTIONID, ACTIONLEVEL, ACTIONNOTE, ACTIONRESULT, ACTIONTEAM, ACTIONTIME, ACTIONTYPE, ACTIONUSER, APPTEAM, APPUSER, EFFECTFLG, FEE, GENACTIONDATE, LETTERTYPE, ACTIONPRO, CONNID, LOSECONTACTFLAG, NO_REPAYMENT_DETAIL, NO_REPAYMENT_REASON, ACTION_LOGINNAME)
  SELECT SYSKEY, ID, ACCOUNTNUMBER, ACTION, ACTIONDATE, ACTIONID, ACTIONLEVEL, ACTIONNOTE, ACTIONRESULT, ACTIONTEAM, ACTIONTIME, ACTIONTYPE, ACTIONUSER, APPTEAM, APPUSER, EFFECTFLG, FEE, GENACTIONDATE, LETTERTYPE, ACTIONPRO, CONNID, LOSECONTACTFLAG, NO_REPAYMENT_DETAIL, NO_REPAYMENT_REASON, ACTION_LOGINNAME
  FROM R_TEMP_ACTION_M1_2
  UNION ALL
  SELECT SYSKEY, ID, ACCOUNTNUMBER, ACTION, ACTIONDATE, ACTIONID, ACTIONLEVEL, ACTIONNOTE, ACTIONRESULT, ACTIONTEAM, ACTIONTIME, ACTIONTYPE, ACTIONUSER, APPTEAM, APPUSER, EFFECTFLG, FEE, GENACTIONDATE, LETTERTYPE, ACTIONPRO, CONNID, LOSECONTACTFLAG, NO_REPAYMENT_DETAIL, NO_REPAYMENT_REASON, ACTION_LOGINNAME
  FROM R_TEMP_ACTION_M1_4;
  COMMIT;


  INSERT INTO REPORT_PAYDETAIL_COLLECT_M1(CALL_TIME, PERIOD_OF_TIME, GROUP_NAME, CURRENT_COLLECTOR, COLLECTOR_ID, CUSTOMER_ID, SYSKEY, CALL_RESULT, REPAYMENT_AMOUNT, TIME_INTERVAL, RECEIPT_DATE)
  SELECT T.ACTIONDATE 拨打时间
    ,VG.GROUP_NAME 时段
    ,VG.GROUP_LEADER 组别
    ,T.ACTIONUSER 催收员姓名
    ,T.ACTION_LOGINNAME 催收员ID
    ,R.CUSTOMER_ID 客户ID
    ,S.Syskey 案件号
    ,T.ACTIONRESULT 拨打结果
    ,R.RECEIPT_AMT 还款金额
    ,R.RECEIPT_DATE-TRUNC(T.ACTIONDATE)+1 时间间隔
    ,R.RECEIPT_DATE 还款时间
  FROM CS_RECEIPT R ,CS_ACCTSUM S,R_TEMP_ACTION_M1 T,EDA_VIRTUAL_GROUP VG,EDA_COLLECTION_ALLOT CA
  WHERE R.CUSTOMER_ID=S.CUSTOMER_ID AND S.SYSKEY=T.SYSKEY
    AND vg.group_id=ca.tactics_id AND CA.USER_ID=T.ACTION_LOGINNAME
    AND R.RECEIPT_DATE=REPORT_DATE;
  COMMIT;


  --添加完成日志
  PRC_ADD_BATCH_TASK_LOG(TO_CHAR(SYSDATE, 'YYYY/MM/DD'),
                         'REPORT_REPAYDETAIL_M1',
                         '催收N1还款统计明细报表',
                         D_BEGIN_TIME,
                         SYSDATE,
                         '1',
                         NULL);

EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    --插入任务出错日志
    PRC_ADD_BATCH_TASK_LOG(TO_CHAR(SYSDATE, 'YYYY/MM/DD'),
                           'REPORT_REPAYDETAIL_M1',
                           '催收M1还款统计明细报表',
                           D_BEGIN_TIME,
                           SYSDATE,
                           '2',
                           SQLERRM);

END REPORT_REPAYDETAIL_M1;
/

select t.syskey as syskey,
       t.customerid as customerid,
       t.customername as customername,
       t.currcollectorleader as currcollectorleader,
       to_char(t.action_date, 'yyyy-MM-dd hh24:MI:ss') as action_date,
       t.action_result as actionresult,
       t.transferdate as transferdate,
       t.transfer_user as transferuser,
       t.transfer_reason as transferreason,
       t.city as city,
       t.province as province,
       t.currcollector as currcollector,
       t.comp_id as compid,
       t.law_current_stage_id as lawcurrentstageid,
       t.law_current_stage_date as lawcurrentstagedate,
       t.law_next_stage_id as lawnextstageid,
       t.law_next_stage_date as lawnextstagedate,
       t.writeoffreason as writeoffreason,
       t.dcatype as dcatype,
       t.dcatrantimes as dcatrantimes,
       t.dca_ahead_status as dcaaheadstatus,
       t.ptp_recheck_date as ptpcheckdate,
       p.id_number as idnumber,
       a.total_o_d_days as totaloddays, --
       a.total_o_d_amt as totalodamt,
       a.total_o_d_principal as totalodprincipal,
       a.total_o_d_interest as totalodinterest,
       a.total_o_d_penalty_interest as totalodpenaltyinterest,
       a.total_o_d_fee as totalodfee,
       g.group_leader as group_leader,
       l1.stagedescription as lawcurrentstagename,
       l2.stagedescription as lawnextstagename,
       o.dcarealallocdate as dcarealallocdate,
       o.dcaplanbldate as dcaplanbldate,
       act.actionnote as actionnote,
       p.qq_no as qqno,
       p.weixin_no as weixinno,
       cp.phone_number as phonenumber,
       TRUNC(SYSDATE) - case
         when ACT.ACTIONDATE is null then
          TRUNC(t.currcollectordate)
         when ACT.ACTIONDATE IS NOT NULL AND ACT.APPUSER = T.currcollector THEN
          TRUNC(ACT.ACTIONDATE)
         else
          TRUNC(t.currcollectordate)
       END AS DDATE
  from cs_person p, cs_case_main t
  left join cs_acctsum a
    on t.syskey = a.syskey
  LEFT JOIN (SELECT EA.*
               FROM EXT_ACTION EA,
                    (SELECT a.syskey, MAX(a.id) row_id
                       FROM EXT_ACTION A
                      GROUP BY a.syskey) z
              WHERE EA.ID = row_id
                AND EA.syskey = z.syskey) ACT
    ON ACT.SYSKEY = T.SYSKEY
  left join cs_phones cp
    on cp.customer_id = t.customerid
   and cp.phone_type = '00001'
  left join eda_virtual_group g --组长
    on t.tactics_id_child = g.group_id
  left join eda_litistate l1 --法务阶段字典表
    on l1.id = t.law_current_stage_id
  left join eda_litistate l2
    on l2.id = t.law_next_stage_id
  left join cs_outsrc_allot_record o  --委外分案记录表
    on (t.syskey = o.syskey and o.dis_valid = 1)
 where a.total_o_d_days >= 0
   and p.customer_id = t.customerid
   and t.CURRCOLLECTOR = 'zuojuan'  --当前催收员
   and t.CURRSECTOR = 'TDC'         --前催阶段
   and t.ptp = '1'                  --我也不知道这是代表ptp的什么
   and t.currstates <> 0
 order by nvl(t.ACTION_DATE, date '1900-01-01') desc
