package com.sss.app.settings.controller;

import com.sss.app.core.controllers.BaseRestController;
import com.sss.app.core.entity.snapshot.ErrorResponse;
import com.sss.app.core.enums.UrlRequestParam;
import com.sss.app.core.exception.AppPermissionException;
import com.sss.app.core.exception.ApplicationRuntimeException;
import com.sss.app.core.snapshot.RestResult;
import com.sss.app.settings.adapter.ApplicationSettingRestAdapter;
import com.sss.app.settings.snapshot.ApplicationSettingResult;
import com.sss.app.settings.snapshot.ApplicationSettingSnapshot;
import com.sss.app.settings.snapshot.ApplicationSettingSnapshotCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/applicationSettings")
public class ApplicationSettingRestController extends BaseRestController {
    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private ApplicationSettingRestAdapter applicationSettingRestAdapter;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ApplicationSettingSnapshotCollection> getApplicationSettings(
            @RequestHeader Map<String, String> headers,
            @RequestParam(value = UrlRequestParam.URL_PARM_FILTER, required = false) String filter,
            @RequestParam(value = UrlRequestParam.URL_PARM_START, defaultValue = "0") long start,
            @RequestParam(value = UrlRequestParam.URL_PARM_LIMIT, defaultValue = "100") int limit
    ) {
        ApplicationSettingSnapshotCollection snapshotCollection;
        try {
            if (filter == null)
                snapshotCollection = applicationSettingRestAdapter.fetchApplicationSettings(
                        start,
                        limit);
            else
                snapshotCollection = applicationSettingRestAdapter.findApplicationSettings(
                        filter,
                        start,
                        limit);
        } catch (AppPermissionException p) {
            snapshotCollection = new ApplicationSettingSnapshotCollection(p.getErrorCode(), true);
        } catch (ApplicationRuntimeException e) {
            snapshotCollection = new ApplicationSettingSnapshotCollection(e.getErrorCode());
        }

        HttpHeaders outHeaders = getHeaders();

        if (snapshotCollection.getErrorCode() != null) {
            return new ResponseEntity<>(snapshotCollection, outHeaders, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(snapshotCollection, outHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/cached", method = RequestMethod.GET)
    public ResponseEntity<ApplicationSettingSnapshotCollection> getCachedApplicationSettings(
            @RequestHeader Map<String, String> headers,
            @RequestParam(value = UrlRequestParam.URL_PARM_FILTER, required = false) String filter,
            @RequestParam(value = UrlRequestParam.URL_PARM_START, defaultValue = "0") long start,
            @RequestParam(value = UrlRequestParam.URL_PARM_LIMIT, defaultValue = "100") int limit
    ) {
        ApplicationSettingSnapshotCollection snapshotCollection;
        try {
            if (filter == null)
                snapshotCollection = applicationSettingRestAdapter.fetchCachedApplicationSettings(
                        start,
                        limit);
            else
                snapshotCollection = applicationSettingRestAdapter.findCachedApplicationSettings(
                        filter,
                        start,
                        limit);
        } catch (AppPermissionException p) {
            snapshotCollection = new ApplicationSettingSnapshotCollection(p.getErrorCode(), true);
        } catch (ApplicationRuntimeException e) {
            snapshotCollection = new ApplicationSettingSnapshotCollection(e.getErrorCode());
        }

        HttpHeaders outHeadersheaders = getHeaders();

        if (snapshotCollection.getErrorCode() != null) {
            return new ResponseEntity<>(snapshotCollection, outHeadersheaders, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(snapshotCollection, outHeadersheaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/reloaded", produces = "application/json", method = RequestMethod.POST)
    public ResponseEntity<RestResult> reloadApplicationSettingCache(
            @RequestHeader Map<String, String> headers) {

        RestResult result;
        try {
            result = applicationSettingRestAdapter.reloadApplicationSettingCache();
        } catch (AppPermissionException p) {
            result = new RestResult(new ErrorResponse(p.getErrorCode(), null));
        } catch (ApplicationRuntimeException bre) {
            result = new RestResult(new ErrorResponse(bre.getErrorCode(), null));
        }

        return assembleResultResponseEntity(result);

    }

    @RequestMapping(value = "/cached", produces = "application/json", method = RequestMethod.POST)
    public ResponseEntity<ApplicationSettingResult> saveCachedApplicationSetting(
            @RequestHeader Map<String, String> headers,
            @RequestBody ApplicationSettingSnapshot applicationSetting,
            BindingResult bindingResult) {

        if (bindingResult.getErrorCount() > 0) {
            return handleBindingErrors(
                    bindingResult,
                    "Errors on ApplicationSetting POST"
            );
        }

        ApplicationSettingResult result;
        try {
            result = applicationSettingRestAdapter.saveCachedApplicationSetting(applicationSetting);
        } catch (AppPermissionException p) {
            result = new ApplicationSettingResult(new ErrorResponse(p.getErrorCode(), null));
        } catch (ApplicationRuntimeException bre) {
            result = new ApplicationSettingResult(new ErrorResponse(bre.getErrorCode(), null));
        }

        return assembleApplicationSettingResultResponseEntity(result);
    }


    @RequestMapping(value = "/cached", produces = "application/json", method = RequestMethod.PUT)
    public ResponseEntity<ApplicationSettingResult> saveCachedpplicationControls(
            @RequestHeader Map<String, String> headers,
            @RequestBody List<ApplicationSettingSnapshot> applicationSettings,
            BindingResult bindingResult) {

        if (bindingResult.getErrorCount() > 0) {
            return handleBindingErrors(
                    bindingResult,
                    "Errors on ApplicationSetting POST"
            );
        }

        ApplicationSettingResult result;
        try {
            result = applicationSettingRestAdapter.saveCachedApplicationSettings(applicationSettings);
        } catch (AppPermissionException p) {
            result = new ApplicationSettingResult(new ErrorResponse(p.getErrorCode(), null));
        } catch (ApplicationRuntimeException bre) {
            result = new ApplicationSettingResult(new ErrorResponse(bre.getErrorCode(), null));
        }

        return assembleApplicationSettingResultResponseEntity(result);
    }


    @RequestMapping(produces = "application/json", consumes = "application/json", method = RequestMethod.POST)
    public ResponseEntity<ApplicationSettingResult> saveApplicationSetting(
            @RequestHeader Map<String, String> headers,
            @RequestBody ApplicationSettingSnapshot applicationSetting,
            BindingResult bindingResult) {

        if (bindingResult.getErrorCount() > 0) {
            return handleBindingErrors(
                    bindingResult,
                    "Errors on ApplicationSetting POST"
            );
        }

        ApplicationSettingResult result;
        try {
            result = applicationSettingRestAdapter.saveApplicationSetting(applicationSetting);
        } catch (AppPermissionException p) {
            result = new ApplicationSettingResult(new ErrorResponse(p.getErrorCode(), null));
        } catch (ApplicationRuntimeException bre) {
            result = new ApplicationSettingResult(new ErrorResponse(bre.getErrorCode(), null));
        }


        return assembleApplicationSettingResultResponseEntity(result);
    }

    @RequestMapping(produces = "application/json", consumes = "application/json", method = RequestMethod.PUT)
    public ResponseEntity<ApplicationSettingResult> saveApplicationSettings(
            @RequestHeader Map<String, String> headers,
            @RequestBody List<ApplicationSettingSnapshot> applicationSettingValues,
            BindingResult bindingResult) {


        ApplicationSettingResult result;
        try {
            result = applicationSettingRestAdapter.saveApplicationSettings(applicationSettingValues);
        } catch (AppPermissionException p) {
            result = new ApplicationSettingResult(new ErrorResponse(p.getErrorCode(), null));
        } catch (ApplicationRuntimeException bre) {
            result = new ApplicationSettingResult(new ErrorResponse(bre.getErrorCode(), null));
        }

        return assembleApplicationSettingResultResponseEntity(result);
    }

}
