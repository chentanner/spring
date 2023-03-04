package com.sss.app.settings.model;

import com.sss.app.core.entity.model.AbstractEntity;
import com.sss.app.core.enums.TransactionErrorCode;
import com.sss.app.core.exception.ApplicationRuntimeException;
import com.sss.app.core.exception.ApplicationValidationException;
import com.sss.app.settings.snapshot.ApplicationSettingSnapshot;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "APPLICATION_SETTING")
@NamedQueries({
        @NamedQuery(
                name = ApplicationSetting.LOAD_ALL,
                query = "SELECT ApplicationSetting " +
                        "FROM ApplicationSetting ApplicationSetting " +
                        "ORDER BY ApplicationSetting.key ")
})
public class ApplicationSetting extends AbstractEntity {

    public static final String LOAD_ALL = "load all application settings";
    private String key;
    private String value;

    public static final int MAX_KEY_LENGTH = 50;
    public static final int MAX_VALUE_LENGTH = 1000;

    public ApplicationSetting() {
    }

    @Id
    @Column(name = "KEY")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key.trim();
    }

    @Column(name = "VALUE")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void delete() {
        getBaseDAO().delete(this);
    }


    public static List<ApplicationSetting> loadAll() {
        return getBaseDAO().executeQuery(ApplicationSetting.class, LOAD_ALL);
    }

    public void createWith(ApplicationSettingSnapshot snapshot) {
        if (snapshot.getKey() != null)
            this.key = snapshot.getKey().trim();
        this.value = snapshot.getValue();
        save();
    }

    public void updateWith(ApplicationSettingSnapshot snapshot) {
        this.value = snapshot.getValue();
        update();
    }

    public void validate() throws ApplicationValidationException {
        if (this.key == null) {
            throw new ApplicationValidationException(TransactionErrorCode.INVALID_APP_SETTING_KEY);
        }

        if (this.key.length() == 0 || key.length() > MAX_KEY_LENGTH) {
            throw new ApplicationValidationException(TransactionErrorCode.INVALID_APP_SETTING_KEY_LENGTH);
        }

        if (this.value != null &&
                this.value.length() > MAX_VALUE_LENGTH) {
            throw new ApplicationValidationException(TransactionErrorCode.INVALID_APP_SETTING_VALUE);
        }
    }

    public void save() {
        try {
            validate();
        } catch (ApplicationValidationException e) {
            throw new ApplicationRuntimeException(e.getErrorCode());
        }
        getBaseDAO().save(this);
    }

    protected void update() {
        try {
            validate();
        } catch (ApplicationValidationException e) {
            throw new ApplicationRuntimeException(e.getErrorCode());
        }
        getBaseDAO().flush();
    }

    public static void saveUpdate(ApplicationSettingSnapshot ApplicationSettingSnapshot) {

        ApplicationSetting applicationSettingExisting = fetch(ApplicationSettingSnapshot.getKey());
        if (applicationSettingExisting != null) {
            applicationSettingExisting.setValue(ApplicationSettingSnapshot.getValue());
            applicationSettingExisting.update();
        } else {
            applicationSettingExisting = new ApplicationSetting();
            applicationSettingExisting.setKey(ApplicationSettingSnapshot.getKey());
            applicationSettingExisting.setValue(ApplicationSettingSnapshot.getValue());
            applicationSettingExisting.save();
        }
    }

    public static ApplicationSetting fetch(String keyValue) {
        return getBaseDAO().loadNonEntity(ApplicationSetting.class, keyValue);
    }

}