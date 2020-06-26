package logic.service;

public interface IService {
    /**
     * Call this method before destroy the service. It is used to save the service state.
     */
    void takeDown();
}
