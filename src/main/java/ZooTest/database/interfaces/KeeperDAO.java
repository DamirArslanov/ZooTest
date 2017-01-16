package ZooTest.database.interfaces;

import ZooTest.entity.Keeper;

import java.util.List;
import java.util.Set;

/**
 * Created by ArslanovDamir on 16.12.2016.
 */
//Maybe <T>?
public interface KeeperDAO {

    public void addKeeper(Keeper keeper);

    public void updateKeeper(Keeper keeper);

    public Keeper getKeeperByID(int id);

    public void deleteKeeper(int id);

    public List<Keeper> getAllKeepers();

    public Keeper findKeeperByNameSurname(String name, String surname);
}

