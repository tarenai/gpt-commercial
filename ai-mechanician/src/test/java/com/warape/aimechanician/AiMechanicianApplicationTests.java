package com.warape.aimechanician;

import java.util.Collections;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.fill.Column;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AiMechanicianApplicationTests {

  @Test
  public void generateCode () {
    generateByTables();
  }

  private void generateByTables () {
    //1、配置数据源
    FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/ai_mechanician", "root", "123456")
        .globalConfig(builder -> {
          builder.author("warape") // 设置作者名
              .outputDir(System.getProperty("user.dir") + "/src/main/java")   //设置输出路径
              .commentDate("yyyy-MM-dd hh:mm:ss")   //注释日期
              .dateType(DateType.ONLY_DATE)   //定义生成的实体类中日期的类型 TIME_PACK=LocalDateTime;ONLY_DATE=Date;
              .enableSwagger()   //开启 swagger 模式
              .disableOpenDir();   //禁止打开输出目录，默认打开
        }).packageConfig(builder -> builder.parent("com.warape.aimechanician") // 设置父包名
            .entity("entity")   //pojo 实体类包名
            .service("service") //Service 包名
            .serviceImpl("impl") // ***ServiceImpl 包名
            .mapper("mapper")   //Mapper 包名
            .xml("mapper")  //Mapper XML 包名
            .controller("controller") //Controller 包名
            .other("utils") //自定义文件包名
            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/src/main/resources/mapper")))
        .strategyConfig(builder ->
            builder
//                .addInclude("tb_banner") // 设置需要生成的表名 可边长参数“user”, “user1”
//            .addTablePrefix("tb_", "c_") // 设置过滤表前缀
            .serviceBuilder()//service策略配置
            .formatServiceFileName("%sService")
            .formatServiceImplFileName("%sServiceImpl")
            .entityBuilder()// 实体类策略配置
            .idType(IdType.AUTO)//主键策略  雪花算法自动生成的id
            // 自动填充配置
            .addTableFills(new Column("create_by", FieldFill.INSERT)) //根据数据库字段名适配
            .addTableFills(new Column("update_by", FieldFill.INSERT_UPDATE)) //根据数据库字段名适配
            .addTableFills(new Column("create_time", FieldFill.INSERT)) //根据数据库字段名适配
            .addTableFills(new Column("update_time", FieldFill.INSERT_UPDATE)) //根据数据库字段名适配
            .addTableFills(new Column("yn", FieldFill.INSERT)) //根据数据库字段名适配
//            .addTableFills(new Property("gmtModified", FieldFill.INSERT_UPDATE))//根据生成实体类的属性名适配
            .enableLombok() //开启lombok
            .logicDeleteColumnName("yn")// 说明逻辑删除是哪个字段
            .enableTableFieldAnnotation()// 属性加上注解说明
            .controllerBuilder() //controller 策略配置
            .formatFileName("%sController")
            .enableRestStyle() // 开启RestController注解
            .mapperBuilder()// mapper策略配置
            .formatMapperFileName("%sMapper")
            .enableMapperAnnotation()//@mapper注解开启
            .formatXmlFileName("%sMapper")).execute();


  }

}
