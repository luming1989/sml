# sml
sql markup language(sql标记语言)

1、mybatis,ibatis书写sql的方便，但调整xml配置文件整体服务需要重新启动

2、一段完整的sql查询包括    查询sql+参数集
# example
## sql

      select * from table t where 1=1 
      <isNotEmpty property="a"> and t.a=#a#</isNotEmpty>
      <isNotEmpty property="b"> and t.b in(#b#)</isNotEmpty>

##code
  
    SqlResolvers sqlResolvers=new SqlResolvers(new JsEl());
    
		sqlResolvers.init();
		
		Rst rst=sqlResolvers.resolverLinks(sql,SMLParams.newSMLParams().add("a","v1")
		.add("b",new String[]{"v2","v3","v4"}).reinit());
		
		System.out.println(rst.getSqlString());//print sqlString
		
		System.out.println(rst.getParamObjects());//print params
		
##result
	
	
	  sqlString:select * from table t where 1=1 and t.a=? and t.b in(?,?,?)
	  params   :[v1, v2, v3, v4]
    
