package by.radchuk.task1.repository.impl;

import by.radchuk.task1.action.CubeMath;
import by.radchuk.task1.entity.Cube;
import by.radchuk.task1.entity.CubeData;
import by.radchuk.task1.repository.FigureRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * contains cube instances in a hash map.
 * reevaluates their data every time they change.
 */
@Slf4j
public class CubeRepository implements FigureRepository<Cube, CubeData> {
    /**
     * Singleton.
     */
    public static final CubeRepository INSTANCE = new CubeRepository();
    /**
     * returns CubeRepository instance.
     * @return CubeRepository instance.
     */
    public static CubeRepository getInstance() {
        return INSTANCE;
    }
    /**
     * id field, increases every time new object added.
     */
    private int autoIncrementID = 1;
    /**
     * cube storage.
     */
    private final List<Cube> cubeStorage = new ArrayList<>();
    /**
     * cube data storage.
     * access by id.
     */
    private final Map<Integer, CubeData> cubeDataStorage = new HashMap<>();
    /**
     * Cube math, which will be used to recalculate cube data.
     */
    private CubeMath math = new CubeMath();

    /**
     * adds cube to repository.
     * creates cube data associated tho the cube.
     * @param object object to add.
     */
    @Override
    public void add(final Cube object) {
        object.setId(getID());
        cubeStorage.add(object);
        CubeData objectData = new CubeData();
        cubeDataStorage.put(object.getId(), objectData);
        this.update(object.getId());
    }

    /**
     * removes cube from repository.
     * @param object object to remove.
     */
    @Override
    public void remove(final Cube object) {
        cubeStorage.remove(object);
        cubeDataStorage.remove(object.getId());
    }

    /**
     * remove cube(s) by condition.
     * @param condition predicate, return true if remove otherwise false.
     */
    @Override
    public void remove(final Predicate<Cube> condition) {
        for (Cube cube : cubeStorage) {
            if (condition.test(cube)) {
                remove(cube);
            }
        }

    }

    /**
     * find cube by condition.
     * @param condition predicate, return true if cube fits otherwise false.
     * @return cube matching condition.
     */
    @Override
    public Cube find(final Predicate<Cube> condition) {
        List<Cube> cubeList = new ArrayList<>();
        for (Cube cube : cubeStorage) {
            if (condition.test(cube)) {
                return cube;
            }
        }
        return null;
    }

    /**
     * find all cubes matching condition.
     * @param condition predicate, return true if cube fits, otherwise false.
     * @return List of cubes matching condition.
     */
    @Override
    public List<Cube> findAll(final Predicate<Cube> condition) {
        List<Cube> cubeList = new ArrayList<>();
        for (Cube cube : cubeStorage) {
            if (condition.test(cube)) {
                cubeList.add(cube);
            }
        }
        return cubeList;
    }

    /**
     * get cube data by id.
     * @param id id of the object.
     * @return cube data matching the specified cube id.
     */
    @Override
    public CubeData getCubeData(final int id) {
        return cubeDataStorage.get(id);
    }

    /**
     * returns sorted list of objects.
     * @param comparator comparator interface.
     * @return list of objects
     */
    @Override
    public List<Cube> sortBy(final Comparator<Cube> comparator) {
        List<Cube> list = new ArrayList<>();
        list.sort(comparator);
        return list;
    }

    /**
     * update method.
     * called each any registered cube changes.
     * @param id object id.
     */
    @Override
    public void update(final int id) {
        log.debug("Called recalculation for cube with id={}", id);
        Cube cube = find(c -> c.getId() == id);
        CubeData data = cubeDataStorage.get(id);
        if (data != null || cube != null) {
            log.debug("Recalculating cube data with id=", id);
            data.setSurfaceArea(math.surfaceArea(cube));
            data.setVolume(math.volume(cube));
            data.setOnCoordinatePlane(math.isOnCoordinatePlane(cube));
            data.setAxisCutVolumeRatio(math.axisCutVolumeRatio(cube));
        } else {
            log.warn("Can't update cube or cube data with id={}", id);
        }
    }

    /**
     * increments id field.
     * @return new id.
     */
    private int getID() {
        return autoIncrementID++;
    }
}
