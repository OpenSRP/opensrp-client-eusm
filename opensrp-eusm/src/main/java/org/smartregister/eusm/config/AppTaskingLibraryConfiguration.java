package org.smartregister.eusm.config;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;

import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONObject;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Location;
import org.smartregister.domain.Task;
import org.smartregister.eusm.activity.EusmHomeActivity;
import org.smartregister.eusm.activity.EusmOfflineMapsActivity;
import org.smartregister.eusm.activity.StructureRegisterActivity;
import org.smartregister.eusm.application.EusmApplication;
import org.smartregister.eusm.job.LocationTaskServiceJob;
import org.smartregister.eusm.util.DefaultLocationUtils;
import org.smartregister.eusm.view.NavigationDrawerView;
import org.smartregister.tasking.activity.TaskingHomeActivity;
import org.smartregister.tasking.adapter.TaskRegisterAdapter;
import org.smartregister.tasking.contract.BaseContract;
import org.smartregister.tasking.contract.BaseDrawerContract;
import org.smartregister.tasking.contract.BaseFormFragmentContract;
import org.smartregister.tasking.layer.DigitalGlobeLayer;
import org.smartregister.tasking.model.BaseTaskDetails;
import org.smartregister.tasking.model.CardDetails;
import org.smartregister.tasking.model.TaskDetails;
import org.smartregister.tasking.model.TaskFilterParams;
import org.smartregister.tasking.repository.TaskingMappingHelper;
import org.smartregister.tasking.util.ActivityConfiguration;
import org.smartregister.tasking.util.GeoJsonUtils;
import org.smartregister.tasking.util.TaskingConstants;
import org.smartregister.tasking.util.TaskingJsonFormUtils;
import org.smartregister.tasking.util.TaskingLibraryConfiguration;
import org.smartregister.tasking.util.TaskingMapHelper;
import org.smartregister.tasking.viewholder.TaskRegisterViewHolder;
import org.smartregister.util.AppExecutors;

import java.util.List;
import java.util.Map;

import static org.smartregister.tasking.util.Utils.getGlobalConfig;

public class AppTaskingLibraryConfiguration extends TaskingLibraryConfiguration {

    @NonNull
    @Override
    public Pair<Drawable, String> getActionDrawable(Context context, TaskDetails task) {
        return null;
    }

    @Override
    public int getInterventionLabel() {
        return 0;
    }

    @NonNull
    @Override
    public Float getLocationBuffer() {
        return Float.valueOf(getGlobalConfig(TaskingConstants.CONFIGURATION.LOCATION_BUFFER_RADIUS_IN_METRES, "25"));
    }

    @Override
    public void startImmediateSync() {
        LocationTaskServiceJob.scheduleJobImmediately(LocationTaskServiceJob.TAG);
    }

    @Override
    public boolean validateFarStructures() {
        return false;
    }

    @Override
    public int getResolveLocationTimeoutInSeconds() {
        return 0;
    }

    @Override
    public String getAdminPasswordNotNearStructures() {
        return null;
    }

    @Override
    public boolean isFocusInvestigation() {
        return false;
    }

    @Override
    public boolean isMDA() {
        return false;
    }

    @Override
    public String getCurrentLocationId() {
        return null;
    }

    @Override
    public String getCurrentOperationalAreaId() {
        return null;
    }

    @Override
    public Integer getDatabaseVersion() {
        return null;
    }

    @Override
    public void tagEventTaskDetails(List<Event> events, SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public Boolean displayDistanceScale() {
        return Boolean.valueOf(getGlobalConfig(TaskingConstants.CONFIGURATION.DISPLAY_DISTANCE_SCALE, "false"));
    }

    @Override
    public String getFormName(@NonNull String encounterType, @Nullable String taskCode) {
        return null;
    }

    @Override
    public boolean resetTaskInfo(@NonNull SQLiteDatabase db, @NonNull BaseTaskDetails taskDetails) {
        return false;
    }

    @Override
    public boolean archiveClient(String baseEntityId, boolean isFamily) {
        return false;
    }

    @Override
    public String getTranslatedIRSVerificationStatus(String status) {
        return null;
    }

    @Override
    public String getTranslatedBusinessStatus(String businessStatus) {
        return null;
    }

    @Override
    public void formatCardDetails(CardDetails cardDetails) {

    }

    @Override
    public void processServerConfigs() {
        EusmApplication.getInstance().processServerConfigs();
    }

    @Override
    public Map<String, Integer> populateLabels() {
        return null;
    }

    @Override
    public void showBasicForm(BaseFormFragmentContract.View view, Context context, String formName) {

    }

    @Override
    public void onLocationValidated(@NonNull Context context, @NonNull BaseFormFragmentContract.View view, @NonNull BaseFormFragmentContract.Interactor interactor, @NonNull BaseTaskDetails baseTaskDetails, @NonNull Location structure) {

    }

    @Override
    public String mainSelect(String mainCondition) {
        return null;
    }

    @Override
    public String nonRegisteredStructureTasksSelect(String mainCondition) {
        return null;
    }

    @Override
    public String groupedRegisteredStructureTasksSelect(String mainCondition) {
        return null;
    }

    @Override
    public String[] taskRegisterMainColumns(String tableName) {
        return new String[0];
    }

    @Override
    public String familyRegisterTableName() {
        return null;
    }

    @Override
    public void saveCaseConfirmation(BaseContract.BaseInteractor baseInteractor, BaseContract.BasePresenter presenterCallBack, JSONObject jsonForm, String eventType) {

    }

    @Override
    public String calculateBusinessStatus(@NonNull org.smartregister.domain.Event event) {
        return null;
    }

    @Override
    public String getCurrentPlanId() {
        return null;
    }

    @Override
    public boolean getSynced() {
        return false;
    }

    @Override
    public void setSynced(boolean synced) {

    }

    @Override
    public boolean isMyLocationComponentEnabled() {
        return true;
    }

    @Override
    public void setMyLocationComponentEnabled(boolean myLocationComponentEnabled) {

    }

    @Override
    public Task generateTaskFromStructureType(@NonNull Context context, @NonNull String structureId, @NonNull String structureType) {
        return null;
    }

    @Override
    public void saveLocationInterventionForm(BaseContract.BaseInteractor baseInteractor, BaseContract.BasePresenter presenterCallBack, JSONObject jsonForm) {

    }

    @Override
    public void saveJsonForm(BaseContract.BaseInteractor baseInteractor, String json) {

    }

    @Override
    public void openFilterActivity(Activity activity, TaskFilterParams filterParams) {

    }

    @Override
    public void openFamilyProfile(Activity activity, CommonPersonObjectClient family, BaseTaskDetails taskDetails) {

    }

    @Override
    public void setTaskDetails(Activity activity, TaskRegisterAdapter taskAdapter, List<TaskDetails> tasks) {

    }

    @Override
    public void showNotFoundPopup(Activity activity, String opensrpId) {

    }

    @Override
    public void startMapActivity(Activity activity, String searchViewText, TaskFilterParams taskFilterParams) {
        Intent intent = new Intent(activity, EusmHomeActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onTaskRegisterBindViewHolder(@NonNull Context context, @NonNull TaskRegisterViewHolder viewHolder, @NonNull View.OnClickListener registerActionHandler, @NonNull TaskDetails taskDetails, int position) {

    }

    @NonNull
    @Override
    public AppExecutors getAppExecutors() {
        return EusmApplication.getInstance().getAppExecutors();
    }

    @Override
    public BaseDrawerContract.View getDrawerMenuView(BaseDrawerContract.DrawerActivity activity) {
        return new NavigationDrawerView(activity);
    }

    @Override
    public void showTasksCompleteActionView(TextView actionView) {

    }

    @Override
    public Map<String, Object> getServerConfigs() {
        return null;
    }

    @Override
    public TaskingJsonFormUtils getJsonFormUtils() {
        return new TaskingJsonFormUtils();
    }

    @Override
    public TaskingMappingHelper getMappingHelper() {
        return new TaskingMappingHelper();
    }

    @Override
    public TaskingMapHelper getMapHelper() {
        return new TaskingMapHelper();
    }

    @Override
    public boolean isRefreshMapOnEventSaved() {
        return false;
    }

    @Override
    public void setRefreshMapOnEventSaved(boolean isRefreshMapOnEventSaved) {

    }

    @Override
    public void setFeatureCollection(FeatureCollection featureCollection) {

    }

    @Override
    public DigitalGlobeLayer getDigitalGlobeLayer() {
        return new DigitalGlobeLayer();
    }

    @Override
    public List<String> getFacilityLevels() {
        return DefaultLocationUtils.getFacilityLevels();
    }

    @Override
    public List<String> getLocationLevels() {
        return DefaultLocationUtils.getLocationLevels();
    }

    @Override
    public ActivityConfiguration getActivityConfiguration() {
        return ActivityConfiguration.builder().offlineMapsActivity(EusmOfflineMapsActivity.class).build();
    }

    @Override
    public void registerFamily(Feature selectedFeature) {

    }

    @Override
    public void openTaskRegister(TaskFilterParams filterParams, TaskingHomeActivity activity) {
        Intent intent = new Intent(activity, StructureRegisterActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public boolean isCompassEnabled() {
        return false;
    }

    @Override
    public boolean showCurrentLocationButton() {
        return false;
    }

    @Override
    public boolean disableMyLocationOnMapMove() {
        return false;
    }

    @Override
    public Boolean getDrawOperationalAreaBoundaryAndLabel() {
        return false;
    }

    @Override
    public GeoJsonUtils getGeoJsonUtils() {
        return new GeoJsonUtils();
    }
}
