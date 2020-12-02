package org.smartregister.eusm.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.eusm.BaseUnitTest;
import org.smartregister.eusm.R;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.smartregister.eusm.model.OfflineMapModel.OfflineMapStatus.DOWNLOADED;

/**
 * Created by Richard Kareko on 1/24/20.
 */

public class AvailableOfflineMapViewHolderTest extends BaseUnitTest {

    private final Context context = RuntimeEnvironment.application;
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    private View.OnClickListener onClickListener;
    private AvailableOfflineMapViewHolder viewHolder;

    @Before
    public void setUp() {
        View view = LayoutInflater.from(context).inflate(R.layout.offline_map_row, null);
        viewHolder = new AvailableOfflineMapViewHolder(view);
    }

    @Test
    public void testSetOfflineMapLabel() {
        viewHolder.setOfflineMapLabel("Akros_1");
        assertEquals("Akros_1", ((TextView) viewHolder.itemView.findViewById(R.id.offline_map_label)).getText());
    }

    @Test
    public void testSetItemViewListener() {
        OfflineMapModel expectedOfflineMapModel = new OfflineMapModel();
        Date dateCreated = new Date();
        expectedOfflineMapModel.setDateCreated(dateCreated);
        expectedOfflineMapModel.setOfflineMapStatus(DOWNLOADED);

        viewHolder.setItemViewListener(expectedOfflineMapModel, onClickListener);

        CheckBox actualCheckBox = viewHolder.itemView.findViewById(R.id.offline_map_checkbox);
        assertNotNull(actualCheckBox);
        OfflineMapModel actualOfflineMapModel = (OfflineMapModel) actualCheckBox.getTag(R.id.offline_map_checkbox);
        assertNotNull(actualOfflineMapModel);

        assertEquals(dateCreated, actualOfflineMapModel.getDateCreated());
        assertEquals(DOWNLOADED, actualOfflineMapModel.getOfflineMapStatus());
    }

    @Test
    public void testEnableCheckBox() {

        viewHolder.enableCheckBox(true);

        CheckBox actualCheckBox = viewHolder.itemView.findViewById(R.id.offline_map_checkbox);
        assertNotNull(actualCheckBox);
        assertTrue(actualCheckBox.isEnabled());
    }

    @Test
    public void testDisableCheckBox() {

        viewHolder.enableCheckBox(false);

        CheckBox actualCheckBox = viewHolder.itemView.findViewById(R.id.offline_map_checkbox);
        assertNotNull(actualCheckBox);
        assertFalse(actualCheckBox.isEnabled());
    }

    @Test
    public void testCheckCheckBox() {

        viewHolder.checkCheckBox(true);

        CheckBox actualCheckBox = viewHolder.itemView.findViewById(R.id.offline_map_checkbox);
        assertNotNull(actualCheckBox);
        assertTrue(actualCheckBox.isChecked());
    }

    @Test
    public void testUnCheckCheckBox() {

        viewHolder.checkCheckBox(false);

        CheckBox actualCheckBox = viewHolder.itemView.findViewById(R.id.offline_map_checkbox);
        assertNotNull(actualCheckBox);
        assertFalse(actualCheckBox.isChecked());
    }

    @Test
    public void testDisplayDownloadingLabel() {

        viewHolder.displayDownloadingLabel(true);

        TextView downloadingLabel = viewHolder.itemView.findViewById(R.id.offline_map_label);
        assertNotNull(downloadingLabel);
        assertEquals(View.VISIBLE, downloadingLabel.getVisibility());
    }

    @Test
    public void testHideDownloadingLabel() {

        viewHolder.displayDownloadingLabel(false);

        TextView downloadingLabel = viewHolder.itemView.findViewById(R.id.offline_map_label);
        assertNotNull(downloadingLabel);
        assertEquals(View.GONE, downloadingLabel.getWindowVisibility());
    }


}
