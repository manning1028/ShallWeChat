package com.manning.entity;


import javax.persistence.*;

/**
 * @author: zengjing
 * @version: v1.0
 * @description:
 * @date: 2019/10/23 15:49
 */
 @Entity
 @Table(name="tb_user")
public class TbUser {
    private  Integer id;
    private String nickname;
    private String telphone;
    private String wechatId;
    private String email;
    private String password;
    private String sign;
    private String profilePhoto;
    private String address;

   @Id
    @GeneratedValue
     @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
 @Column(name = "nickname")
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
   @Column(name = "telphone")
    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }
  @Column(name = "wechat_id")
    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
  @Column(name = "sign")
    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
   @Column(name = "profile_photo")
    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
   @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
