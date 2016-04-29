/*
Copyright info
*/


package ua.dp.strahovik.yalantistask1.entities;


import android.os.Parcel;
import android.os.Parcelable;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Event implements Parcelable {

    private String id;
    private String eventState;
    private Date creationDate;
    private Date registrationDate;
    private Date deadlineDate;
    private Company responsible;
    private String description;
    private List<URI> photos;

    private String eventType;
    private int likeCounter;
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getEventState() {
        return eventState;
    }

    public void setEventState(String eventState) {
        this.eventState = eventState;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
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
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.eventState);
        dest.writeLong(creationDate != null ? creationDate.getTime() : -1);
        dest.writeLong(registrationDate != null ? registrationDate.getTime() : -1);
        dest.writeLong(deadlineDate != null ? deadlineDate.getTime() : -1);
        dest.writeParcelable(this.responsible, flags);
        dest.writeString(this.description);
        dest.writeList(this.photos);
        dest.writeString(this.eventType);
        dest.writeInt(this.likeCounter);
        dest.writeString(this.address);
    }

    public Event() {
    }

    protected Event(Parcel in) {
        this.id = in.readString();
        this.eventState = in.readString();
        long tmpCreationDate = in.readLong();
        this.creationDate = tmpCreationDate == -1 ? null : new Date(tmpCreationDate);
        long tmpRegistrationDate = in.readLong();
        this.registrationDate = tmpRegistrationDate == -1 ? null : new Date(tmpRegistrationDate);
        long tmpDeadlineDate = in.readLong();
        this.deadlineDate = tmpDeadlineDate == -1 ? null : new Date(tmpDeadlineDate);
        this.responsible = in.readParcelable(Company.class.getClassLoader());
        this.description = in.readString();
        this.photos = new ArrayList<URI>();
        in.readList(this.photos, URI.class.getClassLoader());
        this.eventType = in.readString();
        this.likeCounter = in.readInt();
        this.address = in.readString();
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
