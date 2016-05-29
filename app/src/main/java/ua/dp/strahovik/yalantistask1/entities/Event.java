/*
Copyright info
*/


package ua.dp.strahovik.yalantistask1.entities;


import com.google.gson.annotations.SerializedName;

import java.net.URI;
import java.util.Date;
import java.util.List;

public class Event {

    @SerializedName("id")
    private int id;

    @SerializedName("ticket_id")
    private String idForOutput;

    @SerializedName("state")
    private EventState eventState;

    @SerializedName("created_date")
    private Date creationDate;

    @SerializedName("start_date")
    private Date registrationDate;

    @SerializedName(value = "deadline", alternate = "completed_date")
    private Date deadlineDate;

    private Company responsible;

    @SerializedName("body")
    private String description;

    private List<URI> photos;

    @SerializedName("title")
    private String eventType;

    @SerializedName("likes_counter")
    private int likeCounter;

    @SerializedName("address")
    private Address address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdForOutput() {
        return idForOutput;
    }

    public void setIdForOutput(String idForOutput) {
        this.idForOutput = idForOutput;
    }

    public EventState getEventState() {
        return eventState;
    }

    public void setEventState(EventState eventState) {
        this.eventState = eventState;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public Company getResponsible() {
        return responsible;
    }

    public void setResponsible(Company responsible) {
        this.responsible = responsible;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<URI> getPhotos() {
        return photos;
    }

    public void setPhotos(List<URI> photos) {
        this.photos = photos;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getLikeCounter() {
        return likeCounter;
    }

    public void setLikeCounter(int likeCounter) {
        this.likeCounter = likeCounter;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", eventState='" + eventState + '\'' +
                ", creationDate=" + creationDate +
                ", registrationDate=" + registrationDate +
                ", deadlineDate=" + deadlineDate +
                ", responsible=" + responsible +
                ", description='" + description + '\'' +
                ", photos=" + photos +
                ", eventType='" + eventType + '\'' +
                ", likeCounter=" + likeCounter +
                ", address='" + address + '\'' +
                "} \n";
    }


}
