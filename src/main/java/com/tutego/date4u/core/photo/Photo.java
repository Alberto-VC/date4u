package com.tutego.date4u.core.photo;

import com.tutego.date4u.core.profile.Profile;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

@Entity
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "profile_fk")
    public Profile profile;

    @NotNull
    @Pattern(regexp = "[\\w_-]{1,200}")
    public String name;
    public boolean isProfilePhoto;
    @NotNull
    @Past
    public LocalDateTime created;

    public Photo(){}

    public Photo(Long id, Profile profile, String name, boolean isProfilePhoto, LocalDateTime created){
        this.id = id;
        this.profile = profile;
        this.name = name;
        this.isProfilePhoto = isProfilePhoto;
        this.created = created;
    }

    @Override
    public String toString(){
        return "Photo{" +
                "id=" + id +
                ", profile=" + profile +
                ", name='" + name + '\'' +
                ", isProfilePhoto=" + isProfilePhoto +
                ", created=" + created +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isProfilePhoto() {
        return isProfilePhoto;
    }

    public void setProfilePhoto(boolean profilePhoto) {
        isProfilePhoto = profilePhoto;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
