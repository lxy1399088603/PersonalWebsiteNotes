package lxy.personal.website.entity;

/**
 * @Author lxy-- create table catalogue(
 * --  	cat_id int NOT NULL AUTO_INCREMENT,
 * --  	cat_level int default 1,
 * --  	cat_super_id int default 0,
 * --  	cat_area varchar(50) DEFAULT NULL,
 * --  	cat_link varchar(100) DEFAULT NULL,
 * --  	cat_content varchar(200) DEFAULT NULL,
 * --  	cat_sort int DEFAULT 0,
 * -- 	primary key(cat_id)
 * -- )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
 * @Date 2021/10/31 17:23
 * @Description
 */

public class Catalogue {
    private Integer cat_id;
    private Integer cat_level;
    private Integer cat_super_id;
    private String cat_area;
    private String cat_link;
    private String cat_content;
    private Integer cat_sort;
    private Integer cat_children_number;

    public Catalogue(Integer cat_id, Integer cat_level, Integer cat_super_id, String cat_area, String cat_link, String cat_content, Integer cat_sort, Integer cat_children_number) {
        this.cat_id = cat_id;
        this.cat_level = cat_level;
        this.cat_super_id = cat_super_id;
        this.cat_area = cat_area;
        this.cat_link = cat_link;
        this.cat_content = cat_content;
        this.cat_sort = cat_sort;
        this.cat_children_number = cat_children_number;
    }

    public Catalogue() {
    }

    public Integer getCat_id() {
        return cat_id;
    }

    public void setCat_id(Integer cat_id) {
        this.cat_id = cat_id;
    }

    public Integer getCat_level() {
        return cat_level;
    }

    public void setCat_level(Integer cat_level) {
        this.cat_level = cat_level;
    }

    public Integer getCat_super_id() {
        return cat_super_id;
    }

    public void setCat_super_id(Integer cat_super_id) {
        this.cat_super_id = cat_super_id;
    }

    public String getCat_area() {
        return cat_area;
    }

    public void setCat_area(String cat_area) {
        this.cat_area = cat_area;
    }

    public String getCat_link() {
        return cat_link;
    }

    public void setCat_link(String cat_link) {
        this.cat_link = cat_link;
    }

    public String getCat_content() {
        return cat_content;
    }

    public void setCat_content(String cat_content) {
        this.cat_content = cat_content;
    }

    public Integer getCat_sort() {
        return cat_sort;
    }

    public void setCat_sort(Integer cat_sort) {
        this.cat_sort = cat_sort;
    }

    public Integer getCat_children_number() {
        return cat_children_number;
    }

    public void setCat_children_number(Integer cat_children_number) {
        this.cat_children_number = cat_children_number;
    }

    @Override
    public String toString() {
        return getClass().getName()+"[" +
                "cat_id=" + cat_id +
                ", cat_level=" + cat_level +
                ", cat_super_id=" + cat_super_id +
                ", cat_area='" + cat_area + '\'' +
                ", cat_link='" + cat_link + '\'' +
                ", cat_content='" + cat_content + '\'' +
                ", cat_sort=" + cat_sort +
                ", cat_children_number=" + cat_children_number +
                ']';
    }
}
