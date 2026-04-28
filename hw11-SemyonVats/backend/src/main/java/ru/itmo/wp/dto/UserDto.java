package ru.itmo.wp.dto;
import java.util.Date;

public class UserDto {
    private Long id;
    private Date creationTime;
    private String login;

    public UserDto() {
    }


    public UserDto(Long id, Date creationTime, String login) {
        this.id = id;
        this.creationTime = creationTime;
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}