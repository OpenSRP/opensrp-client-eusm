package org.smartregister.eusm.util;

import androidx.annotation.NonNull;

import org.smartregister.domain.Location;
import org.smartregister.domain.Task;
import org.smartregister.eusm.model.StructureDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.smartregister.eusm.interactor.ListTaskInteractor.gson;
import static org.smartregister.eusm.util.AppConstants.BusinessStatus.BEDNET_DISTRIBUTED;
import static org.smartregister.eusm.util.AppConstants.BusinessStatus.BLOOD_SCREENING_COMPLETE;
import static org.smartregister.eusm.util.AppConstants.BusinessStatus.COMPLETE;
import static org.smartregister.eusm.util.AppConstants.BusinessStatus.FAMILY_REGISTERED;
import static org.smartregister.eusm.util.AppConstants.BusinessStatus.FULLY_RECEIVED;
import static org.smartregister.eusm.util.AppConstants.BusinessStatus.NONE_RECEIVED;
import static org.smartregister.eusm.util.AppConstants.BusinessStatus.NOT_ELIGIBLE;
import static org.smartregister.eusm.util.AppConstants.BusinessStatus.NOT_VISITED;
import static org.smartregister.eusm.util.AppConstants.GeoJSON.IS_INDEX_CASE;
import static org.smartregister.eusm.util.AppConstants.Intervention.BEDNET_DISTRIBUTION;
import static org.smartregister.eusm.util.AppConstants.Intervention.BLOOD_SCREENING;
import static org.smartregister.eusm.util.AppConstants.Intervention.CASE_CONFIRMATION;
import static org.smartregister.eusm.util.AppConstants.Intervention.MDA_ADHERENCE;
import static org.smartregister.eusm.util.AppConstants.Intervention.MDA_DISPENSE;
import static org.smartregister.eusm.util.AppConstants.Intervention.REGISTER_FAMILY;
import static org.smartregister.eusm.util.AppConstants.Properties.FAMILY_MEMBER_NAMES;
import static org.smartregister.eusm.util.AppConstants.Properties.FEATURE_SELECT_TASK_BUSINESS_STATUS;
import static org.smartregister.eusm.util.AppConstants.Properties.LOCATION_TYPE;
import static org.smartregister.eusm.util.AppConstants.Properties.LOCATION_UUID;
import static org.smartregister.eusm.util.AppConstants.Properties.LOCATION_VERSION;
import static org.smartregister.eusm.util.AppConstants.Properties.STRUCTURE_NAME;
import static org.smartregister.eusm.util.AppConstants.Properties.TASK_BUSINESS_STATUS;
import static org.smartregister.eusm.util.AppConstants.Properties.TASK_CODE;
import static org.smartregister.eusm.util.AppConstants.Properties.TASK_CODE_LIST;
import static org.smartregister.eusm.util.AppConstants.Properties.TASK_IDENTIFIER;
import static org.smartregister.eusm.util.AppConstants.Properties.TASK_STATUS;

/**
 * Created by samuelgithengi on 1/7/19.
 */
public class GeoJsonUtils {

    private static final String MDA_DISPENSE_TASK_COUNT = "mda_dispense_task_count";

    public static String getGeoJsonFromStructuresAndTasks(List<Location> structures, Map<String, Set<Task>> tasks, String indexCase, Map<String, StructureDetails> structureNames) {
        for (Location structure : structures) {
            Set<Task> taskSet = tasks.get(structure.getId());
            HashMap<String, String> taskProperties = new HashMap<>();

            StringBuilder interventionList = new StringBuilder();

            Map<String, Integer> mdaStatusMap = new HashMap<>();
            mdaStatusMap.put(FULLY_RECEIVED, 0);
            mdaStatusMap.put(NONE_RECEIVED, 0);
            mdaStatusMap.put(NOT_ELIGIBLE, 0);
            mdaStatusMap.put(MDA_DISPENSE_TASK_COUNT, 0);
            StateWrapper state = new StateWrapper();
            if (taskSet == null)
                continue;
            for (Task task : taskSet) {
                calculateState(task, state, mdaStatusMap);

                taskProperties = new HashMap<>();
                taskProperties.put(TASK_IDENTIFIER, task.getIdentifier());

                taskProperties.put(TASK_BUSINESS_STATUS, task.getBusinessStatus());

                taskProperties.put(FEATURE_SELECT_TASK_BUSINESS_STATUS, task.getBusinessStatus()); // used to determine action to take when a feature is selected
                taskProperties.put(TASK_STATUS, task.getStatus().name());
                taskProperties.put(TASK_CODE, task.getCode());

                if (indexCase != null && structure.getId().equals(indexCase)) {
                    taskProperties.put(IS_INDEX_CASE, Boolean.TRUE.toString());
                } else {
                    taskProperties.put(IS_INDEX_CASE, Boolean.FALSE.toString());
                }

                taskProperties.put(LOCATION_UUID, structure.getProperties().getUid());
                taskProperties.put(LOCATION_VERSION, structure.getProperties().getVersion() + "");
                taskProperties.put(LOCATION_TYPE, structure.getProperties().getType());
                interventionList.append(task.getCode());
                interventionList.append("~");

            }

            populateBusinessStatus(taskProperties, mdaStatusMap, state);

            taskProperties.put(TASK_CODE_LIST, interventionList.toString());
            if (structureNames.get(structure.getId()) != null) {
                taskProperties.put(STRUCTURE_NAME, structureNames.get(structure.getId()).getStructureName());
                taskProperties.put(FAMILY_MEMBER_NAMES, structureNames.get(structure.getId()).getFamilyMembersNames());
            }
            structure.getProperties().setCustomProperties(taskProperties);

        }
        return gson.toJson(structures);
    }

    private static void calculateState(Task task, StateWrapper state, @NonNull Map<String, Integer> mdaStatusMap) {
        if (Utils.isResidentialStructure(task.getCode())) {
            switch (task.getCode()) {
                case REGISTER_FAMILY:
                    state.familyRegTaskExists = true;
                    state.familyRegistered = COMPLETE.equals(task.getBusinessStatus());
                    state.ineligibleForFamReg = NOT_ELIGIBLE.equals((task.getBusinessStatus()));
                    break;
                case BEDNET_DISTRIBUTION:
                    state.bednetDistributed = COMPLETE.equals(task.getBusinessStatus()) || NOT_ELIGIBLE.equals(task.getBusinessStatus());
                    break;
                case BLOOD_SCREENING:
                    if (!state.bloodScreeningDone) {
                        state.bloodScreeningDone = COMPLETE.equals(task.getBusinessStatus()) || NOT_ELIGIBLE.equals(task.getBusinessStatus());
                    }
                    state.bloodScreeningExists = true;
                    break;
                case CASE_CONFIRMATION:
                    state.caseConfirmed = COMPLETE.equals(task.getBusinessStatus());
                    break;
                case MDA_ADHERENCE:
                    state.mdaAdhered = COMPLETE.equals(task.getBusinessStatus()) || NOT_ELIGIBLE.equals(task.getBusinessStatus());
                    break;
                case MDA_DISPENSE:
                    populateMDAStatus(task, mdaStatusMap);
                    break;
                default:
                    break;
            }

        }
    }

    private static void populateMDAStatus(Task task, Map<String, Integer> mdaStatusMap) {
        mdaStatusMap.put(MDA_DISPENSE_TASK_COUNT, mdaStatusMap.get(MDA_DISPENSE_TASK_COUNT) + 1);
        switch (task.getBusinessStatus()) {
            case FULLY_RECEIVED:
                mdaStatusMap.put(FULLY_RECEIVED, mdaStatusMap.get(FULLY_RECEIVED) + 1);
                break;
            case NONE_RECEIVED:
                mdaStatusMap.put(NONE_RECEIVED, mdaStatusMap.get(NONE_RECEIVED) + 1);
                break;
            case NOT_ELIGIBLE:
                mdaStatusMap.put(NOT_ELIGIBLE, mdaStatusMap.get(NOT_ELIGIBLE) + 1);
                break;
        }
    }

    private static void populateBusinessStatus(HashMap<String, String> taskProperties, Map<String, Integer> mdaStatusMap, StateWrapper state) {
        // The assumption is that a register structure task always exists if the structure has
        // atleast one bednet distribution or blood screening task
        if (Utils.isResidentialStructure(taskProperties.get(TASK_CODE))) {

            boolean familyRegTaskMissingOrFamilyRegComplete = state.familyRegistered || !state.familyRegTaskExists;

            if (Utils.isFocusInvestigation()) {

                if (familyRegTaskMissingOrFamilyRegComplete &&
                        state.bednetDistributed && state.bloodScreeningDone) {
                    taskProperties.put(TASK_BUSINESS_STATUS, COMPLETE);
                } else if (familyRegTaskMissingOrFamilyRegComplete &&
                        !state.bednetDistributed && (!state.bloodScreeningDone || (!state.bloodScreeningExists && !state.caseConfirmed))) {
                    taskProperties.put(TASK_BUSINESS_STATUS, FAMILY_REGISTERED);
                } else if (state.bednetDistributed && familyRegTaskMissingOrFamilyRegComplete) {
                    taskProperties.put(TASK_BUSINESS_STATUS, BEDNET_DISTRIBUTED);
                } else if (state.bloodScreeningDone) {
                    taskProperties.put(TASK_BUSINESS_STATUS, BLOOD_SCREENING_COMPLETE);
                } else if (state.ineligibleForFamReg) {
                    taskProperties.put(TASK_BUSINESS_STATUS, NOT_ELIGIBLE);
                } else {
                    taskProperties.put(TASK_BUSINESS_STATUS, NOT_VISITED);
                }

            }

        }
    }

    private static class StateWrapper {
        private boolean familyRegistered = false;
        private boolean bednetDistributed = false;
        private boolean bloodScreeningDone = false;
        private boolean familyRegTaskExists = false;
        private boolean caseConfirmed = false;
        private boolean mdaAdhered = false;
        private boolean fullyReceived;
        private boolean nonReceived;
        private boolean nonEligible;
        private boolean partiallyReceived;
        private boolean bloodScreeningExists = false;
        private boolean ineligibleForFamReg = false;
    }
}