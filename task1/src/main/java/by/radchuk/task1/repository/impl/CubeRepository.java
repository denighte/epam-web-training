package by.radchuk.task1.repository.impl;

import by.radchuk.task1.action.CubeMath;
import by.radchuk.task1.entity.Cube;
import by.radchuk.task1.entity.CubeParameters;
import by.radchuk.task1.repository.FigureRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * contains cube instances in a hash map.
 * reevaluates their data every time they change.
 */
@Slf4j
public class CubeRepository implements FigureRepository<Cube> {
    /**
     * Singleton.
     */
    private static final CubeRepository INSTANCE = new CubeRepository();
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
     * cube data map.
     * access by id.
     */
    private final Map<Integer, CubeData> idToDataMap = new HashMap<>();
    /**
     * cube map.
     * access by id.
     */
    private final Map<Integer, Cube> idToCubeMap = new HashMap<>();
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
        CubeData objectData = new CubeData();
        int id = getID();
        object.setId(id);
        objectData.setId(id);
        idToCubeMap.put(id, object);
        idToDataMap.put(id, objectData);
        this.update(id);
    }

    /**
     * remove cube(s) by condition.
     * @param condition predicate, return true if remove otherwise false.
     */
    @Override
    public void remove(final Predicate<Cube> condition) {
        for (Cube cube : idToCubeMap.values()) {
            if (condition.test(cube)) {
                remove(cube.getId());
            }
        }

    }

    /**
     * remove object by id.
     * @param id id of the object.
     */
    @Override
    public void remove(final int id) {
        idToDataMap.remove(id);
        idToCubeMap.remove(id);
    }

    /**
     * find cube by id.
     * @param id id of the object.
     * @return
     */
    @Override
    public Cube find(final int id) {
        return idToCubeMap.get(id);
    }

    /**
     * find cubes matching condition.
     * @param condition predicate, return true if cube fits, otherwise false.
     * @return List of cubes matching condition.
     */
    @Override
    public List<Cube> find(final Predicate<Cube> condition) {
        List<Cube> cubeList = new ArrayList<>();
        for (Cube cube : idToCubeMap.values()) {
            if (condition.test(cube)) {
                cubeList.add(cube);
            }
        }
        return cubeList;
    }

    /**
     * find cube by cube parameters.
     * @param condition predicate, return true if cube fits, otherwise false.
     * @return List of cubes matching condition.
     */
    public List<Cube> findByParameters(
            final Predicate<CubeParameters> condition) {
        List<Cube> cubeList = new ArrayList<>();
        for (Integer id : idToDataMap.keySet()) {
            if (condition.test(idToDataMap.get(id))) {
                cubeList.add(idToCubeMap.get(id));
            }
        }
        return cubeList;
    }

    /**
     * returns sorted list of cubes.
     * @param comparator comparator interface.
     * @return list of objects
     */
    @Override
    public List<Cube> sort(final Comparator<Cube> comparator) {
        List<Cube> list = new ArrayList<>(idToCubeMap.values());
        list.sort(comparator);
        return list;
    }

    /**
     * returns sorted list of cubes.
     * compare by cube parameters.
     * @param comparator comparator interface
     * @return list of objects.
     */
    public List<Cube> sortByParameters(
            final Comparator<CubeParameters> comparator) {
        List<CubeData> dataList = new ArrayList<>(idToDataMap.values());
        dataList.sort(comparator);
        return dataList.stream()
                .map(data -> idToCubeMap.get(data.getId()))
                .collect(Collectors.toList());
    }

    /**
     * update method.
     * called each any registered cube changes.
     * @param id object id.
     */
    @Override
    public void update(final int id) {
        log.debug("Called recalculation for cube with id={}", id);
        Cube cube = idToCubeMap.get(id);
        CubeData data = idToDataMap.get(id);
        if (!calculateData(cube, data)) {
            log.warn("Can't update cube or cube data by id={}", id);
        }

    }

    /**
     * recalculates data for specified objects.
     * @param cube changed cube.
     * @param data data to recalculate.
     * @return true, if recalculated successfully, otherwise false.
     */
    private boolean calculateData(final Cube cube, final CubeData data) {
        if (data != null && cube != null) {
            log.debug("Recalculating cube data with id=", cube.getId());
            data.setSurfaceArea(math.surfaceArea(cube));
            data.setVolume(math.volume(cube));
            data.setOnCoordinatePlane(math.isOnCoordinatePlane(cube));
            data.setAxisCutVolumeRatio(math.axisCutVolumeRatio(cube));
            return true;
        }
        return false;
    }

    /**
     * increments id field.
     * @return new id.
     */
    private int getID() {
        return autoIncrementID++;
    }
}
