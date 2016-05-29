package ua.dp.strahovik.yalantistask1.entities;


public class Address {

    private String city;
    private String district;
    private String streetTypeName;
    private String streetTypeShortName;
    private String street;
    private String house;
    private String flat;

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetTypeName() {
        return streetTypeName;
    }

    public void setStreetTypeName(String streetTypeName) {
        this.streetTypeName = streetTypeName;
    }

    public String getStreetTypeShortName() {
        return streetTypeShortName;
    }

    public void setStreetTypeShortName(String streetTypeShortName) {
        this.streetTypeShortName = streetTypeShortName;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    @Override
    public String toString() {
        return "Address{" +
                "flat=" + flat +
                ", house='" + house + '\'' +
                ", streetTypeShortName='" + streetTypeShortName + '\'' +
                ", streetTypeName='" + streetTypeName + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                '}';
    }

    public String getFormattedToString() {
        StringBuilder addressOutput = new StringBuilder();
        if (getCity() == null) {
            if (getStreetTypeShortName() != null) {
                addressOutput
                        .append(getStreetTypeShortName())
                        .append(" ");
            }
        } else {
            addressOutput
                    .append(getCity())
                    .append(", ");
            if (getStreetTypeName() != null) {
                addressOutput
                        .append(getStreetTypeName())
                        .append(" ");
            }
        }
        if (getStreet() != null) {
            addressOutput
                    .append(getStreet())
                    .append(", ");
        }
        if (getHouse() != null) {
            addressOutput
                    .append(getHouse())
                    .append(", ");
        }
        if (getFlat() != null) {
            addressOutput
                    .append(getFlat())
                    .append(", ");
        }
        if (getDistrict() != null) {
            addressOutput
                    .append(getDistrict());
        }
        return addressOutput.toString();
    }
}
