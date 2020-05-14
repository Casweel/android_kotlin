package com.example.lab3;

public class Students
{
    private int id;
    private String fio;
    private String dtime;

    Students(int id,String fio,String dtime)
    {
        this.id = id;
        this.fio = fio;
        this.dtime = dtime;
    }

    public int getId() {
        return id;
    }

    public String getDtime() {
        return dtime;
    }

    public String getFio() {
        return fio;
    }
}