package core.delta.ticketsystem.model;

import android.content.Context;
import androidx.annotation.NonNull;
import core.delta.ticketsystem.R;

public class TicketModel {

    private int id;
    private String date;
    private int state;
    private int priority;
    private String department;
    private String mandate;
    private String targetDate;
    private String subject;
    private String description;
    private int userId = -1;
    private String userName = "user";

    public TicketModel(int id,
                       String date,
                       int state,
                       int priority,
                       String department,
                       String mandate,
                       String targetDate,
                       String subject,
                       String description,
                       int userId,
                       String userName) {

        this.id = id;
        this.date = date;
        this.state = state;
        this.priority = priority;
        this.department = department;
        this.mandate = mandate;
        this.targetDate = targetDate;
        this.subject = subject;
        this.description = description;
        this.userId = userId;
        this.userName = userName;
    }

    // constructor simplified for adding new note
    public TicketModel(String date,
                       String subject,
                       String description,
                       String department,
                       String userName) {

        this(-1, date, 0, 1,department, "mandate", "tDate",
                subject, description, -1, userName);
    }

    // toString is necessary for printing the contents of a class object
    @NonNull
    @Override
    public String toString() {
        return "TicketModel{" +
                "id=" + id +
                ", ticketDate='" + date + '\'' +
                ", state=" + state +
                ", priority=" + priority +
                ", mandate='" + mandate + '\'' +
                ", targetDate='" + targetDate + '\'' +
                ", subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", department='" + department + '\'' +
                '}';
    }

    public void updateData(TicketModel newData) {
        if (this.id == newData.getId()) {
            this.date           = newData.getDate();
            this.state          = newData.getState();
            this.priority       = newData.getPriority();
            this.department     = newData.getDepartment();
            this.mandate        = newData.getMandate();
            this.targetDate     = newData.getTargetDate();
            this.subject        = newData.getSubject();
            this.description    = newData.getDescription();
            this.userId         = newData.getUserId();
            this.userName       = newData.getUserName();
        }
    }

    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getMandate() {
        return mandate;
    }

    public void setMandate(String mandate) {
        this.mandate = mandate;
    }

    public String getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    // static methods
    public static String[] getStateOptions(Context context) {
        return context.getResources().getStringArray(R.array.ticket_states);
    }

    public static String[] getPriorityOptions(Context context) {
        return context.getResources().getStringArray(R.array.ticket_priorities);
    }
}
