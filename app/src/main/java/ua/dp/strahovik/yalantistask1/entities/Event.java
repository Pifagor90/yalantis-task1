/*
Copyright info
*/


package ua.dp.strahovik.yalantistask1.entities;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.List;

public class Event {

    private String mId;
    private String mEventState;
    private Date mCreationDate;
    private Date mRegistrationDate;
    private Date mDeadlineDate;
    private Company mResponsible;
    private String mDescription;
    private List<URI> mPhotos;

    private String mEventType;
    private int mLikeCounter;
    private String mAddress;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public Date getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(Date creationDate) {
        mCreationDate = creationDate;
    }

    public Date getRegistrationDate() {
        return mRegistrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        mRegistrationDate = registrationDate;
    }

    public Date getDeadlineDate() {
        return mDeadlineDate;
    }

    public void setDeadlineDate(Date deadlineDate) {
        mDeadlineDate = deadlineDate;
    }

    public Company getResponsible() {
        return mResponsible;
    }

    public void setResponsible(Company responsible) {
        mResponsible = responsible;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public List<URI> getPhotos() {
        return mPhotos;
    }

    public void setPhotos(List<URI> photos) {
        mPhotos = photos;
    }

    public String getEventState() {
        return mEventState;
    }

    public void setEventState(String eventState) {
        mEventState = eventState;
    }


    public String getEventType() {
        return mEventType;
    }

    public void setEventType(String eventType) {
        mEventType = eventType;
    }

    public int getLikeCounter() {
        return mLikeCounter;
    }

    public void setLikeCounter(int likeCounter) {
        mLikeCounter = likeCounter;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    @Override
    public String toString() {
        return "Event{" +
                "mId='" + mId + '\'' +
                ", mEventState='" + mEventState + '\'' +
                ", mCreationDate=" + mCreationDate +
                ", mRegistrationDate=" + mRegistrationDate +
                ", mDeadlineDate=" + mDeadlineDate +
                ", mResponsible=" + mResponsible +
                ", mDescription='" + mDescription + '\'' +
                ", mPhotos=" + mPhotos +
                ", mEventType='" + mEventType + '\'' +
                ", mLikeCounter=" + mLikeCounter +
                ", mAddress='" + mAddress + '\'' +
                '}';
    }
}
