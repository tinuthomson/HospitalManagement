package com.zibbix.hospital.hospitalmanagement;


class appoint {
    private String date, doctor;

    appoint(String date1, String s, String date, String doctor) {
        this.date = date;
        this.doctor = doctor;

    }

    String getDate() {
        return date;
    }

    String getDoctor() {
        return doctor;
    }


}
