springbatch-eodcsv-import
----
- 使用Mybatis-Plus的批量插入功能，并使用数据库线程池 
- 支持降低为单行且失败就输出的重试策略，最后需要输出成功处理的行数和重试失败的行数 
- 支持事务隔离，一开始清空整张表，当所有批量导入操作完成才submit事务。 
- 一运行application马上执行批处理
- 使用lombok注解，使用log4j2来输出日志 
- 使用yaml配置文件 
- 使用Apache comment csv来读取csv，注意csv文件可能比较大，几百兆甚至1G都有可能，要防止内存溢出。
- 导入的实体类包含以下字段accountNumber,amount,balanceDate

# Author
Moshow郑锴@https://zhengkai.blog.csdn.net/ https://github.com/moshowgame

# how to create spring batch meta data tables
https://docs.spring.io/spring-batch/reference/schema-appendix.html#metaDataSchema`

# how to run spring batch
Run the EodCsvImportApplication directly
