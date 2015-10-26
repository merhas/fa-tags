package fr.xorus.software.fa;

/**
 * Created by Xorus on 26/10/2015.
 */
public interface StatusUpdatable {
    void setStatus(String status);
    void setStatus(String status, Integer progress);
}
