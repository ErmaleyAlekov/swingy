package app;

public class Controler {
    private int state;
    public Controler() {state = 0;}
    public void setState(int newState) {state = newState;}
    public int getState() {return state;}
    public void inState() {state++;}
    public void deState() {state--;}
}
