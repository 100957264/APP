“whereOr” where语句里面写的条件都是用“且”连接，whereOr里的语句使用“或”连接
“distinct”  直接过滤掉重负字段
“limit”  分页n个一页，一般和offset结合使用
“offset” 忽略查询出的前n条结果
“orderAsc” 以字段升序排序
“orderDesc”以字段降序
“preferLocalizedStringOrder” 本地化字符串排序
“orderCustom” 自定义排序 里面需要传两个参数： 一个属性 和对应的排序方案 ASC 或是 DESC
“orderRaw”  也是自定义排序， 把字段和 排序方案 写在一个字符串传入
“stringOrderCollation” 也是自定义排序 可以合并多个升降排序方案 以日期升序 且 价格降序

一个简单的where语句大概是这样，在where的括号里面可以并列的写很多条件，其中全部以“且” 来连接。除了上面的“in”和“eq”还有很多其他判断条件
“notEq” 和eq相反，别傻傻在再去外面敲“！”取反
“notIn” 同上
“or” 或者
“like” 就是sql语句的LIKE  "%"+string+"%"
“between” 也就是BETWEEN ? AND ?  可以取两个值的区间 (但是这条语句要慎用，不同的数据库不一样，有的是A<条件<B，有的是A<=条件<=B)
“gt” 相当于 >
“ge”相当于 >=
“lt” 相当于 <
“le”相当于  <=
“isNull” 为空
“notIsNull” 不为空

if (newVersion > oldVersion) {
    String sql = "CREATE TABLE NODE_RICH_AND_INFO(CONTENT  TEXT,ANSWER_START_TIME   VARCHAR(12),END_TIME  TIME,START_TIME      TIME,ANSWER_SHOW    BOOLEAN,ANSWER_SHOW_STATUS    INTEGER,STATUS  INTEGER,ID    INTEGER  NOT NULL PRIMARY KEY)";
    String sql1 = "CREATE TABLE SAVE_STUDENT_ANSWER(ANSWER        VARCHAR(200) ,STUDENT_NO   VARCHAR(20),ANSWER_SHEET_ANSWER_ID INTEGER  NOT NULL,ANSWER_SHEET_TEXT_ID  INTEGER   NOT NULL,ID      INTEGER)";
    String sql2 = "CREATE TABLE STUDENT_ANSWER(ACTUAL_SCORE     INTEGER ,ANALYSIS   TEXT,ANSWER   TEXT,ANSWER_SHEET_ANSWER_ID   INTEGER  NOT NULL,ANSWER_SHEET_TEXT_ID  INTEGER   NOT NULL,CLASS_ROOM_ID    INTEGER,CREATE_TIME   TIMESTAMP   ,ID      INTEGER ,IS_TRUE INTEGER ,SCORE INTEGER,STATUS INTEGER,STUDENT_ID INTEGER,STUDENT_NAME VARCHAR(32),TYPE INTEGER,UPDATE_TIME TIMESTAMP)";
    String sql3 = "CREATE TABLE ANSWER(ANALYSIS   TEXT,ANSWER   TEXT,ANSWER_SHEET_TEXT_ID INTEGER,CREATE_TIME TIME,Id INTEGER,SCORE INTEGER,TYPE INTEGER,UPDATE_TIME TIME)";
    String sql4 = "CREATE TABLE ATTACHMENT_LIST_INFO(ANSWER_SHEET_TEXT_ID   INTEGER,ATTACHMENT_ID   TEXT,ATTACHMENT_NAME TEXT,ATTACHMENT_SIZE_IN_STRING TEXT,CREATE_TIME TIME,ID INTEGER,UPDATE_TIME TIME)";
    String sql7 = "ALTER  TABLE NODES_BEAN  ADD ORDER_WEIGHT  INTEGER";
    String sql8 = "ALTER  TABLE NODES_BEAN  ADD  STATUS INTEGER";
    String sql9 = "ALTER  TABLE NODES_BEAN  ADD  TEACHER_ID INTEGER";
    String sql10 = "ALTER  TABLE NODES_BEAN  ADD  IS_DELETE BOOLEAN";
    String sql11 = "ALTER  TABLE NODES_BEAN  ADD  IS_END BOOLEAN";
    String sql12 = "ALTER  TABLE NODES_BEAN  ADD  CREATE_TIME TIME";
    String sql13 = "ALTER  TABLE NODES_BEAN  ADD  UPDATE_TIME TIME";
    String sql6 = "ALTER  TABLE MESSAGE  ADD  ASSOCIATED_ID INTEGER;";
    db.execSQL(sql);
    db.execSQL(sql1);
    db.execSQL(sql2);
    db.execSQL(sql3);
    db.execSQL(sql4);
    db.execSQL(sql6);
    db.execSQL(sql7);
    db.execSQL(sql8);
    db.execSQL(sql9);
    db.execSQL(sql10);
    db.execSQL(sql11);
    db.execSQL(sql12);
    db.execSQL(sql13);