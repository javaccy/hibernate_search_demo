package com.javaccy.entity;

import com.javaccy.base.DataEntity;
import lombok.Data;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.type.descriptor.sql.LobTypeMappings;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Data
@Entity(name = "t_user")
@Indexed(index = "user")
@Analyzer(impl=IKAnalyzer.class)//分词器
public class User extends DataEntity{

    @Id
    @DocumentId
    private String id;
    private String username;
    private String password;
    @Column(columnDefinition = "TEXT")
    @Field
    private String about;

    @Field
    public String getAbout() {
        return about;
    }
}
