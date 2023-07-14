package com.sss.app.core.entity.snapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionResult extends BaseTransactionResult {

    List<BusinessKey> keys = new ArrayList<>();

    public TransactionResult() {
    }

    public TransactionResult(BusinessKey key) {
        this.keys.add(key);
    }

    public TransactionResult(List<BusinessKey> keys) {
        this.keys = keys;
    }

    public Integer getId() {
        return keys.stream()
                .findFirst()
                .orElse(BusinessKey.createNullBusinessKey())
                .getId();
    }

    public List<Integer> getIds() {
        return keys.stream()
                .map(BusinessKey::getId)
                .collect(Collectors.toList());
    }

    public BusinessKey getKey() {
        return keys.stream().findFirst().orElse(null);
    }

    public List<BusinessKey> getKeys() {
        return keys;
    }

    public void setKeys(List<BusinessKey> keys) {
        this.keys = keys;
    }

    public void addKey(BusinessKey key) {
        this.keys.add(key);
    }
}
