package logic.service.itemSelectorService;

public enum States {

    Q0(false), Q1(true), Q2(false), Q3(true), Q4(false), Q5(false), Q6(true), Q7(true), Q8(false);

    final private boolean accept;

    States(boolean accept) {
        this.accept = accept;
    }

    States yes;
    States no;

    static { //transitions
        Q0.yes = Q2;
        Q0.no = Q1;
        Q2.yes = Q3;
        Q2.no = Q4;
        Q4.yes = Q5;
        Q4.no = Q6;
        Q5.yes = Q7;
        Q5.no = Q8;
        Q8.yes = Q8;
        Q8.no = Q8;
    }
}