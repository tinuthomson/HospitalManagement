package com.zibbix.hospital.hospitalmanagement;


class appoint {
    private String date, doctor, session, counter;

    appoint(String date, String doctor, String session, String counter) {
        this.date = date;
        this.doctor = doctor;
        this.session = session;
        this.counter = counter;
    }

    String getDate() {
        return date;
    }

    String getDoctor() {
        return doctor;
    }

    String getSession() {
        return session;
    }

    String getCounter() {
        return counter;
    }


}
