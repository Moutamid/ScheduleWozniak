package com.example.schedulewozniak;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class EmployeeModel implements Serializable {

    private int emp_Id;
    private String emp_FName ;
    private String emp_LName ;
    private String emp_nName;
    private String emp_Email;
    private String emp_Phone;
    private boolean emp_Active;
    private boolean emp_OpenTrained;
    private boolean emp_CloseTrained;
    private List<EmployeeAvailability> employeeAvailabilityList;
    private List<Date> daysOff;

    public EmployeeModel(int emp_Id, String emp_FName, String emp_LName, String emp_nName, String emp_Email, String emp_Phone, boolean emp_Active, boolean emp_OpenTrained, boolean emp_CloseTrained, List<EmployeeAvailability> employeeAvailabilityList, List<Date> daysOff) {
        this.emp_Id = emp_Id;
        this.emp_FName = emp_FName;
        this.emp_LName = emp_LName;
        this.emp_nName = emp_nName;
        this.emp_Email = emp_Email;
        this.emp_Phone = emp_Phone;
        this.emp_Active = emp_Active;
        this.emp_OpenTrained = emp_OpenTrained;
        this.emp_CloseTrained = emp_CloseTrained;
        this.employeeAvailabilityList = employeeAvailabilityList;
        this.daysOff = daysOff;
    }

    public EmployeeModel(String emp_FName, String emp_LName, String emp_nName, String emp_Email, String emp_Phone, boolean emp_Active) {
        this.emp_FName = emp_FName;
        this.emp_LName = emp_LName;
        this.emp_nName = emp_nName;
        this.emp_Email = emp_Email;
        this.emp_Phone = emp_Phone;
        this.emp_Active = emp_Active;
    }

    public EmployeeModel(int emp_Id, String emp_FName, String emp_LName, String emp_nName, boolean emp_Active) {
        this.emp_Id = emp_Id;
        this.emp_FName = emp_FName;
        this.emp_LName = emp_LName;
        this.emp_nName = emp_nName;
        this.emp_Active = emp_Active;
    }

    public EmployeeModel() {
    }

    public int getEmp_Id() {
        return emp_Id;
    }

    public void setEmp_Id(int emp_Id) {
        this.emp_Id = emp_Id;
    }

    public String getEmp_FName() {
        return emp_FName;
    }

    public void setEmp_FName(String emp_FName) {
        this.emp_FName = emp_FName;
    }

    public String getEmp_LName() {
        return emp_LName;
    }

    public void setEmp_LName(String emp_LName) {
        this.emp_LName = emp_LName;
    }

    public String getEmp_Email() {
        return emp_Email;
    }

    public void setEmp_Email(String emp_Email) {
        this.emp_Email = emp_Email;
    }

    public String getEmp_Phone() {
        return emp_Phone;
    }

    public void setEmp_Phone(String emp_Phone) {
        this.emp_Phone = emp_Phone;
    }

    public boolean isEmp_OpenTrained() {
        return emp_OpenTrained;
    }

    public void setEmp_OpenTrained(boolean emp_OpenTrained) {
        this.emp_OpenTrained = emp_OpenTrained;
    }

    public boolean isEmp_CloseTrained() {
        return emp_CloseTrained;
    }

    public void setEmp_CloseTrained(boolean emp_CloseTrained) {
        this.emp_CloseTrained = emp_CloseTrained;
    }

    public List<EmployeeAvailability> getWeeklyAvailability() {
        return employeeAvailabilityList;
    }

    public void setEmployeeAvailabilityList(List<EmployeeAvailability> employeeAvailabilityList) {
        this.employeeAvailabilityList = employeeAvailabilityList;
    }

    public List<Date> getDaysOff() {
        return daysOff;
    }

    public void setDaysOff(List<Date> daysOff) {
        this.daysOff = daysOff;
    }

    public String getEmp_nName() {
        return emp_nName;
    }

    public void setEmp_nName(String emp_nName) {
        this.emp_nName = emp_nName;
    }

    public boolean isEmp_Active() {
        return emp_Active;
    }

    public void setEmp_Active(boolean emp_Active) {
        this.emp_Active = emp_Active;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(!(emp_nName == null) && !(emp_nName.isEmpty()) ){
            //return name;
            sb.append(emp_nName);
        } else {
            sb.append(emp_FName).append(" ").append(emp_LName);
        }

        if(emp_OpenTrained&& emp_CloseTrained){
            sb.append(" [Fully Trained]");
        } else if (emp_OpenTrained) {
            sb.append(" [Opening Trained]");
        } else if (emp_CloseTrained) {
            sb.append(" [Closing Trained]");
        }
        return sb.toString();
    }
}
