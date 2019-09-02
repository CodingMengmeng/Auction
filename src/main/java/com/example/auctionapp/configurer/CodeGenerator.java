package com.example.auctionapp.configurer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;


/**
 * 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
 */
public class CodeGenerator {


    /**
     * 要生成的数组表名
     */
    public static String[] str = {

            "lottery_info",
            "lottery_result"
    };


    public static void main(String[] args) {

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();

//        文件覆盖
        gc.setFileOverride(false);
//        策略配置
        String projectPath = System.getProperty("user.dir");
//        指定的目录
        gc.setOutputDir(projectPath + "/src/main/java");
//        开发人员
        gc.setAuthor("孔邹祥");
//        是否开启Swagger2模式
        gc.setSwagger2(false);

//        生产指定字段类型
        gc.setIdType(IdType.AUTO);
//        生成基本的resultMap
        gc.setBaseResultMap(true);
//        生成基本的SQL片段
        gc.setBaseColumnList(true);

//        是否打开输出目录
        gc.setOpen(false);

        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://192.168.0.101:3306/auction?useUnicode=true&useSSL=false&characterEncoding=utf8");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        mpg.setDataSource(dsc);

//        包配置
        PackageConfig pc = new PackageConfig();

//        自定义输入模块名
        pc.setParent("com.example.auctionapp");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";


        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();


        // 自定义配置会被优先输出
       /* focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });*/

        cfg.setFileOutConfigList(focList);


        mpg.setCfg(cfg);


        TemplateConfig tc = new TemplateConfig();
        mpg.setTemplate(tc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();

//        是否生成实体时，生成字段注解
        strategy.entityTableFieldAnnotationEnable(true);

//        下划线转驼峰命名
        strategy.setNaming(NamingStrategy.underline_to_camel);

//        自定义命名规则 下划线转驼峰命名
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);

//        lombok模式
        strategy.setEntityLombokModel(true);

//        是否为RestController风格
        strategy.setRestControllerStyle(true);

//        自定义继承的Service类全称，带包名
//        strategy.setSuperServiceClass("");
//        自定义继承的ServiceImpl类全称，带包名
//        strategy.setSuperServiceImplClass("");

//        修改替换成你需要的表名，多个表名传数组

        strategy.setInclude(str);
//        自定义基础的Entity类，公共字段
//        strategy.setSuperEntityColumns("id");

//       【实体】是否为构建者模型（默认 false）

        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");

        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

}
