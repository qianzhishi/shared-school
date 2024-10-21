package com.sharedschool.backend.typehandler;
import java.util.Date;
import lombok.Data;

@Data
public class FileInfo {
    private String name;
    private Date date;
    private String author;
    private String pathUrl;
}
