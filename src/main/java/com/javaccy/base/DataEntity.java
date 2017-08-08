package com.javaccy.base;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.sql.Timestamp;

@Data
@Entity(name = "t_data_entity")
@Inheritance(strategy = InheritanceType.JOINED)
public class DataEntity {
    @Id
    private String id;
    private String delFlag;
    private Timestamp createTime;
    private Timestamp updateTime;

}
