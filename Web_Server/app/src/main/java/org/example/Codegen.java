package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import org.jooq.meta.jaxb.*;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.codegen.GenerationTool;

public class Codegen {
    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.configure().directory("/Users/hanbao/cheng_wenhan_002330833_assignments/WebServer").load();
        String databaseUrl = dotenv.get("DATABASE_URL");
        String databaseUsername = dotenv.get("DATABASE_USERNAME");
        String databasePassword = dotenv.get("DATABASE_PASSWORD");
        // 输出日志确认环境变量是否成功加载
        System.out.println("Using Database URL: " + databaseUrl);
        System.out.println("Using Database Username: " + databaseUsername);
        System.out.println("Using Database Password: " + databasePassword);

        if (databaseUrl == null || databaseUsername == null || databasePassword == null) {
            System.out.println("One or more environment variables are missing.");
        }
        // JOOQ 配置
        Configuration configuration = new Configuration()
                .withJdbc(new Jdbc()
                        .withDriver("org.postgresql.Driver")
                        .withUrl(databaseUrl)
                        .withUser(databaseUsername)
                        .withPassword(databasePassword))
                .withGenerator(new Generator()
                        .withGenerate(new org.jooq.meta.jaxb.Generate().withComments(false))
                        .withDatabase(new Database()
                                .withName("org.jooq.meta.postgres.PostgresDatabase")
                                .withIncludes(".*")
                                .withExcludes("")
                                .withInputSchema("public"))
                        .withTarget(new Target()
                                .withPackageName("org.example.generated")
                                .withDirectory("/Users/hanbao/cheng_wenhan_002330833_assignments/WebServer/app/src/main/java")));

        // 运行代码生成器
        GenerationTool.generate(configuration);
    }
}