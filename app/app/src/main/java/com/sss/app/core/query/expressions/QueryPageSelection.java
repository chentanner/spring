package com.sss.app.core.query.expressions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sss.app.core.entity.snapshot.BusinessKey;
import com.sss.app.core.enums.TransactionErrorCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QueryPageSelection implements Serializable {
    private static final long serialVersionUID = 1L;
    private Boolean resultsWereFiltered = false;
    private Boolean resultsWereTruncated = false;
    private String errorCode = TransactionErrorCode.SUCCESS.getCode();
    private String errorMessage;
    private boolean isPermissionException = false;

    private OrderClause orderClause;

    private List<Integer> selectedIds = new ArrayList<>();

    public QueryPageSelection(String errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public QueryPageSelection(String errorCode, boolean isPermissionException) {
        super();
        this.errorCode = errorCode;
        this.isPermissionException = isPermissionException;
    }

    public QueryPageSelection() {
    }

    public QueryPageSelection(Integer id) {
        selectedIds.add(id);
    }

    public QueryPageSelection(List<Integer> selectedIds) {
        super();
        this.selectedIds = selectedIds;
    }

    public QueryPageSelection(
            List<Integer> selectedIds,
            OrderClause orderClause) {
        super();
        this.selectedIds = selectedIds;
        this.orderClause = orderClause;
    }

    public void addSelectedId(Integer id) {
        selectedIds.add(id);
    }

    public void addAllSelectedIds(List<Integer> ids) {
        selectedIds.addAll(ids);
    }

    public List<Integer> getSelectedIds() {
        return selectedIds;
    }

    public Boolean getResultsWereFiltered() {
        return resultsWereFiltered;
    }

    public void setResultsWereFiltered(Boolean resultsWereFiltered) {
        this.resultsWereFiltered = resultsWereFiltered;
    }

    public Boolean getResultsWereTruncated() {
        return resultsWereTruncated;
    }

    public void setResultsWereTruncated(Boolean resultsWereTruncated) {
        this.resultsWereTruncated = resultsWereTruncated;
    }

    public void setSelectedIds(List<Integer> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public OrderClause getOrderClause() {
        return orderClause;
    }

    public void setOrderClause(OrderClause orderClause) {
        this.orderClause = orderClause;
    }

    @JsonIgnore
    public List<BusinessKey> getSelectedBusinessKeys() {
        if (selectedIds == null) {
            return null;
        }

        List<BusinessKey> selectedBusinessKeys = new ArrayList<>();
        for (Integer selectedId : selectedIds) {
            selectedBusinessKeys.add(new BusinessKey(selectedId));
        }

        return selectedBusinessKeys;
    }

    /**
     * Setter is required in order for getter to be serialized.
     *
     * @param keys
     */
    public void setSelectedBusinessKeys(List<BusinessKey> keys) {

    }

    public String toString() {
        return selectedIds.toString();
    }

    @JsonIgnore
    public boolean wasSuccessful() {
        if (errorCode == null)
            return true;
        return errorCode.equals(TransactionErrorCode.SUCCESS.getCode());
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
