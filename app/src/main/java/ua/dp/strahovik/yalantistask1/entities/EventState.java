package ua.dp.strahovik.yalantistask1.entities;


public class EventState {

    private String state;
    private int stateId;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public String toString() {
        return "EventState{" +
                "state='" + state + '\'' +
                ", stateId=" + stateId +
                '}';
    }
}
