/*
Copyright info
*/


package ua.dp.strahovik.yalantistask1.entities;


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

}
