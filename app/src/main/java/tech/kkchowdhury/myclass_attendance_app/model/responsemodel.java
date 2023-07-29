package tech.kkchowdhury.myclass_attendance_app.model;

public class responsemodel
{
    String message;

    public responsemodel(String message) {
        this.message = message;
    }

    public responsemodel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}