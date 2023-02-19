package com.xxl.job.client.handler;


import java.util.Objects;

/**
 * @author sweeter
 * @date 2023/2/19
 */
public class BeanHandler extends AbstractHandler {

    private String name;

    private String description;

    private boolean deprecated = false;

    private String author;

    private final AbstractScheduledTask target;

    public BeanHandler(AbstractScheduledTask target) {
        this.target = target;
        this.name = target.name();
        this.description = target.description();
        this.author = target.author();
        this.deprecated = Objects.nonNull(target.getClass().getAnnotation(Deprecated.class));
    }

    public BeanHandler(AbstractScheduledTask target,boolean deprecated) {
        this.target = target;
        this.name = target.name();
        this.description = target.description();
        this.author = target.author();
        this.deprecated = deprecated;
    }

    @Override
    public void execute() throws Exception {
        this.target.execute();

    }

    @Override
    public void init() throws Exception {
        this.target.init();
    }

    @Override
    public void destroy() throws Exception {
        this.target.destroy();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
