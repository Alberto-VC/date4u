package com.tutego.date4u.core.profile;

import com.tutego.date4u.core.photo.Photo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Access(AccessType.FIELD)
public class Profile {
    public static final int FEE = 1;
    public static final int MAA = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;
    private LocalDate birthdate;
    private short manelength;
    private byte gender;

    @Column(name = "attracted_to_gender")
    private Byte attractedToGender;

    @Column(length = 2048)
    private String description;
    private LocalDateTime lastseen;

    @JsonIgnore
    @OneToOne(mappedBy = "profile")
    private Unicorn unicorn;

    @JsonIgnore
    @OneToMany(mappedBy = "profile", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Photo> photos = new ArrayList<>();

    public Profile(String nickname, LocalDate birthdate, int manelength,
                   int gender, @Nullable Integer attractedToGender, String description
            , LocalDateTime lastseen) {
        setNickname(nickname);
        setBirthdate(birthdate);
        setManelength((short) manelength);
        setGender(gender);
        setAttractedToGender(attractedToGender);
        setDescription(description);
        setLastseen(lastseen);
    }

    public Profile() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public short getManelength() {
        return manelength;
    }

    public void setManelength(short manelength) {
        this.manelength = manelength;
    }

    public byte getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = (byte) gender;
    }

    public @org.springframework.lang.Nullable Integer getAttractedToGender() {
        return attractedToGender == null ? null : attractedToGender.intValue();
    }

    public void setAttractedToGender(@org.springframework.lang.Nullable Integer attractedToGender) {
        this.attractedToGender = attractedToGender == null ? null : attractedToGender.byteValue();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getLastseen() {
        return lastseen;
    }

    public void setLastseen(LocalDateTime lastseen) {
        this.lastseen = lastseen;
    }

    public Unicorn getUnicorn() {
        return unicorn;
    }

    public void setUnicorn(Unicorn unicorn) {
        this.unicorn = unicorn;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}
