package com.example.basicauthentication.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_table")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "User's name cannot be empty.")
    private String name;
    @NotEmpty(message = "Email cannot be empty.")
    private String email;
    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name="users_roles",
            joinColumns={@JoinColumn(name="USER_ID", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="ROLE_ID", referencedColumnName="ID")})
    @JsonIgnore
    private List<Role> roles = new ArrayList<>();
    @JsonIgnore
    @NotEmpty(message = "Password cannot be empty.")
    private String password;

    @Lob
    @Column(name = "profile_picture", length = Integer.MAX_VALUE)
    private byte[] profilePicture;


//    public Collection<Project> getProjects() {
//        return projects;
//    }
//
//    public void setProjects(Collection<Project> projects) {
//        this.projects = projects;
//    }
//    @ManyToMany(mappedBy = "members")
//    private Collection<Project> projects;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public User(Long id, String name, String email, String password, List<Role>roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles=roles;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}
