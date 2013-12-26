package com.cefetmg.mmc.mrcpsp.process.mode.impl;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.cefetmg.mmc.mrcpsp.model.enums.EnumModesComparator;
import com.cefetmg.mmc.mrcpsp.model.main.Job;
import com.cefetmg.mmc.mrcpsp.model.main.Mode;
import com.cefetmg.mmc.mrcpsp.model.main.ModesInformation;
import com.cefetmg.mmc.mrcpsp.process.mode.ModeComparatorProcessor;
import com.cefetmg.mmc.mrcpsp.utils.CloneUtils;
import com.cefetmg.mmc.mrcpsp.utils.LogUtils;
import com.cefetmg.mmc.mrcpsp.utils.PropertyConstants;
import com.google.common.collect.Iterables;

public class ModeComparatorProcessorImpl implements ModeComparatorProcessor {
	
	private static final Logger log = Logger.getLogger(ModeComparatorProcessorImpl.class);
	
	private ModeComparator modeComparator;
	
	public ModeComparatorProcessorImpl() {
		modeComparator = new ModeComparator();
	}

	@Override
	public ModesInformation orderModeByRenewableAmountResources(Job job) {		
		ModesInformation modesInformation = job.getModesInformation();
		// to not lose the natural modes order		
		List<Mode> cloneModeList = CloneUtils.cloneModeList(job.getAvailableModes()); 
		
		modeComparator.setComparatorType(EnumModesComparator.MC_AMOUNT_RENEWABLE);		
		Collections.sort(cloneModeList, modeComparator);
		log.debug("JOB ID: " + job.getId() + " - MODES ORDER BY RENEWABLE AMOUNT RESOURCES: " 
				  + LogUtils.generateIDsModeListLog(cloneModeList));		
		
		Integer indexLowerRenewableConsumption = cloneModeList.get(PropertyConstants.INDEX_START).getId();
		modesInformation.setLowerRenewableConsumption(indexLowerRenewableConsumption);
		
		Integer indexGreaterRenewableConsumption = Iterables.getLast(cloneModeList).getId();		
		modesInformation.setGreaterRenewableConsumption(indexGreaterRenewableConsumption);
		
		return modesInformation;
	}

	@Override
	public ModesInformation orderModeByNonRenewableAmountResources(Job job) {
		ModesInformation modesInformation = job.getModesInformation();
		// to not lose the natural modes order		
		List<Mode> cloneModeList = CloneUtils.cloneModeList(job.getAvailableModes()); 
		
		modeComparator.setComparatorType(EnumModesComparator.MC_AMOUNT_NON_RENEWABLE);
		Collections.sort(cloneModeList, modeComparator);
		log.debug("JOB ID: " + job.getId() + " - MODES ORDER BY NON RENEWABLE AMOUNT RESOURCES: " 
				  + LogUtils.generateIDsModeListLog(cloneModeList));
		
		Integer indexLowerNonRenewableConsumption = cloneModeList.get(PropertyConstants.INDEX_START).getId();
		modesInformation.setLowerNonRenewableConsumption(indexLowerNonRenewableConsumption);
		
		Integer indexGreaterNonRenewableConsumption = Iterables.getLast(cloneModeList).getId();		
		modesInformation.setGreaterNonRenewableConsumption(indexGreaterNonRenewableConsumption);
		
		return modesInformation;
	}

	@Override
	public ModesInformation orderModeByDuration(Job job) {
		ModesInformation modesInformation = job.getModesInformation();
		// to not lose the natural modes order		
		List<Mode> cloneModeList = CloneUtils.cloneModeList(job.getAvailableModes()); 
		
		modeComparator.setComparatorType(EnumModesComparator.MC_DURATION);
		Collections.sort(cloneModeList, modeComparator);
		log.debug("JOB ID: " + job.getId() + " - MODES ORDER BY DURATION: " 
				  + LogUtils.generateIDsModeListLog(cloneModeList));
		
		Integer indexShorter = cloneModeList.get(PropertyConstants.INDEX_START).getId();
		modesInformation.setShorter(indexShorter);
		
		Integer indexLonger = Iterables.getLast(cloneModeList).getId();		
		modesInformation.setLonger(indexLonger);
		
		return modesInformation;
	}

	@Override
	public ModesInformation orderModeBySumResources(Job job) {
		ModesInformation modesInformation = job.getModesInformation();
		// to not lose the natural modes order		
		List<Mode> cloneModeList = CloneUtils.cloneModeList(job.getAvailableModes()); 
		
		modeComparator.setComparatorType(EnumModesComparator.MC_SUM_RESOURCES);
		Collections.sort(cloneModeList, modeComparator);
		log.debug("JOB ID: " + job.getId() + " - MODES ORDER BY SUM OF THE RESOURCES: " 
				  + LogUtils.generateIDsModeListLog(cloneModeList));
		
		Integer indexLowerSumComsuption = cloneModeList.get(PropertyConstants.INDEX_START).getId();
		modesInformation.setLowerSumComsuption(indexLowerSumComsuption);
		
		Integer indexHigherSumComsuption = Iterables.getLast(cloneModeList).getId();		
		modesInformation.setHigherSumComsuption(indexHigherSumComsuption);
		
		return modesInformation;
	}

}
