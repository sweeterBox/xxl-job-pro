package com.xxl.job.admin.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;


/**
 * @author sweeter
 * @description JPA自定义命名策略
 * @date 2022/9/4
 */
public class JpaPhysicalNamingStrategy extends SpringPhysicalNamingStrategy {

    private String prefix = "xxl_job_";

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
       // return super.toPhysicalTableName(name, jdbcEnvironment);
        return this.apply(name, jdbcEnvironment);
    }

    private Identifier apply(Identifier name, JdbcEnvironment jdbcEnvironment) {
        if (name == null) {
            return null;
        } else {
            String text = name.getText();
            if (!text.startsWith(prefix)) {
                text = prefix + text;
            }
            StringBuilder builder = new StringBuilder(text.replace('.', '_'));
            for(int i = 1; i < builder.length() - 1; ++i) {
                if (this.isUnderscoreRequired(builder.charAt(i - 1), builder.charAt(i), builder.charAt(i + 1))) {
                    builder.insert(i++, '_');
                }
            }
            return this.getIdentifier(builder.toString(), name.isQuoted(), jdbcEnvironment);
        }
    }

    private boolean isUnderscoreRequired(char before, char current, char after) {
        return Character.isLowerCase(before) && Character.isUpperCase(current) && Character.isLowerCase(after);
    }

}
