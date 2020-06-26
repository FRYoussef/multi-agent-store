package control;

public interface AttachableController {

    /**
     * All controllers should use this method to update their views before show them.
     */
    void viewSetup();

    /**
     *
     * @param msg
     */
    void showMessage(String msg);

    /**
     * Use it before destroy the controller.
     */
    void takeDown();
}
