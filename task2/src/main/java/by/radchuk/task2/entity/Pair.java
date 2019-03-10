package by.radchuk.task2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Pair class.
 * Used to hold closely related values.
 * @param <K> key of the pair.
 * @param <V> value of the pair.
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Pair<K, V> {
    /**
     * key of the pair.
     */
    private K key;
    /**
     * value of the pair.
     */
    private V value;
}
