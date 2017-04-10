SELECT (NVL(TEM.SUM1, 0) + NVL(TEM.SUM2, 0) - NVL(TEM.SUM3, 0) -
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
                          from cs_loan where contract_no = '10620455003') t1
                     on t.objectno = t1.loan_no
                     or t.relativeobjectno = t1.loan_no
                 --期还款日大于当天
                  where T.PAYDATE >= TO_CHAR(SYSDATE, 'YYYY/MM/DD')) tem
         --取聚合的第一条数据
          where tem.row_number = 1