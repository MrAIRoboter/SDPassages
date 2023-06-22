package ru.aircorp.sdpassages;

public class Client {
    public Boolean IsUnlimited;

    private int _remainingTime;
    private String _name;
    private Boolean _isBlocked;

    public Client(String name, int remainingTime){
        _remainingTime = remainingTime;
        _name = name;
        IsUnlimited = false;
        _isBlocked = false;

        UpdateState();
    }

    public void AddRemainingTime(int time){
        if((_remainingTime + time) > Integer.MAX_VALUE)
            _remainingTime = Integer.MAX_VALUE;
        else
            _remainingTime += time;

        UpdateState();
    }

    public void RemoveRemainingTime(int time){
        if((_remainingTime - time) < 0)
            _remainingTime = 0;
        else
            _remainingTime -= time;

        UpdateState();
    }

    public int GetRemainingTime(){
        return _remainingTime;
    }

    public String GetName(){
        return _name;
    }

    public boolean IsBlocked(){
        if(IsUnlimited == true)
            _isBlocked = false;

        return _isBlocked;
    }

    private void UpdateState(){
        if(_remainingTime > 0 || IsUnlimited == true)
            _isBlocked = false;
        else if(_remainingTime == 0 && IsUnlimited == false)
            _isBlocked = true;
    }
}
