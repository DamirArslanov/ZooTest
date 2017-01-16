package ZooTest.database.interfaces;

import ZooTest.entity.Cage;

import java.util.List;

/**
 * Created by ArslanovDamir on 18.12.2016.
 */
//Maybe <T>?
public interface CageDAO {

    public void addCage(Cage cage);

    public void updateCage(Cage cage);

    public Cage getCageByID(int id);

    public void deleteCage(int id);

    public List<Cage> getAllCages();

    public Cage findCageByNumber(int number);
}
