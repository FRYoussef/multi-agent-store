package logic.service;

public class DFA {
    private States st;

    public DFA(){
        this.st = States.Q0;
    }

    public void nextState(Alphabet symbol){

        if (symbol.equals(Alphabet.YES)) {

            this.st = st.yes;
        }
        else if (symbol.equals(Alphabet.NO)){
            this.st = st.no;
        }
        else {
            this.st = States.Q8;
        }
    }

    public States getState(){
        return st;
    }


}
