package org.smartregister.eusm.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.reflect.Whitebox;
import org.smartregister.domain.Location;
import org.smartregister.eusm.BaseUnitTest;
import org.smartregister.eusm.application.EusmApplication;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.util.Cache;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.smartregister.eusm.util.AppConstants.Preferences.CURRENT_DISTRICT;
import static org.smartregister.eusm.util.AppConstants.Preferences.CURRENT_FACILITY;
import static org.smartregister.eusm.util.AppConstants.Preferences.CURRENT_OPERATIONAL_AREA;
import static org.smartregister.eusm.util.AppConstants.Preferences.CURRENT_OPERATIONAL_AREA_ID;
import static org.smartregister.eusm.util.AppConstants.Preferences.CURRENT_REGION;
import static org.smartregister.eusm.util.AppConstants.Preferences.FACILITY_LEVEL;

/**
 * Created by Richard Kareko on 4/15/20.
 */

public class PreferencesUtilTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private AllSharedPreferences allSharedPreferences;

    @Mock
    private Cache<Location> cache;

    @Mock
    private Location location;

    private PreferencesUtil preferencesUtil;

    @Before
    public void setUp() {
        preferencesUtil = PreferencesUtil.getInstance();
        Whitebox.setInternalState(preferencesUtil, "allSharedPreferences", allSharedPreferences);
    }

    @After
    public void tearDown() {
        Whitebox.setInternalState(preferencesUtil, "allSharedPreferences", EusmApplication.getInstance().getContext().allSharedPreferences());
    }

    @Test
    public void testSetCurrentFacility() {
        preferencesUtil.setCurrentFacility("Akros_1");
        verify(allSharedPreferences).savePreference(CURRENT_FACILITY, "Akros_1");
    }

    @Test
    public void testGetCurrentFacility() {
        when(allSharedPreferences.getPreference(CURRENT_FACILITY)).thenReturn("Akros_1");

        String actualCurrentFacility = preferencesUtil.getCurrentFacility();
        assertEquals("Akros_1", actualCurrentFacility);
    }

    @Test
    public void testSetCurrentDistrict() {
        preferencesUtil.setCurrentDistrict("Chadiza");
        verify(allSharedPreferences).savePreference(CURRENT_DISTRICT, "Chadiza");
    }

    @Test
    public void testGetCurrentDistrict() {
        when(allSharedPreferences.getPreference(CURRENT_DISTRICT)).thenReturn("Chadiza");

        String actualCurrentDistrict = preferencesUtil.getCurrentDistrict();
        assertEquals("Chadiza", actualCurrentDistrict);
    }

    @Test
    public void testSetCurrentProvince() {
        preferencesUtil.setCurrentRegion("Lusaka");
        verify(allSharedPreferences).savePreference(CURRENT_REGION, "Lusaka");
    }

    @Test
    public void testGetCurrentProvince() {
        when(allSharedPreferences.getPreference(CURRENT_REGION)).thenReturn("Lusaka");

        String actualCurrentProvince = preferencesUtil.getCurrentRegion();
        assertEquals("Lusaka", actualCurrentProvince);
    }

    @Test
    public void testGetPreferenceValue() {
        when(allSharedPreferences.getPreference(CURRENT_REGION)).thenReturn("Lusaka");

        String actualPreferenceValue = preferencesUtil.getPreferenceValue(CURRENT_REGION);
        assertEquals("Lusaka", actualPreferenceValue);
    }

    @Test
    public void testSetCurrentFacilityLevel() {
        preferencesUtil.setCurrentFacilityLevel("Village");
        verify(allSharedPreferences).savePreference(FACILITY_LEVEL, "Village");
    }

    @Test
    public void testGetCurrentFacilityLevel() {
        when(allSharedPreferences.getPreference(FACILITY_LEVEL)).thenReturn("Village");

        String actualFacilityLevel = preferencesUtil.getCurrentFacilityLevel();
        assertEquals("Village", actualFacilityLevel);
    }


    @Test
    public void testSetCurrentOperationalAreaShouldUpdatePreferences() {
        String operationalArea = "oa_1";
        Whitebox.setInternalState(AppUtils.class, "cache", cache);
        when(cache.get(eq(operationalArea), any())).thenReturn(location);
        when(location.getId()).thenReturn("id_11121121");
        when(preferencesUtil.getCurrentOperationalArea()).thenReturn(operationalArea);

        preferencesUtil.setCurrentOperationalArea(operationalArea);
        verify(allSharedPreferences).savePreference(CURRENT_OPERATIONAL_AREA, operationalArea);
        verify(allSharedPreferences).savePreference(CURRENT_OPERATIONAL_AREA_ID, "id_11121121");
    }

    @Test
    public void testSetCurrentOperationalAreaShouldClearPreferences() {
        preferencesUtil.setCurrentOperationalArea(null);
        verify(allSharedPreferences).savePreference(CURRENT_OPERATIONAL_AREA, null);
        verify(allSharedPreferences).savePreference(CURRENT_OPERATIONAL_AREA_ID, null);
    }

}
