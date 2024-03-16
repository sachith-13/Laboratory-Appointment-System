package com.djamware.springsecuritymongo.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "labs")
public class Lab {
    @Id
    private String id;
    @Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
    private String userId;
    private String labName;

    private String description;

    public Lab(String id, String userId, String labName, String description) {
        this.id = id;
        this.userId = userId;
        this.labName = labName;
        this.description = description;
    }

    public Lab() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Lab{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", labName='" + labName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}