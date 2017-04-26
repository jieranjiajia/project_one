create or replace function account_function(tdc varchar2) return sys_refcursor is
v_cursor sys_refcursor;
begin
    open v_cursor for 
				SELECT B.CONTRACT_NO  AS CONTRACTNO, 
   					   B.PRODUCT_TYPE_NAME   AS PRODUCTTYPENAME,                     
                       NVL(U.FINANCED_AMOUNT,0.0)  AS FINANCEDAMOUNT,   
                       NVL(B.O_D_PRINCIPAL_AMT,0.0)   AS ODPRINCIPALAMT,  
                       NVL(B.TOTAL_O_D_AMT,0.0)    AS TOTALODAMT,     
                       NVL(B.O_D_INTEREST_AMT,0.0)   AS ODINTERESTAMT,  
                       NVL(B.O_D_PENALTY_INTEREST_AMT,0.0)   AS ODPENALTYINTERESTAMT,   
                       NVL(B.O_D_CHARGES_AMT,0.0)    AS ODCHARGESAMT,   
                       TO_CHAR(B.O_D_DUE_DATE,'yyyy-mm-dd') AS ODDUEDATE,   
                       NVL(B.DELINQUENCY_TERM,0)  AS DELINQUENCYTERM,  
                       NVL(B.O_D_AMT,0.0)      AS ODAMT,   
                       NVL(B.O_D_PAYMENT,0.0) AS ODPAYMENT,   
                       U.RESOURCES_ORG  AS RESOURCESORG,   
                       NVL(B.PAID_TERM,0)    AS PAID_TERM,   
                       NVL(U.TERM_MONTH,0)    AS TERM_MONTH,    
                       TO_CHAR(U.FIRST_PAYMENT_DATE,'yyyy-mm-dd') AS FIRSTPAYMENTDATE,  
                       NVL(TO_CHAR(B.NEXT_INSTALMENT_DATE,'yyyy-mm-dd'),' ') AS NEXTINSTALMENTDATE ,
                       B.SYSKEY as SYSKEY
				FROM CS_CONTRACT U ,(select A.* from cs_account A
                                                INNER JOIN Cs_Case_Main E ON E.SYSKEY =a.syskey WHERE E.CURRSECTOR=tdc)B 
                where B.Contract_No = U.CONTRACT_NO 
                ORDER BY B.NEXT_INSTALMENT_DATE ASC;
        return v_cursor;
end account_function;



create or replace function f_sys_getseqid(  
    v_seqname           IN VARCHAR2,  
    v_provincecode      IN VARCHAR2    --省编码  
) return Varchar2  
IS  
    iv_date             VARCHAR2(8);  
    iv_seqname          VARCHAR2(50);  
    iv_sqlstr           VARCHAR2(200);  
    iv_seq              VARCHAR2(8);  
    iv_seqid            VARCHAR2(16);  
BEGIN  
    iv_seqname := LOWER(TRIM(v_seqname));  
    iv_sqlstr := 'SELECT '||iv_seqname||'.nextval FROM DUAL';  
    EXECUTE IMMEDIATE iv_sqlstr INTO iv_seq;--执行动态的sql语句，执行相似的一组语句  
    IF v_seqname = 'SEQ_FUNCROLE_ID' THEN  
      iv_seqid:= 'ESS' || LPAD(iv_seq,5,'0');  
    ELSE  
      SELECT substrb(v_provincecode,1,2)||TO_CHAR(SYSDATE,'yymmdd') INTO iv_date FROM DUAL;  
      iv_seqid:= iv_date || LPAD(iv_seq,8,'0');  
    END IF;  
    RETURN iv_seqid;  
EXCEPTION  
    WHEN OTHERS THEN  
    RETURN NULL;  
END;
