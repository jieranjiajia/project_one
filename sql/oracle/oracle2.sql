create or replace procedure init_next_should_pay is
  /* 当批量跑完之后，把下期应还款同步到csaccount表中去*/
  D_BEGIN_TIME DATE; --任务开始时间
begin
  --添加开始日志
  D_BEGIN_TIME := sysdate;
  PRC_ADD_BATCH_TASK_LOG(TO_CHAR(SYSDATE, 'YYYY/MM/DD'),
                         'init_next_should_pay',
                         '更新下期应还款',
                         D_BEGIN_TIME,
                         null,
                         '0',
                         NULL);

  merge into cs_account t
  using (SELECT (NVL(TEM.SUM1, 0) + NVL(TEM.SUM2, 0) - NVL(TEM.SUM3, 0) -
                NVL(TEM.SUM4, 0)) NEX,
                tem.contractno contractno
           from (select t1.loan_no,
                        t.paydate,
                        t1.contract_no contractno,
                        --按合同号，和下期应还的期数聚合
                        SUM(T.PAYPRINCIPALAMT) OVER(PARTITION BY t1.LOAN_NO, T.SEQID ORDER BY T.PAYDATE ASC) SUM1,
                        SUM(T.PAYINTEAMT) OVER(PARTITION BY t1.LOAN_NO, T.SEQID ORDER BY T.PAYDATE ASC) SUM2,
                        SUM(T.ACTUALPAYPRINCIPALAMT) OVER(PARTITION BY t1.LOAN_NO, T.SEQID ORDER BY T.PAYDATE ASC) SUM3,
                        SUM(T.ACTUALPAYINTEAMT) OVER(PARTITION BY t1.LOAN_NO, T.SEQID ORDER BY T.PAYDATE ASC) SUM4,
                        ROW_NUMBER() OVER(PARTITION BY t1.LOAN_NO ORDER BY T.PAYDATE ASC) ROW_NUMBER
                   from acct_payment_schedule t
                 --匹配借据表
                   join (select to_char(loan_no) loan_no, contract_no
                          from cs_loan) t1
                     on t.objectno = t1.loan_no
                     or t.relativeobjectno = t1.loan_no
                 --期还款日大于当天
                  where T.PAYDATE >= TO_CHAR(SYSDATE, 'YYYY/MM/DD')) tem
         --取聚合的第一条数据
          where tem.row_number = 1) tem1
  on (t.contract_no = tem1.contractno)
  when matched then --当匹配上后执行update操作
    update set t.next_instalment_amount = tem1.nex;
  --更新完成日志
  PRC_ADD_BATCH_TASK_LOG(TO_CHAR(SYSDATE, 'YYYY/MM/DD'),
                         'init_next_should_pay',
                         '更新下期应还款',
                         D_BEGIN_TIME,
                         SYSDATE,
                         '1',
                         NULL);
  --发生异常回滚
exception
  when others then
    rollback;
    --添加异常日志
    PRC_ADD_BATCH_TASK_LOG(TO_CHAR(SYSDATE, 'YYYY/MM/DD'),
                           'init_next_should_pay',
                           '更新下期应还款',
                           D_BEGIN_TIME,
                           SYSDATE,
                           '2',
                           NULL);

end init_next_should_pay;
