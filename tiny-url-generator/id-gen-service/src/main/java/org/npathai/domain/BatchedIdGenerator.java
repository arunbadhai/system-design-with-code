package org.npathai.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Generates batch of ids
 */
public class BatchedIdGenerator {
    private Id current;

    public BatchedIdGenerator(Id current) {
        this.current = current;
    }

    public Batch generate(int batchSize) {
        Set<String> ids = new HashSet<>();
        for (int i = 0; i < batchSize; i++) {
            try {
                ids.add(getAndIncrementCurrentId());
            } catch (IdExhaustedException e) {
                // TODO handle this logically
                e.printStackTrace();
            }
        }
        return new Batch(ids);
    }

    private String getAndIncrementCurrentId() throws IdExhaustedException {
        String id = current.encode();
        current = current.incrementAndGet();
        return id;
    }
}