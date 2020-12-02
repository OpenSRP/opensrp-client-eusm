package org.smartregister.eusm.contract;

import android.app.Activity;

import androidx.annotation.NonNull;

import org.json.JSONObject;
import org.smartregister.eusm.model.StructureDetail;
import org.smartregister.eusm.model.TaskDetail;
import org.smartregister.tasking.adapter.TaskRegisterAdapter;
import org.smartregister.view.contract.BaseRegisterFragmentContract;

import java.util.List;

public interface TaskRegisterFragmentContract {

    interface View extends BaseRegisterFragmentContract.View {

        void initializePresenter();

        void onViewClicked(android.view.View view);

        void onResumption();

        TaskRegisterAdapter getAdapter();

        void startFormActivity(JSONObject form);

        Activity getActivity();

        StructureDetail getStructureDetail();
    }

    interface Presenter {
        void fetchData();

        View getView();

        void startForm(StructureDetail structureDetail, TaskDetail taskDetail, String formName);
    }

    interface Interactor {

        void fetchData(StructureDetail structureDetail, @NonNull InteractorCallBack callBack);

        void startForm(StructureDetail structureDetail, TaskDetail taskDetail, Activity activity, InteractorCallBack callBack, String formName);

        void injectAdditionalFields(JSONObject jsonForm, String formName,
                                          StructureDetail structureDetail,
                                          TaskDetail taskDetail);
    }

    interface InteractorCallBack {
        void onFetchedData(List<TaskDetail> taskDetailList);

        void onFormFetched(JSONObject jsonForm);
    }
}
